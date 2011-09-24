package dk.apaq.shopsystem.ui.qash.data;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.Product;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import dk.apaq.vaadin.addon.crudcontainer.CrudItem;

/**
 *
 * @author michaelzachariassenkrog
 */
public class ProductContainer extends CrudContainer<String, Product>  {

    public ProductContainer(Crud<String, Product> crud) {
        super(crud, Product.class);
    }

    @Override
    protected CrudItem buildItem(CrudContainer<String, Product> container, Crud<String, Product> crud, String id, Product bean) {
        return new ProductItem(container, crud, bean);
    }


}
