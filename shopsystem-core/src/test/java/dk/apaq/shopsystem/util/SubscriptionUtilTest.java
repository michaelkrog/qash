package dk.apaq.shopsystem.util;

import dk.apaq.shopsystem.entity.CustomerRelationship;
import dk.apaq.shopsystem.entity.IntervalUnit;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Subscription;
import dk.apaq.shopsystem.entity.SubscriptionPricingType;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;

/**
 *
 * @author krog
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
@Transactional
public class SubscriptionUtilTest {
    
    @Autowired
    private SystemService service;
    private Organisation organisation;
    private OrganisationService organisationService;
    private CustomerRelationship relationship;
    private Organisation customer;
    private Subscription subscription;
    
    @Before
    public void setup() {
        organisation = new Organisation();
        organisation.setCompanyName("Apaq");
        organisation.setCountryCode("DK");
        String orgId = service.getOrganisationCrud().create(organisation);
        organisation = service.getOrganisationCrud().read(orgId);
        
        organisationService = service.getOrganisationService(organisation);
        
        customer = new Organisation();
        customer.setCompanyName("Qwerty");
        customer.setFeePercentage(0.001);
        String custId = service.getOrganisationCrud().create(customer);
        customer = service.getOrganisationCrud().read(custId);
        
        relationship = organisationService.getCustomerRelationship(customer, true);
        
        Date anHourAgo = new Date(System.currentTimeMillis() - 3600000);
        subscription = new Subscription();
        subscription.setCurrency("DKK");
        subscription.setCustomer(relationship);
        subscription.setInterval(1);
        subscription.setIntervalUnit(IntervalUnit.Hour);
        subscription.setDateChanged(anHourAgo);
        subscription.setDateCreated(anHourAgo);
        subscription.setDateCharged(anHourAgo);
        subscription.setPricingType(SubscriptionPricingType.QashUsageBase);
        organisationService.getSubscriptions().create(subscription);
        
        OrganisationService custService = service.getOrganisationService(customer);
        Order order = new Order();
        order.addOrderLine("test", 1, 100, null);
        order.setStatus(OrderStatus.Completed);
        custService.getOrders().create(order);
    }
    
    @Test
    public void testGetRevenueSinceLastCharge() throws InterruptedException {
        System.out.println("getRevenueSinceLastCharge");
        
        double result = SubscriptionUtil.getRevenueSinceLastCharge(service, subscription);
        assertEquals(100.0, result,0.5);
    }

    @Test
    public void testGenerateOrderFromSubscription() {
        System.out.println("generateOrderFromSubscription");
        double expected = 0.1;
        Order result = SubscriptionUtil.generateOrderFromSubscription(service, subscription);
        assertEquals(expected, result.getTotalWithTax(), 0.05);
    }

    @Test
    public void testIsDueForCollection() {
        System.out.println("isDueForCollection");
        assertTrue(SubscriptionUtil.isDueForCollection(subscription));
    }

    
}
