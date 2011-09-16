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
import dk.apaq.shopsystem.service.crud.InventoryManager;
import dk.apaq.shopsystem.service.crud.SecurityHandler;
import dk.apaq.shopsystem.service.crud.UserCrud;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.VFS;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
public class OrganisationServiceImpl implements OrganisationService, ApplicationContextAware{

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
    public FileSystem getFileSystem() {
        if(fs==null) {
            try {
                //Must create a filesystem for this organisation using Commons VFS and a local File Folder.
                FileSystem systemFs = service.getFileSystem();
                FileObject fo = systemFs.getRoot().resolveFile("Organisations/"+orgId);
                fo.createFolder();
                fs = VFS.getManager().resolveFile(fo.getURL().toString()).getFileSystem();
                
                fs.resolveFile("Modules").createFolder();
                fs.resolveFile("Templates").createFolder();
                
            } catch (FileSystemException ex) {
                Logger.getLogger(OrganisationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
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
