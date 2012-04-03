package dk.apaq.shopsystem.management;

import java.util.Map;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import dk.apaq.shopsystem.entity.CustomerRelationship;
import dk.apaq.shopsystem.entity.IntervalUnit;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.SubscriptionPricingType;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.util.Date;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Subscription;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.pay.MockPaymentGateway;
import dk.apaq.shopsystem.service.MockMailSender;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;

/**
 *
 * @author krog
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/defaultspringcontext.xml"})
@Transactional
public class SubscriptionManagerBeanTest {

    @Autowired
    private SystemService service;
    @Autowired
    private MockMailSender mailSender;
    @Autowired
    SubscriptionManagerBean subscriptionManagerBean;
    @Autowired 
    private MockPaymentGateway paymentGateway;
    private Organisation organisation;
    private OrganisationService organisationService;
    private CustomerRelationship relationship;
    private Organisation customer;
    private Subscription subscriptionDue;
    private Subscription subscriptionNotDue;
    private boolean initialized = false;

    @Before
    public void init() {
        mailSender.reset();

        if (!initialized) {


            SystemUser customerUser = new SystemUser();
            customerUser.setEmail("john@doe.dk");
            customerUser.setName("john@doe.dk");
            customerUser.setPassword("doe");
            String customerUserId = service.getSystemUserCrud().create(customerUser);
            customerUser = service.getSystemUserCrud().read(customerUserId);

            organisation = new Organisation();
            organisation.setCompanyName("Apaq");
            organisation.setCountryCode("DK");
            String orgId = service.getOrganisationCrud().create(organisation);
            organisation = service.getOrganisationCrud().read(orgId);

            organisationService = service.getOrganisationService(organisation);

            customer = new Organisation();
            customer.setCompanyName("Qwerty");

            customer = service.createOrganisation(customerUser, customer);

            relationship = organisationService.getCustomerRelationship(customer, true);

            Date anHourAgo = new Date(System.currentTimeMillis() - 3600000);
            subscriptionDue = new Subscription();
            subscriptionDue.setCurrency("DKK");
            subscriptionDue.setEnabled(true);
            subscriptionDue.setAutoRenew(true);
            subscriptionDue.setCustomer(relationship);
            subscriptionDue.setInterval(1);
            subscriptionDue.setIntervalUnit(IntervalUnit.Hour);
            subscriptionDue.setDateChanged(anHourAgo);
            subscriptionDue.setDateCreated(anHourAgo);
            subscriptionDue.setDateCharged(anHourAgo);
            subscriptionDue.setPrice(9995);
            subscriptionDue.setPricingType(SubscriptionPricingType.FixedSubsequent);
            organisationService.getSubscriptions().create(subscriptionDue);

            subscriptionNotDue = new Subscription();
            subscriptionNotDue.setCurrency("DKK");
            subscriptionNotDue.setEnabled(true);
            subscriptionNotDue.setAutoRenew(true);
            subscriptionNotDue.setCustomer(relationship);
            subscriptionNotDue.setInterval(2);
            subscriptionNotDue.setIntervalUnit(IntervalUnit.Hour);
            subscriptionNotDue.setDateChanged(anHourAgo);
            subscriptionNotDue.setDateCreated(anHourAgo);
            subscriptionNotDue.setDateCharged(anHourAgo);
            subscriptionNotDue.setPrice(9995);
            subscriptionNotDue.setPricingType(SubscriptionPricingType.FixedSubsequent);
            subscriptionNotDue = organisationService.getSubscriptions().read(organisationService.getSubscriptions().create(subscriptionNotDue));

            OrganisationService custService = service.getOrganisationService(customer);
            Order order = new Order();
            order.addOrderLine("test", 1, 100, null);
            order.setStatus(OrderStatus.Completed);
            custService.getOrders().create(order);
            
            initialized = true;
        }
    }

    @Test
    public void testIsDueForCollection() {
        System.out.println("isDueForCollection");
        assertTrue(subscriptionManagerBean.isDueForCollection(subscriptionDue));
        assertFalse(subscriptionManagerBean.isDueForCollection(subscriptionNotDue));
    }

    @Test
    public void testMaintainSubscriptionNoPaymentId() {
        System.out.println("maintainSubscription");

        assertEquals(0, organisationService.getOrders().listIds().size());
        subscriptionManagerBean.maintainSubscriptions();
        assertEquals(1, organisationService.getOrders().listIds().size());

        SimpleMailMessage msg = mailSender.lastMessageSent();
        assertNotNull(msg);
        assertTrue(msg.getText().contains("Dear Customer"));
        assertEquals("john@doe.dk", msg.getTo()[0]);

    }
    
    @Test
    public void testPerformManualCollectionNoPaymentId() {
        System.out.println("performManualCollection");

        subscriptionManagerBean.performCollection(subscriptionNotDue);
        SimpleMailMessage msg = mailSender.lastMessageSent();
        assertNotNull(msg);
        assertTrue(msg.getText().contains("Dear Customer"));
        assertTrue(msg.getText().contains("we dont have your payment information"));
        assertEquals("john@doe.dk", msg.getTo()[0]);

        //Test with paymentinformation - unable to withdraw
        paymentGateway.setLegalTransactionId("qwerty");
        subscriptionNotDue.setSubscriptionPaymentId("123");
        
        subscriptionManagerBean.performCollection(subscriptionNotDue);
        msg = mailSender.lastMessageSent();
        assertNotNull(msg);
        assertTrue(msg.getText().contains("Dear Customer"));
        assertTrue(msg.getText().contains("unable withdraw a payment"));
        assertEquals("john@doe.dk", msg.getTo()[0]);
        
        //Test with paymentinformation - able to withdraw
        subscriptionNotDue.setSubscriptionPaymentId("qwerty");
        
        Order order = subscriptionManagerBean.performCollection(subscriptionNotDue);
        msg = mailSender.lastMessageSent();
        assertNotNull(msg);
        assertTrue(msg.getText().contains("Dear Customer"));
        assertTrue(msg.getText().contains("have withdrawn a payment"));
        assertEquals("john@doe.dk", msg.getTo()[0]);
        assertTrue(paymentGateway.isCaptured());
        assertEquals(order.getTotalWithTax(), paymentGateway.getCaptureAmount());
    }
}
