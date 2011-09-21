package dk.apaq.shopsystem.service.crud;

/**
 * A simple interface for handling inventory.
 * @author michaelzachariassenkrog
 */
public interface InventoryManager {

    /**
     * Pulls an item from Stock.
     * @param itemId The itemid of the item.
     * @param quantity The quantity to pull.
     */
    void pullFromStock(String itemId, double quantity);

    /**
     * Checks wether the stock/inventory manages an item with the given itemid.
     */
    boolean isStockItem(String itemId);
}
