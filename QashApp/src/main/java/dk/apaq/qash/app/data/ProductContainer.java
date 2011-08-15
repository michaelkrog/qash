package dk.apaq.qash.app.data;

import dk.apaq.crud.Crud;
import dk.apaq.qash.share.model.Item;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import dk.apaq.vaadin.addon.crudcontainer.CrudItem;

/**
 *
 * @author michaelzachariassenkrog
 */
public class ProductContainer extends CrudContainer<String, Item>  {

    public ProductContainer(Crud<String, Item> crud) {
        super(crud, Item.class);
    }

    @Override
    protected CrudItem buildItem(CrudContainer<String, Item> container, Crud<String, Item> crud, String id, Item bean) {
        return new ProductItem(container, crud, bean);
    }


}
