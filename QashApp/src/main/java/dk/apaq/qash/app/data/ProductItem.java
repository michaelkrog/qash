package dk.apaq.qash.app.data;

import com.vaadin.data.Property;
import dk.apaq.crud.Crud;
import dk.apaq.qash.app.data.util.PropertyDependencyManager;
import dk.apaq.qash.share.model.Item;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import dk.apaq.vaadin.addon.crudcontainer.CrudItem;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author michaelzachariassenkrog
 */
public class ProductItem extends CrudItem<String, Item> {

    private final static Map<String, String[]> dependencyMap = new HashMap<String, String[]>();
    private static final String[] DEPENDENCIES_PRICE = {"priceWithTax"};
    private static final String[] DEPENDENCIES_PRICEWITHTAX = {"price", "tax"};
    private static final String[] DEPENDENCIES_TAXVALUE = {"price", "priceWithTax", "tax"};
    private final PropertyDependencyManager dependencyManager = new PropertyDependencyManager(this);

    static {

        dependencyMap.put("price", new String[]{"priceWithTax"});
        dependencyMap.put("priceWithTax", new String[]{"price", "tax"});
        dependencyMap.put("taxValue", new String[]{"price", "priceWithTax", "tax"});

    }

    
    public ProductItem(CrudContainer<String, Item> container, Crud<String, Item> crud, Item bean) {
        super(container, crud, bean.getId(), bean);

        dependencyManager.addDependency("price", DEPENDENCIES_PRICE);
        dependencyManager.addDependency("priceWithTax", DEPENDENCIES_PRICEWITHTAX);
        dependencyManager.addDependency("taxValue", DEPENDENCIES_TAXVALUE);

    }

    @Override
    public Property getItemProperty(Object id) {
        if(dependencyManager.isManagerFor(id)) {
            return dependencyManager.getDependentProperty(id);
        } else {
            return super.getItemProperty(id);
        }
        
    }

}
