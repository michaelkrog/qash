/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.service.crud;

/**
 *
 * @author michaelzachariassenkrog
 */
public interface InventoryManager {

    void pullFromStock(String itemId, double quantity);
    boolean isStockItem(String itemId);
}
