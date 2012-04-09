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
public class Product extends AbstractCommodity implements Serializable {

    private long discountprice = 0;
    private double quantityInStock = 0;
    private boolean stockProduct = false;
    
    @ManyToOne(fetch= FetchType.EAGER)
    private ProductGroup productGroup;
    
    @ManyToMany(fetch= FetchType.EAGER)
    private List<ProductCategory> productCategories;
    


    public Product() {
    }
    
    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public List getProductCategories() {
        return productCategories;
    }

    public long getDiscountPrice() {
        return discountprice;
    }

    public void setDiscountPrice(long discountprice) {
        this.discountprice = discountprice;
    }

    public String getShortDescription() {
        return getItemNo();
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

    @Override
    public CommodityType getCommodityType() {
        return CommodityType.Product;
    }

    

}
