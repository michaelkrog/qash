package dk.apaq.shopsystem.service;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.BaseUser;
import dk.apaq.shopsystem.entity.Category;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.Product;
import dk.apaq.shopsystem.entity.Store;
import dk.apaq.shopsystem.entity.Tax;
import dk.apaq.shopsystem.entity.User;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.crud.UserCrud;
import org.apache.commons.vfs2.FileSystem;

/**
 *
 * @author krog
 */
public interface OrganisationService {

    public Organisation readOrganisation();
    public void updateOrganisation(Organisation organisation);

    UserCrud getUsers();

    Crud.Editable<String, Store> getStores();
    Crud.Complete<String, Category> getCategories();
    Crud.Complete<String, Website> getWebsites();
    Crud.Complete<String, Order> getOrders();
    Crud.Complete<String, Product> getProducts();
    Crud.Editable<String, Tax> getTaxes();
    Crud.Complete<String, Payment> getPayments();

    FileSystem getFileSystem();
}
