package dk.apaq.shopsystem.service;

import dk.apaq.shopsystem.service.sequence.SequenceProcessor;
import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.Customer;
import dk.apaq.shopsystem.entity.Document;
import dk.apaq.shopsystem.entity.ProductGroup;
import dk.apaq.shopsystem.entity.Domain;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.OrganisationUserReference;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.Product;
import dk.apaq.shopsystem.entity.Store;
import dk.apaq.shopsystem.entity.Tax;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.entity.WebPage;
import dk.apaq.shopsystem.entity.ProductCategory;
import dk.apaq.shopsystem.entity.Theme;
import dk.apaq.vfs.FileSystem;
import java.io.Serializable;

/**
 *
 * @author krog
 */
public interface OrganisationService extends Serializable {

    public Organisation readOrganisation();
    public void updateOrganisation(Organisation organisation);

    Crud.Complete<String, OrganisationUserReference> getUsers();

    Crud.Editable<String, Store> getStores();
    Crud.Complete<String, Customer> getCustomers();
    Crud.Complete<String, ProductGroup> getProductGroups();
    Crud.Complete<String, ProductCategory> getProductCategories();
    Crud.Complete<String, Order> getOrders();
    Crud.Complete<String, Product> getProducts();
    Crud.Editable<String, Tax> getTaxes();
    Crud.Complete<String, Payment> getPayments();

    Crud.Complete<String, Domain> getDomains();
    Crud.Complete<String, Document> getDocuments();
    Crud.Complete<String, Website> getWebsites();
    Crud<String,Theme> getThemes();
    Crud.Complete<String,WebPage> getPages(Website website);

    FileSystem getFileSystem();
    
    SequenceProcessor getOrderSequence();
    SequenceProcessor getInvoiceSequence();
    
    /**
     * Returns the organisations default tax or null if it has no default tax.
     */
    Tax getDefaultTax();
}
