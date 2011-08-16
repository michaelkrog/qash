package dk.apaq.shopsystem.service;

import dk.apaq.shopsystem.model.Category;
import dk.apaq.shopsystem.model.Store;
import dk.apaq.shopsystem.model.Website;
import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.service.crud.InventoryManager;
import dk.apaq.shopsystem.service.crud.SecurityHandler;
import dk.apaq.shopsystem.model.SystemUser;
import dk.apaq.shopsystem.model.Product;
import dk.apaq.shopsystem.model.Order;
import dk.apaq.shopsystem.model.Payment;
import dk.apaq.shopsystem.model.Tax;
import java.util.Map;
import java.util.WeakHashMap;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import dk.apaq.crud.Crud.*;
import dk.apaq.crud.CrudNotifier;
import dk.apaq.crud.jpa.EntityManagerCrud.EntityManagerCrudAssist;
import dk.apaq.shopsystem.model.AbstractContentEntity;
import dk.apaq.shopsystem.model.Organisation;
import dk.apaq.shopsystem.service.crud.GenericContentCrudAssist;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;

/**
 *
 * @author michaelzachariassenkrog
 */
public class ServiceImpl implements Service, ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceImpl.class);

    @PersistenceUnit
    private EntityManager em;

    private OrganisationCrud orgCrud;
    private Crud.Complete<String, SystemUser> systemUserCrud;
    private final Map<String, Complete<String, Order>> crudMap = new WeakHashMap<String, Complete<String, Order>>();
    private ApplicationContext context;

    private class SystemUserCrudAssist implements EntityManagerCrudAssist<String, SystemUser> {

        @Override
        public Class<SystemUser> getEntityClass() {
            return SystemUser.class;
        }

        @Override
        public SystemUser createInstance() {
            return new SystemUser();
        }

        @Override
        public String getIdForEntity(SystemUser entity) {
            return entity.getId();
        }
        
    }
    
    @Override
    public OrganisationCrud getOrganisationCrud() {
        LOG.debug("Retrieving OrganisationCrud");
        if(orgCrud==null) {
            orgCrud = context.getBean("organisationCrud", OrganisationCrud.class);
            ((CrudNotifier)orgCrud).addListener(new SecurityHandler.OrganisationSecurity());
        }
        return orgCrud;
    }

    @Override
    public Complete<String, SystemUser> getSystemUserCrud() {
        LOG.debug("Retrieving AccountCrud");
        if(systemUserCrud==null) {
            systemUserCrud = (Crud.Complete<String, SystemUser>) context.getBean("crud", em, new SystemUserCrudAssist());
            ((CrudNotifier)systemUserCrud).addListener(new SecurityHandler.AccountSecurity());
        }
        return systemUserCrud;
    }

    @Override
    public Complete<String, Order> getOrderCrud(Organisation organisation) {
        return getGenericContentCrud(organisation, Order.class);
    }

    @Override
    public Complete<String, Product> getProductCrud(Organisation organisation) {
        return getGenericContentCrud(organisation, Product.class);
    }

    @Override
    public Editable<String, Tax> getTaxCrud(Organisation organisation) {
        return getGenericContentCrud(organisation, Tax.class);
    }

    @Override
    public Complete<String, Payment> getPaymentCrud(Organisation organisation) {
        return getGenericContentCrud(organisation, Payment.class);
    }

    @Override
    public Complete<String, Category> getCategoryCrud(Organisation organisation) {
        return getGenericContentCrud(organisation, Category.class);
    }

    @Override
    public Editable<String, Store> getStoreCrud(Organisation organisation) {
        return getGenericContentCrud(organisation, Store.class);
    }

    @Override
    public Complete<String, Website> getWebsiteCrud(Organisation organisation) {
         return getGenericContentCrud(organisation, Website.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private <T> Complete<String, T> getGenericContentCrud(Organisation organisation, Class<T> clazz) {
        String crudId = organisation.getId() + "_" + clazz.getSimpleName();
        Complete crud = crudMap.get(crudId);
        if(crud==null) {
            crud = (Crud.Complete<String, T>) context.getBean("contentCrud", em, new GenericContentCrudAssist(organisation, clazz));
            ((CrudNotifier)crud).addListener(new SecurityHandler.ContentSecurity(organisation));
            crudMap.put(crudId, crud);
        }
        return crud;
    }
}
