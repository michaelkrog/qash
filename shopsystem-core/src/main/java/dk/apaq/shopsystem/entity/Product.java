package dk.apaq.shopsystem.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import dk.apaq.shopsystem.util.TaxTool;
import java.util.Date;
import java.util.List;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 * Specifies a product.
 */
@Entity
public class Product implements Serializable, ContentEntity, HasEnable {

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
    private long price = 0;
    private long discountprice = 0;
    private double quantityInStock = 0;
    private boolean stockProduct = false;
    private boolean enabled = true;
    
    @ManyToOne(fetch= FetchType.EAGER)
    private ProductGroup productGroup;
    
    @ManyToMany(fetch= FetchType.EAGER)
    private List<ProductCategory> productCategories;
    
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

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
    
    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setproductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public List getproductCategories() {
        return productCategories;
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

    public long getDiscountPrice() {
        return discountprice;
    }

    public void setDiscountPrice(long discountprice) {
        this.discountprice = discountprice;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    /**
     * Price i the lowest denomination of a currency.
     * @return 
     */
    public long getPrice() {
        return price;
    }

    /**
     * Retrieves the price including tax value. This is the same as calling
     * getPrice() + getTaxValue();
     * @return
     */
    public long getPriceWithTax() {
        return getPrice() + getTaxValue();
    }

    public void setPrice(long price) {
        this.price = price;
    }

    /**
     * Sets the price of item including tax. This will use taxes to calculate how much
     * of the price is tax and then call setPrice() to adjust the price. This means that
     * changing the tax after calling this method with also change the retrieved value with
     * getPriceWithTax.
     */
    public void setPriceWithTax(long price) {
        if (tax != null) {
            price = (long)( ( price / ( tax.getRate() + 100.0 ) ) * 100.0 );
        }

        setPrice(price);
    }

    public long getTaxValue() {
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
