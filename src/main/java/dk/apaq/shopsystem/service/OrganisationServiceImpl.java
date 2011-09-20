package dk.apaq.shopsystem.service;

import dk.apaq.crud.Crud;
import dk.apaq.crud.Crud.Complete;
import dk.apaq.crud.Crud.Editable;
import dk.apaq.crud.CrudNotifier;
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
import dk.apaq.shopsystem.entity.Module;
import dk.apaq.shopsystem.entity.Template;
import dk.apaq.shopsystem.entity.Theme;
import dk.apaq.shopsystem.service.crud.InventoryManager;
import dk.apaq.shopsystem.service.crud.ModuleCrud;
import dk.apaq.shopsystem.service.crud.SecurityHandler;
import dk.apaq.shopsystem.service.crud.ThemeCrud;
import dk.apaq.shopsystem.service.crud.UserCrud;
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.FileSystem;
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

    private UserCrud userCrud = null;
    private final Map<Class, Complete<String, Order>> crudMap = new WeakHashMap<Class, Complete<String, Order>>();
    private ApplicationContext context;
    private String orgId;
    private FileSystem fs;

    public OrganisationServiceImpl(Organisation org) {
        this.orgId = org.getId();
    }


    @Override
    public Complete<String, Order> getOrders() {
        Organisation organisation = readOrganisation();
        Complete crud = crudMap.get(Order.class);
        if(crud==null) {
            InventoryManager im = (InventoryManager) context.getBean("inventoryManager", getProducts());
            crud = (Crud.Complete<String, Payment>) context.getBean("orderCrud", em, organisation, im);
            ((CrudNotifier)crud).addListener(new SecurityHandler.ContentSecurity(organisation));
        }
        return crud;
    }

    @Override
    public Complete<String, Product> getProducts() {
        return getGenericContentCrud(Product.class);
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
    public Complete<String, Category> getCategories() {
        return getGenericContentCrud(Category.class);
    }

    @Override
    public Editable<String, Store> getStores() {
        return getGenericContentCrud(Store.class);
    }

    @Override
    public Complete<String, Website> getWebsites() {
         return getGenericContentCrud(Website.class);
    }

    private <T> Complete<String, T> getGenericContentCrud(Class<T> clazz) {
        Organisation organisation = readOrganisation();
        Complete crud = crudMap.get(clazz);
        if(crud==null) {
            crud = (Crud.Complete<String, T>) context.getBean("contentCrud", em, organisation, clazz);
            ((CrudNotifier)crud).addListener(new SecurityHandler.ContentSecurity(organisation));
            crudMap.put(clazz, crud);
        }
        return crud;
    }

    @Override
    public Organisation readOrganisation() {
        return em.find(Organisation.class, this.orgId);
    }

    @Override
    @Transactional
    public void updateOrganisation(Organisation org) {
        em.merge(org);
        em.flush();
    }

    @Override
    public UserCrud getUsers() {
        Organisation organisation = readOrganisation();
        if(userCrud==null) {
            userCrud =  (UserCrud) context.getBean("userCrud", em, organisation);
            ((CrudNotifier)userCrud).addListener(new SecurityHandler.ContentSecurity(organisation));
        }
        return userCrud;
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
    public Crud<String, Module> getModules() {
        try {
            return new ModuleCrud((Directory)service.getFileSystem().getNode("/System/Modules/Standard"),
                                                (Directory)getFileSystem().getNode("/Modules"));
        } catch (FileNotFoundException ex) {
            LOG.error("Unable to create modulecrud.", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public FileSystem getFileSystem() {
        if(fs==null) {
            try {
                //Must create a filesystem for this organisation using Commons VFS and a local File Folder.
                FileSystem systemFs = service.getFileSystem();
                Directory orgsDir = systemFs.getRoot().getDirectory("Organisations");
                if(!orgsDir.hasDirectory(orgId)) orgsDir.createDirectory(orgId);

                Directory orgDir = orgsDir.getDirectory(orgId);
                fs = new SubFs(systemFs, orgDir);

                Directory root = fs.getRoot();

                if(!root.hasDirectory("Content")) root.createDirectory("Content");
                if(!root.hasDirectory("Modules")) root.createDirectory("Modules");
                if(!root.hasDirectory("Themes")) root.createDirectory("Themes");
                
            } catch (IOException ex) {
                LOG.error("Unable to resolve filesystem for organisation.", ex);
                throw new RuntimeException(ex);
            }
        }
        return fs;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
