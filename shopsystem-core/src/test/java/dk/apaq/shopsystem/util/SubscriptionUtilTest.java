package dk.apaq.shopsystem.util;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Subscription;
import dk.apaq.shopsystem.service.SystemService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;

/**
 *
 * @author krog
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
public class SubscriptionUtilTest {
    
    @Autowired
    private SystemService service;
    
    @Before
    public void setup() {
        
    }
    
    public void testDummy() {
        
    }
    /*
    @Test
    public void testGetRevenueSinceLastCharge() {
        System.out.println("getRevenueSinceLastCharge");
        Subscription subscription = null;
        double expResult = 0.0;
        double result = SubscriptionUtil.getRevenueSinceLastCharge(service, subscription);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGenerateOrderFromSubscription() {
        System.out.println("generateOrderFromSubscription");
        SystemService service = null;
        Subscription subscription = null;
        Order expResult = null;
        Order result = SubscriptionUtil.generateOrderFromSubscription(service, subscription);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsDueForCollection() {
        System.out.println("isDueForCollection");
        Subscription subscription = null;
        boolean expResult = false;
        boolean result = SubscriptionUtil.isDueForCollection(subscription);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
