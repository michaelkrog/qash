package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.model.Order;
import dk.apaq.shopsystem.model.OrderStatus;
import dk.apaq.shopsystem.model.Organisation;
import dk.apaq.shopsystem.model.Payment;
import dk.apaq.shopsystem.model.PaymentType;
import dk.apaq.shopsystem.model.Store;
import java.util.Date;
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
        super(em, organisation, new GenericContentCrudAssist<Payment>(organisation, Payment.class));
        this.em = em;
        
        if(orderCrud == null) {
            throw new NullPointerException("orderCrud was null.");
        }
        this.orderCrud = orderCrud;
    }

    @Override
    @Transactional
    public void update(Payment entity) {
        Order order = orderCrud.read(entity.getOrderId());
        //we need to detach it to prevent our changes on the order to persist it self
        em.detach(order);

        update(entity, order);
        
    }

    public void update(Payment entity, Order order) {
        LOG.debug("Updating payment [id={}]", entity.getId());

        Payment existingPayment = read(entity.getId());
        boolean firstPersist = existingPayment.getOrderId() == null;
        if(!firstPersist) {
            throw new IllegalStateException("Persisting a payment twice is not allowed.");
        }

        if(entity.getOrderId()==null) {
            throw new NullPointerException("Payment has no order specified, which is required.");
        }

        //Get total from a fresh order
        double total = order.getTotalWithTax();

        if(!order.getStatus().isConfirmedState()) {
            throw new IllegalStateException("Payments only possible for accepted orders.");
        }

        if(entity.getPaymentType() == PaymentType.Change && order.getStatus() != OrderStatus.Completed) {
            throw new IllegalStateException("Change can only be registered for an order when it is complete.");
        }

        if(entity.getPaymentType() != PaymentType.Change && order.getStatus() == OrderStatus.Completed) {
            throw new IllegalStateException("Ordinary payments can only be registered for an order when it is NOT complete.");
        }

        //Calculate due
        double payments = getTotalPayment(order.getId());
        double due = total - payments;

        if(due - entity.getAmount() <=0 && order.getStatus()!=OrderStatus.Completed) {
            order.setStatus(OrderStatus.Completed);
            orderCrud.update(order);
        }

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
    

}
