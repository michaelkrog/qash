package dk.apaq.shopsystem.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author michael
 */
@Entity
public class ProductDiscount extends AbstractDiscount {

    
    @OneToMany
    private List<Product> products = new ArrayList<Product>();

    public List<Product> getProducts() {
        return products;
    }

    
    
}
