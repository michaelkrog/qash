package dk.apaq.shopsystem.service;

import dk.apaq.shopsystem.service.sequence.SequenceProcessor;
import dk.apaq.crud.Crud;
import dk.apaq.crud.Crud.Complete;
import dk.apaq.crud.Crud.Editable;
import dk.apaq.crud.CrudListener;
import dk.apaq.crud.CrudNotifier;
import dk.apaq.shopsystem.entity.Customer;
import dk.apaq.shopsystem.entity.Document;
import dk.apaq.shopsystem.entity.ProductGroup;
import dk.apaq.shopsystem.entity.Domain;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.WebPage;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.Product;
import dk.apaq.shopsystem.entity.Store;
import dk.apaq.shopsystem.entity.Tax;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.entity.OrganisationUserReference;
import dk.apaq.shopsystem.entity.ProductCategory;
import dk.apaq.shopsystem.entity.Theme;
import dk.apaq.shopsystem.service.crud.InventoryManager;
import dk.apaq.shopsystem.service.crud.SecurityHandler;
import dk.apaq.shopsystem.service.crud.ThemeCrud;
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.FileSystem;
import dk.apaq.vfs.Path;
import dk.apaq.vfs.impl.layered.LayeredFileSystem;
import dk.apaq.vfs.impl.subfs.SubFs;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.WeakHashMap;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
public class OrganisationServiceImpl implements OrganisationService, ApplicationContextAware{

    private static final Logger LOG = LoggerFactory.getLogger(OrganisationServiceImpl.class);

    @PersistenceContext
    private EntityManager em;
    
    @Autowired
    private SystemService service;

    private final Map<Class, Complete<String, Order>> crudMap = new WeakHashMap<Class, Complete<String, Order>>();
    private final Map<String, Map<Class, Complete>> webcontentCrudMap = new WeakHashMap<String, Map<Class, Complete>>();
    private ApplicationContext context;
    private FileSystem fs;
    private Organisation organisation;
    private SequenceProcessor orderSequence = null;
    private SequenceProcessor invoiceSequence = null;
    private SecurityHandler.OrganisationUserReferenceSecurity userReferenceSecurity = new SecurityHandler.OrganisationUserReferenceSecurity();
    

    public OrganisationServiceImpl(Organisation org) {
        this.organisation = org;
    }


    @Override
    public Complete<String, Order> getOrders() {
        Organisation organisation = readOrganisation();
        Complete crud = crudMap.get(Order.class);
        if(crud==null) {
            InventoryManager im = (InventoryManager) context.getBean("inventoryManager", getProducts());
            crud = (Crud.Complete<String, Payment>) context.getBean("orderCrud", em, this, organisation, im);
            ((CrudNotifier)crud).addListener(new SecurityHandler.ContentSecurity(organisation));
            crudMap.put(Order.class, crud);
        }
        return crud;
    }

    @Override
    public Complete<String, Product> getProducts() {
        return getGenericContentCrud(Product.class);
    }
    
    @Override
    public Complete<String, Document> getDocuments() {
        return getGenericContentCrud(Document.class);
    }

    @Override
    public Editable<String, Tax> getTaxes() {
        return getGenericContentCrud(Tax.class);
    }

    @Override
    public Complete<String, Payment> getPayments() {
        Organisation organisation = readOrganisation();
        Complete crud = crudMap.get(Payment.class);
        if(crud==null) {
            crud = (Crud.Complete<String, Payment>) context.getBean("paymentCrud", em, organisation, getOrders());
            ((CrudNotifier)crud).addListener(new SecurityHandler.ContentSecurity(organisation));
            crudMap.put(Payment.class, crud);
        }
        return crud;
    }

    @Override
    public Complete<String, Customer> getCustomers() {
        return getGenericContentCrud(Customer.class);
    }
    
    @Override
    public Complete<String, ProductGroup> getProductGroups() {
        return getGenericContentCrud(ProductGroup.class);
    }
    
    @Override
    public Complete<String, ProductCategory> getProductCategories() {
        return getGenericContentCrud(ProductCategory.class);
    }

    @Override
    public Editable<String, Store> getStores() {
        return getGenericContentCrud(Store.class);
    }

    @Override
    public Complete<String, Website> getWebsites() {
         return getGenericContentCrud(Website.class);
    }

