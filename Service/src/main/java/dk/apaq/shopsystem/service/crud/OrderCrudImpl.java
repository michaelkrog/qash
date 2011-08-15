package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.service.ServiceException;
import dk.apaq.shopsystem.model.Order;
import dk.apaq.shopsystem.model.OrderLine;
import dk.apaq.shopsystem.model.OrderStatus;
import dk.apaq.shopsystem.model.Organisation;
import dk.apaq.shopsystem.model.Payment;
import dk.apaq.shopsystem.model.Sequence;
import dk.apaq.shopsystem.model.Store;
import java.util.Date;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author michaelzachariassenkrog
 */
public class OrderCrudImpl extends AbstractContentCrud<String, Order> implements Crud.Complete<String, Order> {

    private final long DAYINMILLIS = 86400000L;
    private final InventoryManager inventoryManager;

    public OrderCrudImpl(Organisation organisation, InventoryManager inventoryManager) {
        super(organisation, Order.class);
        this.inventoryManager = inventoryManager;
    }

    @Override
    protected Order createInstance() {
        Order order = new Order();
        order.setOrganisation(organisation);
        order.setStatus(OrderStatus.Processing);
        order.setNumber(getNextSequence(organisation.getId() + "_OrderSequence", organisation.getInitialOrdernumber()));
        order.setCurrency(organisation.getCurrency());
        order.setDateCreated(new Date());
        return order;
    }

    @Override
    public Order read(String id) {
        Order order = super.read(id);
        return order;
    }

    @Override
    @Transactional
    public void update(Order entity) {
        fireOnBeforeUpdate(entity.getId(), entity);
        if(entity.getStatus() == OrderStatus.New) {
            entity.setStatus(OrderStatus.Processing);
        }

        Order existingOrder = read(entity.getId());
        
        if(existingOrder.getStatus() == OrderStatus.Completed) {
            throw new ServiceException("The order has already been marked as completed. Cannot persist it anymore.");
        }

        if (!entity.getOrganisation().getId().equals(organisation.getId())) {
            throw new SecurityException("Not allowed to change the owner of an item.");
        }
        checkStatusChange(existingOrder, entity);

        boolean pickFromStock = (existingOrder.getStatus().ordinal() < OrderStatus.Accepted.ordinal()) &&
                                (entity.getStatus().ordinal() >= OrderStatus.Accepted.ordinal());

        if(pickFromStock) {
            for (int i = 0; i < entity.getOrderLineCount(); i++) {
            OrderLine line = entity.getOrderLine(i);
            String itemid = line.getItemId();
            if (itemid != null) {
                if(inventoryManager.isStockItem(itemid)) {
                    inventoryManager.pullFromStock(itemid, line.getQuantity());
                }
            }
        }

            long invoicenumber =
                    getNextSequence(organisation.getId() + "_InvoiceSequence", organisation.getInitialInvoiceNumber());
            Date invoiceDate = new Date();
            Date timelyPayment = new Date(invoiceDate.getTime() + (DAYINMILLIS * organisation.getPaymentPeriodInDays()));

                entity.setInvoiceNumber(invoicenumber);
            entity.setDateInvoiced(invoiceDate);
            entity.setDateTimelyPayment(timelyPayment);
        }
        em.merge(entity);
        em.flush();
        fireOnUpdate(entity.getId(), entity);
    }


    private void checkStatusChange(Order existingOrder, Order newOrder) {
        OrderStatus currentStatus = existingOrder.getStatus();
        OrderStatus newStatus = newOrder.getStatus();

        if(newStatus == OrderStatus.New) {
            throw new ServiceException("An order cannot be persisted as new. Persist as processing instead.");
        }

        if(currentStatus.isConfirmedState() && !newStatus.isConfirmedState()) {
            throw new ServiceException("Existing order is confirmed and can not be changed to a non confirmed state.");
        }
    }

    @Transactional
    private long getNextSequence(String id, long initialSequence) {

        int maxRetries = 5;

        Sequence sequence;
        long nextSequence = -1;

        for (int i = 0; i < maxRetries; i++) {
            try {

                sequence = em.find(Sequence.class, id);
                if(sequence == null) {
                    if (initialSequence < 1) {
                        initialSequence = 1;
                    }

                    sequence = new Sequence();
                    sequence.setId(id);
                    sequence.setSequence(initialSequence);
                } else {
                        sequence.incrementSequence();
                }

                nextSequence = sequence.getSequence();

                em.persist(sequence);
                em.flush();

                break;
            } catch (Exception ex) {
                if (i == (maxRetries - 1)) {
                    throw new ServiceException("Unable to get next sequence.", ex);
                }
            }
        }

        return nextSequence;

    }
}
