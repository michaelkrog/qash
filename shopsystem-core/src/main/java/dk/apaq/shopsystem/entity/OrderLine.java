package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;


/**
 * A line in an order. This is not the same as an item. A line may or may not reference the item being sold.
 * If no Item is referenced then the orderline is not a sale of something in stock or may not even be a 
 * physical item.
 *
 */
@Entity
public class OrderLine implements Serializable, BasicEntity {

    private static final int PERCENTAGEDIVIDE = 100;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateChanged = new Date();

    private String title;
    private String itemId;
    private String itemNo;
    private double quantity;
    private BigDecimal price = BigDecimal.ZERO;
    private CommodityType commodityType = CommodityType.Unknown;

    /*@Column(name="UNITTYPE")
    @Enumerated(EnumType.STRING)
    private UnitType unittype = UnitType.Each;*/

    @OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    private OrderLineTax tax = null;

    private double discountPercentage = 0;

    /**
     * Retrieves the tax value for one item described by this orderline. The
     * tax is NOT a tax rate. It is the actual value of the tax calculated using
     * the taxes added to the orderline..
     * @return The tax value.
     */
    public BigDecimal getTaxValue() {
        
        if (tax != null) {
            return calculateTaxValue(price);
        } else {
            return BigDecimal.ZERO;
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public void setDateCreated(Date date) {
        this.dateCreated = date;
    }

    @Override
    public Date getDateChanged() {
        return dateChanged;
    }

    @Override
    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    public OrderLineTax getTax() {
        return tax;
    }

    public void setTax(OrderLineTax tax) {
        this.tax = tax;
    }

    public void setTax(Tax tax) {
        if (tax == null) {
            setTax((OrderLineTax) null);
        } else {
            setTax(new OrderLineTax(tax));
        }
    }

    /**
     * Retrieves the id of the item.
     * @return The id of the item in stock control or null if not refereing to an item from stock.
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Sets the id of the item.
     * @param itemId The id of an item or null.
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * Retrieves the itemno of the item. This is not a unique identifier for an item as
     * one or more item may share the same itemno(although its not typical). Use itemid for
     * a unique reference.
     * @return The itemno original came from an item from stock.
     */
    public String getItemNo() {
        return itemNo;
    }

    /**
     * Sets the itemno of an item.
     * @param itemId The itemno of an item or null.
     */
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    /**
     * Retrieves the price of the item. This price is tax-excluded.
     * @return The price, tax-excluded.
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets the price of item, tax-excluded, and in the smallest denomination of a currency.
     * @param price Price of the item, tax-excluded.
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Retrieves the price including tax value. This is the same as calling
     * getPrice() + getTaxValue();
     * @return
     */
    public BigDecimal getPriceWithTax() {
        return getPrice().add(getTaxValue());
    }

    /*
     *
     * Retrieves discount. 0.0 is 0% and 1.0 is 100%.
     */
    public double getDiscountPercentage() {
        return discountPercentage;
    }

    /**
     * Sets discount where 0.0 is 0% and 1.0 is 100%
     * @param discountPercentage
     */
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    /**
     * Gets the quantity for this orderline.
     * @return The quantity.
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity for this orderline.
     * @param quantity The quantity for this orderline.
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     * Retrieves the title of this orderline.
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this orderline.
     * @param title The title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public CommodityType getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(CommodityType commodityType) {
        this.commodityType = commodityType;
    }
    
    
    public BigDecimal getTotal() {
        return getTotal(true);
    }

    /**
     * Calculates the total(tax-excluded) for this orderline.
     *
     * If discount is included it uses this calculation:
     * price*quantity-discount.
     *
     * If discount is not included it uses this calculation
     * price*quantity.
     *
     * @return The total for this order.
     */
    public BigDecimal getTotal(boolean includeDiscount) {
        BigDecimal total = getPrice().multiply(BigDecimal.valueOf(getQuantity()));
        if(includeDiscount) {
            BigDecimal discount = total.multiply(BigDecimal.valueOf(discountPercentage));
            total = total.add(discount.negate());
        }
        return total.setScale(2);
    }

    
    public BigDecimal getTotalWithTax() {
        return getTotalWithTax(true);
    }
     
    public BigDecimal getTotalWithTax(boolean includeDiscount) {
        return getTotal(includeDiscount).add(getTotalTax(includeDiscount)).setScale(2);
    }


    public BigDecimal getTotalTax() {
        return getTotalTax(true);
    }
    /**
     * Calculates the total tax for this orderline by this equation: tax*quantity.
     * @return The total tax for this order.
     */
    public BigDecimal getTotalTax(boolean includeDiscount) {
        return calculateTaxValue(getTotal(includeDiscount)).setScale(2);
    }

    /*
    public UnitType getUnittype() {
        return unittype;
    }

    public void setUnittype(UnitType unittype) {
        this.unittype = unittype;
    }*/

    private BigDecimal calculateTaxValue(BigDecimal value) {
        
        if (tax != null) {
            return value.multiply(BigDecimal.valueOf(tax.getRate() / PERCENTAGEDIVIDE)).setScale(2);
        } else {
            return BigDecimal.ZERO;
        }

    }
    
}