    @Override
    public Complete<String, Domain> getDomains() {
        return getGenericContentCrud(Domain.class);
    }

    @Override
    public Complete<String, WebPage> getPages(Website website) {
        return getWebContentCrud(website, WebPage.class);
    }

    

    private <T> Complete<String, T> getGenericContentCrud(Class<T> clazz, CrudListener ... listeners) {
        Organisation organisation = readOrganisation();
        Complete crud = crudMap.get(clazz);
        if(crud==null) {
            crud = (Crud.Complete<String, T>) context.getBean("contentCrud", em, organisation, clazz);
            ((CrudNotifier)crud).addListener(new SecurityHandler.ContentSecurity(organisation));
            
            for(CrudListener listener : listeners) {
                ((CrudNotifier)crud).addListener(listener);
            }
            crudMap.put(clazz, crud);
        }
        return crud;
    }
    
    private <T> Complete<String, T> getWebContentCrud(Website website, Class<T> clazz) {
        if(website == null) {
            throw new NullPointerException("Website is null.");
        }
        Map<Class, Complete> crudMap = webcontentCrudMap.get(website.getId());
        if(crudMap==null) {
            crudMap = new WeakHashMap<Class, Complete>();
            webcontentCrudMap.put(website.getId(), crudMap);
        }
        Complete crud = crudMap.get(clazz);
        if(crud==null) {
            crud = (Crud.Complete<String, T>) context.getBean("webContentCrud", em, website, clazz);
            //((CrudNotifier)crud).addListener(new SecurityHandler.ContentSecurity(organisation));
            crudMap.put(clazz, crud);
        }
        return crud;
    }

    @Override
    public Organisation readOrganisation() {
        return organisation;
    }

    @Override
    @Transactional
    public void updateOrganisation(Organisation org) {
        this.service.getOrganisationCrud().update(org);
        this.organisation = org;
    }

    @Override
    public Crud.Complete<String, OrganisationUserReference> getUsers() {
        return getGenericContentCrud(OrganisationUserReference.class, userReferenceSecurity);
    }

    @Override
    public Crud<String, Theme> getThemes() {
        try {
            return new ThemeCrud((Directory)service.getFileSystem().getNode("/System/Themes/Standard"),
                                                (Directory)getFileSystem().getNode("/Themes"));
        } catch (FileNotFoundException ex) {
            LOG.error("Unable to create themecrud.", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public FileSystem getFileSystem() {
        if(fs==null) {
            try {
                //Must create a filesystem for this organisation
                String orgId = organisation.getId();
                FileSystem systemFs = service.getFileSystem();
                Directory orgsDir = systemFs.getRoot().getDirectory("Organisations");
                if(!orgsDir.hasDirectory(orgId)) orgsDir.createDirectory(orgId);

                Directory orgDir = orgsDir.getDirectory(orgId);
                fs = new SubFs(systemFs, orgDir);
                fs = new LayeredFileSystem(fs);
                
                //Inject content of /System/Themes/Standard at /Themes
                Path injectedThemesPath = new Path("/Themes");
                Directory injectedThemesDirectory = systemFs.getRoot().
                                                        getDirectory("System", true).
                                                        getDirectory("Themes",true).
                                                        getDirectory("Standard", true);
                ((LayeredFileSystem)fs).addLayer(injectedThemesPath, injectedThemesDirectory);
                
                
                Directory root = fs.getRoot();

                if(!root.hasDirectory("Content")) root.createDirectory("Content");
                if(!root.hasDirectory("Themes")) root.createDirectory("Themes");
                
            } catch (IOException ex) {
                LOG.error("Unable to resolve filesystem for organisation.", ex);
                throw new RuntimeException(ex);
            }
        }
        return fs;
    }

    @Override
    public SequenceProcessor getInvoiceSequence() {
        if(invoiceSequence == null) {
           invoiceSequence = (SequenceProcessor) context.getBean("sequenceProcessor", organisation.getId() + "_InvoiceSequence"); 
        }
        return invoiceSequence;
    }

    @Override
    public SequenceProcessor getOrderSequence() {
        if(orderSequence == null) {
           orderSequence = (SequenceProcessor) context.getBean("sequenceProcessor", organisation.getId() + "_OrderSequence"); 
        }
        return orderSequence;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
