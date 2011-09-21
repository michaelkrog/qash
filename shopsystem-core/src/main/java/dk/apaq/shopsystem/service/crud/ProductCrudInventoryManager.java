package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.crud.Crud.Editable;
import dk.apaq.shopsystem.entity.Product;

/**
 *
 * @author michaelzachariassenkrog
 */
public class ProductCrudInventoryManager implements InventoryManager {

    private final Crud.Editable<String, Product> crud;

    public ProductCrudInventoryManager(Editable<String, Product> crud) {
        this.crud = crud;
    }

    public void pullFromStock(String itemId, double quantity) {
        Product item = crud.read(itemId);
        item.setQuantityInStock(item.getQuantityInStock() - quantity);
    }

    public boolean isStockItem(String itemId) {
        Product item = crud.read(itemId);
        return item.isStockProduct();
    }


}
