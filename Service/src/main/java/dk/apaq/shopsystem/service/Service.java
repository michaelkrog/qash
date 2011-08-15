/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.service;

import dk.apaq.shopsystem.service.crud.ShopCrud;
import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.model.Account;
import dk.apaq.shopsystem.model.Product;
import dk.apaq.shopsystem.model.Order;
import dk.apaq.shopsystem.model.Organisation;
import dk.apaq.shopsystem.model.Payment;
import dk.apaq.shopsystem.model.Store;
import dk.apaq.shopsystem.model.Tax;
import java.util.concurrent.Future;

/**
 *
 * @author michaelzachariassenkrog
 */
public interface Service {

    ShopCrud getShopCrud();
    Crud.Complete<String, Account> getAccountCrud();

    Crud.Complete<String, Order> getOrderCrud(Organisation organisation);
    Crud.Complete<String, Product> getItemCrud(Organisation organisation);
    Crud.Editable<String, Tax> getTaxCrud(Organisation organisation);
    Crud.Complete<String, Payment> getPaymentCrud(Organisation organisation);


}
