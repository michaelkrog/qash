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
import dk.apaq.shopsystem.entity.Organisation;
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

    @Override
    public OrganisationService getOrganisationService(Organisation org) {
        OrganisationService service = orgServiceMap.get(org.getId());
        if(service==null) {
            service = (OrganisationService) context.getBean("organisationService", org);
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
            systemUserCrud = (Crud.Complete<String, SystemUser>) context.getBean("crud", em, SystemUser.class);
            ((CrudNotifier)systemUserCrud).addListener(new SecurityHandler.AccountSecurity());
        }
        return systemUserCrud;
    }

    @Override
    public Complete<String, Order> getOrderCrud(Organisation organisation) {
        return getOrganisationService(organisation).getOrders();
    }

    @Override
    public Complete<String, Product> getProductCrud(Organisation organisation) {
        return getOrganisationService(organisation).getProducts();
    }

    @Override
    public Editable<String, Tax> getTaxCrud(Organisation organisation) {
        return getOrganisationService(organisation).getTaxes();
    }

    @Override
    public Complete<String, Payment> getPaymentCrud(Organisation organisation) {
        return getOrganisationService(organisation).getPayments();
    }

    @Override
    public Complete<String, Category> getCategoryCrud(Organisation organisation) {
        return getOrganisationService(organisation).getCategories();
    }

    @Override
    public Editable<String, Store> getStoreCrud(Organisation organisation) {
        return getOrganisationService(organisation).getStores();
    }

    @Override
    public Complete<String, Website> getWebsiteCrud(Organisation organisation) {
         return getOrganisationService(organisation).getWebsites();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

   
}
