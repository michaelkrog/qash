package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.PaymentType;
import java.util.List;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author michaelzachariassenkrog
 */
public class PaymentCrudImpl extends ContentCrud<Payment> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentCrudImpl.class);
    private final Crud.Editable<String, Order> orderCrud;
    private EntityManager em;

    public PaymentCrudImpl(EntityManager em, Organisation organisation, Crud.Editable<String, Order> orderCrud) {
        super(em, organisation, Payment.class);
        this.em = em;

        if(orderCrud == null) {
            throw new NullPointerException("orderCrud was null.");
        }
        this.orderCrud = orderCrud;
    }

    @Override
    @Transactional
    public <E extends Payment> String create(E entity) {
        LOG.debug("Persisting payment.");
        ensurePaymentOk(entity);
        Order order = getDetachedOrder(entity);
        if(order!=null) {
            ensurePaymentOrderOk(entity, order);
            updateOrderAccordingToPayment(entity, order);
        }
        
        return super.create(entity);
    }

    
    @Override
    @Transactional
    public void update(Payment entity) {
        LOG.debug("Updating payment [id={}]", entity.getId());

        ensurePaymentOk(entity);
        if(entity.getOrderId()==null) {
            throw new NullPointerException("Payment has no order specified, which is required.");
        }
        
        Order order = getDetachedOrder(entity);
        ensurePaymentOrderOk(entity, order);
        
        
        updateOrderAccordingToPayment(entity, order);

        fireOnBeforeUpdate(entity.getId(), entity);


        //Persist payment
        em.merge(entity);
        em.flush();

        fireOnUpdate(entity.getId(), entity);
    }

    private double getTotalPayment(String orderId) {
        Filter filter = new CompareFilter<String>("orderId", orderId, CompareFilter.CompareType.Equals);
        List<String> idlist = listIds(filter, null, null);

        double value = 0;
        for(String id : idlist) {
            Payment p = read(id);
            if(p!=null) {
                value+=p.getAmount();
            }
        }
        return value;

    }

    private void ensurePaymentOk(Payment entity) {
        Payment existingPayment = entity.getId() == null ? null : read(entity.getId());
        boolean firstPersist = existingPayment == null ? true : existingPayment.getOrderId() == null;
        if(!firstPersist) {
            throw new IllegalStateException("Persisting a payment twice is not allowed.");
        }

    }
    
    private void ensurePaymentOrderOk(Payment entity, Order order) {
        if(!order.getStatus().isConfirmedState()) {
            throw new IllegalStateException("Payments only possible for accepted orders.");
        }

        if(entity.getPaymentType() == PaymentType.Change && order.getStatus() != OrderStatus.Completed) {
            throw new IllegalStateException("Change can only be registered for an order when it is complete.");
        }

        if(entity.getPaymentType() != PaymentType.Change && order.getStatus() == OrderStatus.Completed) {
            throw new IllegalStateException("Ordinary payments can only be registered for an order when it is NOT complete.");
        }
    }
    
    private void updateOrderAccordingToPayment(Payment entity, Order order) {
        if(order==null) return;
        //Get total from a fresh order
        double total = order.getTotalWithTax();

        //Calculate due
        double payments = getTotalPayment(order.getId());
        double due = total - payments;

        if(due - entity.getAmount() <=0 && order.getStatus()!=OrderStatus.Completed) {
            order.setStatus(OrderStatus.Completed);
            order.setPaid(true);
            orderCrud.update(order);
        }
    }
    
    private Order getDetachedOrder(Payment entity) {
        if(entity.getOrderId() == null) return null;
        
        Order order = orderCrud.read(entity.getOrderId());
        //we need to detach it to prevent our changes on the order to persist it self
        em.detach(order);
        return order;
    }

}
