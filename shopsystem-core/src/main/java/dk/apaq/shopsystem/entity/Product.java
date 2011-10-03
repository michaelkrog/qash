package dk.apaq.shopsystem.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import dk.apaq.shopsystem.util.TaxTool;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 * Specifies a product.
 */
@Entity
public class Product implements Serializable, ContentEntity {

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

    private String name;
    private String itemNo;
    private String barcode;
    private double price = 0;
    private double discountprice = 0;
    private double quantityInStock = 0;
    private boolean stockProduct = false;
    @ManyToOne
    private Category category;
    @ManyToOne
    private Tax tax = null;

    public Product() {
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

    @Override
    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }
    
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public double getDiscountPrice() {
        return discountprice;
    }

    public void setDiscountPrice(double discountprice) {
        this.discountprice = discountprice;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public double getPrice() {
        return price;
    }

    /**
     * Retrieves the price including tax value. This is the same as calling
     * getPrice() + getTaxValue();
     * @return
     */
    public double getPriceWithTax() {
        return getPrice() + getTaxValue();
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets the price of item including tax. This will use taxes to calculate how much
     * of the price is tax and then call setPrice() to adjust the price. This means that
     * changing the tax after calling this method with also change the retrieved value with
     * getPriceWithTax.
     */
    public void setPriceWithTax(double price) {
        if (tax != null) {
            price = ( ( price / ( tax.getRate() + 100.0 ) ) * 100.0 );
        }

        setPrice(price);
    }

    public double getTaxValue() {
        return TaxTool.getAddableTaxValue(getPrice(), getTax());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return itemNo;
    }

    public double getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(double quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public boolean isStockProduct() {
        return stockProduct;
    }

    public void setStockProduct(boolean stockProduct) {
        this.stockProduct = stockProduct;
    }

    public String getType() {
        return "Item";
    }
}
