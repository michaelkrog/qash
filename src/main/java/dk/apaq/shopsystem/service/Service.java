package dk.apaq.shopsystem.service;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.Category;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.entity.Product;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.Store;
import dk.apaq.shopsystem.entity.Tax;
import dk.apaq.shopsystem.entity.Website;
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
