package dk.apaq.shopsystem.service;

import dk.apaq.shopsystem.entity.Category;
import dk.apaq.shopsystem.entity.Store;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.service.crud.InventoryManager;
import dk.apaq.shopsystem.service.crud.SecurityHandler;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.entity.Product;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.Tax;
import java.util.Map;
import java.util.WeakHashMap;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import dk.apaq.crud.Crud.*;
import dk.apaq.crud.CrudNotifier;
import dk.apaq.crud.jpa.EntityManagerCrud.EntityManagerCrudAssist;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.crud.GenericContentCrudAssist;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;
import javax.persistence.PersistenceContext;

/**
 *
 * @author michaelzachariassenkrog
 */
public class ServiceImpl implements Service, ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    private OrganisationCrud orgCrud;
    private Crud.Complete<String, SystemUser> systemUserCrud;
    private final Map<String, OrganisationService> orgServiceMap = new WeakHashMap<String,OrganisationService>();
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
    public OrganisationService getOrganisationService(Organisation org) {
        OrganisationService service = orgServiceMap.get(org.getId());
        if(service==null) {
            service = (OrganisationService) context.getBean("organisationService", em, org.getId());
            orgServiceMap.put(org.getId(), service);
        }
        return service;
    }


    
    @Override
    public OrganisationCrud getOrganisationCrud() {
        LOG.debug("Retrieving OrganisationCrud");
        if(orgCrud==null) {
            orgCrud = (OrganisationCrud) context.getBean("organisationCrud", em);
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
        String crudId = organisation.getId() + "_Order";
        Complete crud = crudMap.get(crudId);
        if(crud==null) {
            InventoryManager im = (InventoryManager) context.getBean("inventoryManager", getProductCrud(organisation));
            crud = (Crud.Complete<String, Payment>) context.getBean("orderCrud", em, organisation, im);
            ((CrudNotifier)crud).addListener(new SecurityHandler.ContentSecurity(organisation));
            crudMap.put(crudId, crud);
        }
        return crud;
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
        String crudId = organisation.getId() + "_Payment";
        Complete crud = crudMap.get(crudId);
        if(crud==null) {
            crud = (Crud.Complete<String, Payment>) context.getBean("paymentCrud", em, organisation, getOrderCrud(organisation));
            ((CrudNotifier)crud).addListener(new SecurityHandler.ContentSecurity(organisation));
            crudMap.put(crudId, crud);
        }
        return crud;
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
            crud = (Crud.Complete<String, T>) context.getBean("contentCrud", em, organisation, new GenericContentCrudAssist(organisation, clazz));
            ((CrudNotifier)crud).addListener(new SecurityHandler.ContentSecurity(organisation));
            crudMap.put(crudId, crud);
        }
        return crud;
    }
}
