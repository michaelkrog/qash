package dk.apaq.shopsystem.service;

import dk.apaq.shopsystem.service.crud.ShopCrud;
import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.service.crud.InventoryManager;
import dk.apaq.shopsystem.service.crud.SecurityHandler;
import dk.apaq.shopsystem.model.Account;
import dk.apaq.shopsystem.model.Product;
import dk.apaq.shopsystem.model.Order;
import dk.apaq.shopsystem.model.Payment;
import dk.apaq.shopsystem.model.Store;
import dk.apaq.shopsystem.model.Tax;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Future;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import dk.apaq.crud.Crud.*;
import dk.apaq.crud.CrudNotifier;
import dk.apaq.shopsystem.model.Organisation;

/**
 *
 * @author michaelzachariassenkrog
 */
public class ServiceImpl implements Service, ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceImpl.class);

    @PersistenceUnit
    private EntityManagerFactory emf;

    private ShopCrud shopCrud;
    private Crud.Complete<String, Account> accountCrud;
    private final Map<String, Complete<String, Order>> orderMap = new WeakHashMap<String, Complete<String, Order>>();
    private final Map<String, Complete<String, Product>> productMap = new WeakHashMap<String, Complete<String, Product>>();
    private final Map<String, Editable<String, Tax>> taxMap = new WeakHashMap<String, Editable<String, Tax>>();
    private final Map<String, Complete<String, Payment>> paymentMap = new WeakHashMap<String, Complete<String, Payment>>();

    private ApplicationContext context;

    public ShopCrud getShopCrud() {
        LOG.debug("Retrieving ShopCrud");
        if(shopCrud==null) {
            shopCrud = context.getBean("shopCrud", ShopCrud.class);
            ((CrudNotifier)shopCrud).addListener(new SecurityHandler.ShopSecurity());
        }
        return shopCrud;
    }

    public Complete<String, Account> getAccountCrud() {
        LOG.debug("Retrieving AccountCrud");
        if(accountCrud==null) {
            accountCrud = context.getBean("accountCrud", Complete.class);
            ((CrudNotifier)accountCrud).addListener(new SecurityHandler.AccountSecurity());
        }
        return accountCrud;
    }

    public Complete<String, Order> getOrderCrud(Organisation organisation) {
        LOG.debug("Retrieving OrderCrud");
        Crud itemCrud = getItemCrud(organisation);
        InventoryManager im = (InventoryManager) context.getBean("inventoryManager", itemCrud);

        Complete crud = orderMap.get(organisation.getId());
        if(crud == null) {
            crud = (Complete) context.getBean("orderCrud", organisation, im);
            ((CrudNotifier)crud).addListener(new SecurityHandler.OrderSecurity(organisation));
            orderMap.put(organisation.getId(), crud);
        }

        return crud;
    }

    public Complete<String, Product> getItemCrud(Organisation organisation) {
        LOG.debug("Retrieving ItemCrud");

        Complete crud = productMap.get(organisation.getId());
        if(crud == null) {
            crud = (Complete) context.getBean("itemCrud", organisation);
            ((CrudNotifier)crud).addListener(new SecurityHandler.ProductSecurity(organisation));
            productMap.put(organisation.getId(), crud);
        }
        return crud;
    }

    public Editable<String, Tax> getTaxCrud(Organisation organisation) {
        LOG.debug("Retrieving TaxCrud");
        Editable crud = taxMap.get(organisation.getId());
        if(crud == null) {
            crud = (Editable) context.getBean("taxCrud", organisation);
            ((CrudNotifier)crud).addListener(new SecurityHandler.TaxSecurity(organisation));
            taxMap.put(organisation.getId(), crud);
        }

        return crud;
    }

    public Complete<String, Payment> getPaymentCrud(Organisation organisation) {
        LOG.debug("Retrieving PaymentCrud");
        Complete crud = paymentMap.get(organisation.getId());
        if(crud == null) {
            crud = (Complete) context.getBean("paymentCrud", organisation, getOrderCrud(organisation));
            ((CrudNotifier)crud).addListener(new SecurityHandler.PaymentSecurity(organisation));
            paymentMap.put(organisation.getId(), crud);
        }

        return crud;
    }

    public boolean supportsIndexing() {
        return true;
    }

    public Future<?> startIndexing() {
        EntityManager em = emf.createEntityManager();
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);

        LOG.debug("Indexing started");

        return fullTextEntityManager.createIndexer().start();

    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

}
