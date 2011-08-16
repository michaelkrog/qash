package dk.apaq.shopsystem.service;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.model.Category;
import dk.apaq.shopsystem.model.SystemUser;
import dk.apaq.shopsystem.model.Product;
import dk.apaq.shopsystem.model.Order;
import dk.apaq.shopsystem.model.Organisation;
import dk.apaq.shopsystem.model.Payment;
import dk.apaq.shopsystem.model.Store;
import dk.apaq.shopsystem.model.Tax;
import dk.apaq.shopsystem.model.Website;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;

/**
 *
 * @author michaelzachariassenkrog
 */
public interface Service {

    OrganisationCrud getOrganisationCrud();
    Crud.Complete<String, SystemUser> getSystemUserCrud();

    Crud.Editable<String, Store> getStoreCrud(Organisation organisation);
    Crud.Complete<String, Category> getCategoryCrud(Organisation organisation);
    Crud.Complete<String, Website> getWebsiteCrud(Organisation organisation);
    Crud.Complete<String, Order> getOrderCrud(Organisation organisation);
    Crud.Complete<String, Product> getProductCrud(Organisation organisation);
    Crud.Editable<String, Tax> getTaxCrud(Organisation organisation);
    Crud.Complete<String, Payment> getPaymentCrud(Organisation organisation);


}
