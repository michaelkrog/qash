package dk.apaq.shopsystem.service.crud;

import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderLine;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Sequence;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.ServiceException;
import java.util.Date;
import javax.persistence.EntityManager;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author michaelzachariassenkrog
 */
public class OrderCrudImpl extends ContentCrud<Order> {

    private final long DAYINMILLIS = 86400000L;
    private final InventoryManager inventoryManager;
    private final EntityManager em;
    private final OrganisationService organisationService;

    public OrderCrudImpl(EntityManager em, OrganisationService organisationService, Organisation organisation, InventoryManager inventoryManager) {
        super(em, organisation, Order.class);
        this.em = em;
        this.inventoryManager = inventoryManager;
        this.organisationService = organisationService;
    }

    @Override
    @Transactional
    public String create() {
        return create(new Order());
    }

    @Override
    public <E extends Order> String create(E order) {
        order.setNumber(organisationService.getOrderSequence().increment());
        order.setCurrency(organisation.getCurrency());
        return super.create(order);
    }

    
    @Override
    public Order read(String id) {
        Order order = super.read(id);
        em.detach(order);
        return order;
    }

    @Override
    @Transactional(isolation= Isolation.REPEATABLE_READ)
    public void update(Order entity) {
        fireOnBeforeUpdate(entity.getId(), entity);
        if (entity.getStatus() == OrderStatus.New) {
            entity.setStatus(OrderStatus.Processing);
        }

        Order existingOrder = read(entity.getId());

        if (existingOrder.getStatus() == OrderStatus.Completed) {
            throw new ServiceException("The order has already been marked as completed. Cannot persist it anymore.");
        }

        if (!entity.getOrganisation().getId().equals(organisation.getId())) {
            throw new SecurityException("Not allowed to change the owner of an item.");
        }
        checkStatusChange(existingOrder, entity);

        boolean changeToAccepted = (existingOrder.getStatus().ordinal() < OrderStatus.Accepted.ordinal())
                && (entity.getStatus().ordinal() >= OrderStatus.Accepted.ordinal());

        if (changeToAccepted) {
            for (int i = 0; i < entity.getOrderLineCount(); i++) {
                OrderLine line = entity.getOrderLine(i);
                String itemid = line.getItemId();
                if (itemid != null) {
                    if (inventoryManager.isStockItem(itemid)) {
                        inventoryManager.pullFromStock(itemid, line.getQuantity());
                    }
                }
            }

            long invoicenumber = organisationService.getInvoiceSequence().increment();
         
            Date invoiceDate = new Date();
            Date timelyPayment = new Date(invoiceDate.getTime() + (DAYINMILLIS * organisation.getDefaultPaymentPeriodInDays()));

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

        if (newStatus == OrderStatus.New) {
            throw new ServiceException("An order cannot be persisted as new. Persist as processing instead.");
        }

        if (currentStatus.isConfirmedState() && !newStatus.isConfirmedState()) {
            throw new ServiceException("Existing order is confirmed and can not be changed to a non confirmed state.");
        }
    }

    private static long getNextSequence(EntityManager em, String id, long initialSequence) {

        int maxRetries = 5;

        Sequence sequence;
        long nextSequence = -1;

        for (int i = 0; i < maxRetries; i++) {
            try {

                sequence = em.find(Sequence.class, id);
                if (sequence == null) {
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
