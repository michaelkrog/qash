package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import org.hibernate.annotations.GenericGenerator;

/**
 * Specifies an order.
 */
@Entity
@Table(name="OrderModel")
public class Order implements Serializable, ContentEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateChanged = new Date();

    @ManyToOne
    private Organisation organisation;

    private String currency = "USD";

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateInvoiced = null;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateTimelyPayment = null;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.New;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
    @JoinColumn(name="order_id")
    @Fetch(FetchMode.SUBSELECT)
    private List<OrderLine> orderlines = new ArrayList<OrderLine>();

    private long number = -1;
    private long invoiceNumber = -1;
    
    @Embedded
    private ContactInformation buyer;
    private String buyerId;
    
    @Embedded
    private ContactInformation recipient;
    
    private String clerkUserId;
    private String clerkName;
    
    @ManyToOne
    private Outlet outlet;
    
    private boolean paid;


    public Order() {
    }

     public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date date) {
        this.dateCreated = date;
    }

    public Date getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    /**
     * Retrieves the buyer or null if no buyer has been set no the order.
     */
    public ContactInformation getBuyer() {
        return buyer;
    }

    /**
     * Sets information of the buyer.
     */
    public void setBuyer(ContactInformation buyer) {
        this.buyer = buyer;
    }
    
    /**
     * Sets information of the buyer. The buyer information will be copied to
     * a new ContactInformation object.
     */
    public void setBuyer(HasContactInformation buyer) {
        this.buyer = new ContactInformation(buyer);
    }

    /*
     * The id of the organisation that is registered as buyer.
     */
    public String getBuyerId() {
        return buyerId;
    }

    /*
     * The id of the organisation that is registered as buyer.
     */
    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
    
    public String getClerkUserId() {
        return clerkUserId;
    }

    public void setClerkUserId(String clerkUserId) {
        this.clerkUserId = clerkUserId;
    }

    public String getClerkName() {
        return clerkName;
    }

    public void setClerkName(String clerk) {
        this.clerkName = clerk;
    }
    
    /**
     * Retrieves the recipient or null if no recipient has been set.
     */
    public ContactInformation getRecipient() {
        return recipient;
    }

    public void setRecipient(ContactInformation recipient) {
        this.recipient = recipient;
    }

    @Override
    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    

    public Date getDateInvoiced() {
        return dateInvoiced;
    }

    public void setDateInvoiced(Date dateInvoiced) {
        this.dateInvoiced = dateInvoiced;
    }

    public Date getDateTimelyPayment() {
        return dateTimelyPayment;
    }

    public void setDateTimelyPayment(Date dateTimelyPayment) {
        this.dateTimelyPayment = dateTimelyPayment;
    }

    public long getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getName() {
        return Long.toString(number);
    }

    public String getShortDescription() {
        return status.name();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        if (currency == null) {
            currency = "USD";

        }
        this.currency = currency;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void addOrderLine(Product item) {
        addOrderLine(item, 1);
    }

    /**
     * @deprecated 
     * @param line
     */
    public void addOrderLine(OrderLine line) {
        orderlines.add(line);
    }

    public void addOrderLine(Product item, double quantity) {
        if (item == null) {
            throw new IllegalArgumentException("item was null.");

        }

        //If an item like this is already in the order then just increment the quantity
        OrderLine match = getOrderLine(item);
        if (match != null) {
            match.setQuantity(match.getQuantity() + quantity);
            return;
        }

        addOrderLine(item.getName(), quantity, item.getPrice(), item.getId(), item.getItemNo(), item.getTax());
    }

    public void addOrderLine(String title, double quantity, long price, Tax tax) {
        addOrderLine(title, quantity, price, null, null, tax);
    }

    public void addOrderLine(String title, double quantity, long price, String itemid, String itemno) {
        addOrderLine(title, quantity, price, itemid, itemno, null);
    }

    public void addOrderLine(String title, double quantity, long price, String itemid, String itemno, Tax tax) {

        OrderLineTax lineTax;

        if (tax == null) {
            lineTax = null;
        } else {
            lineTax = new OrderLineTax(tax);
        }

        OrderLine line = new OrderLine();
        line.setTitle(title);
        line.setQuantity(quantity);
        line.setPrice(price);
        line.setItemId(itemid);
        line.setTax(lineTax);
        line.setItemNo(itemno);

        addOrderLine(line);

    }

    public int getOrderLineCount() {
        return orderlines.size();
    }

    public OrderLine removeOrderLine(int index) {
        Iterator<OrderLine> it = orderlines.iterator();

        int count =0;
        while(it.hasNext()) {
            OrderLine current = it.next();
            if(count==index) {
                it.remove();
                return current;
            }
            count++;
        }
        return null;
    }

    public OrderLine getOrderLine(int index) {
        Iterator<OrderLine> it = orderlines.iterator();
        if(index<0 || index>orderlines.size()) {
            return null;
        }

        int count =0;
        while(it.hasNext()) {
            OrderLine current = it.next();
            if(count==index) {
                return current;
            }
            count++;
        }
        return null;
    }

    public int getOrderLineIndex(OrderLine orderline) {
        Iterator<OrderLine> it = orderlines.iterator();

        int count =0;
        while(it.hasNext()) {
            OrderLine current = it.next();
            if(orderline.equals(current)) {
                return count;
            }
            count++;
        }
        return -1;
    }

    public List<OrderLine> getOrderLines() {
        return new ArrayList<OrderLine>(orderlines);
    }

    public OrderLine getOrderLine(Product item) {
        if (item.getId() == null) {
            return null;
        }

        OrderLineTax lineTax;

        if (item.getTax() == null) {
            lineTax = null;
        } else {
            lineTax = new OrderLineTax(item.getTax());
        }

        for (OrderLine line : orderlines) {
            if (item.getId().equals(line.getItemId())
                    && item.getPrice() == line.getPrice()
                    && isSame(lineTax, line.getTax())) {
                return line;
            }

        }
        return null;
    }

    /**
     * @deprecated
     * @param orderlines
     */
    public void setOrderlines(List<OrderLine> orderlines) {
        this.orderlines = new ArrayList<OrderLine>(orderlines);
    }

    public void clear() {
        status = OrderStatus.New;
        orderlines.clear();
        //payments.clear();
    }

    public long getTotal() {
        long total = 0;
        for (int i = 0; i < getOrderLineCount(); i++) {
            OrderLine line = getOrderLine(i);
            total += line.getTotal();
        }
        return total;
    }

    public long getTotalTax() {
        return getTotalTax(null);
    }

    public long getTotalWithTax() {
        return getTotal() + getTotalTax();
    }

    public List<OrderLineTax> getTaxList() {
        List<String> refList = new ArrayList<String>();
        List<OrderLineTax> taxes = new ArrayList<OrderLineTax>();
        for (OrderLine line : orderlines) {
            OrderLineTax tax = line.getTax();
            if (tax != null && tax.getReferenceId() != null
                    && !refList.contains(tax.getReferenceId())) {
                taxes.add(tax);
                refList.add(tax.getReferenceId());
            }
        }
        return taxes;
    }

    public long getTotalTax(OrderLineTax tax) {
        long value = 0;
        for (OrderLine line : orderlines) {
            if (tax == null || hasSameTaxReferenced(tax, line.getTax())) {
                value += line.getTotalTax();
            }
        }
        return value;
    }

    private boolean isSame(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        }

        if (obj1 == null) {
            return false;
        }

        return obj1.equals(obj2);
    }

    private boolean hasSameTaxReferenced(OrderLineTax t1, OrderLineTax t2) {
        if (t1 == null || t2 == null) {
            return false;
        }

        if (t1.getReferenceId() == null || t2.getReferenceId() == null) {
            return false;
        }

        return t1.getReferenceId().equals(t2.getReferenceId());
    }
}
