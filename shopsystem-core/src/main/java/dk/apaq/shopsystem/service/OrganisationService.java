package dk.apaq.shopsystem.service;

import dk.apaq.shopsystem.service.sequence.SequenceProcessor;
import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.Document;
import dk.apaq.shopsystem.entity.DocumentCollection;
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
import dk.apaq.shopsystem.entity.CustomerRelationship;
import dk.apaq.shopsystem.entity.Subscription;
import dk.apaq.shopsystem.entity.Theme;
import dk.apaq.vfs.FileSystem;
import java.io.Serializable;

public interface OrganisationService extends Serializable {

    public Organisation readOrganisation();
    public void updateOrganisation(Organisation organisation);

    /** CRUDS **/
    Crud.Complete<String, OrganisationUserReference> getUsers();
    Crud.Editable<String, Store> getStores();
    Crud.Complete<String, CustomerRelationship> getCustomers();
    Crud.Complete<String, ProductGroup> getProductGroups();
    Crud.Complete<String, ProductCategory> getProductCategories();
    Crud.Complete<String, Order> getOrders();
    Crud.Complete<String, Product> getProducts();
    Crud.Editable<String, Tax> getTaxes();
    Crud.Complete<String, Payment> getPayments();
    Crud.Complete<String, Domain> getDomains();
    Crud.Complete<String, Document> getDocuments();
    Crud.Complete<String, DocumentCollection> getDocumentCollections();
    Crud.Complete<String, Subscription> getSubscriptions();
    Crud.Complete<String, Website> getWebsites();
    Crud<String,Theme> getThemes();
    Crud.Complete<String,WebPage> getPages(Website website);

    FileSystem getFileSystem();

    /** SEQUENCES **/
    SequenceProcessor getOrderSequence();
    SequenceProcessor getInvoiceSequence();
    
    CustomerRelationship getCustomerRelationship(Organisation org, boolean createIfNotFound);
    
    /**
     * Returns the organisations default tax or null if it has no default tax.
     */
    Tax getDefaultTax();
}
