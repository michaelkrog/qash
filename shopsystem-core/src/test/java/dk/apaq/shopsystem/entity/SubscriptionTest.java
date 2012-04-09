package dk.apaq.shopsystem.entity;

import java.util.Date;
import org.junit.Test;
import junit.framework.TestCase;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

/**
 *
 * @author krog
 */
public class SubscriptionTest extends TestCase {
    
    @Test
    public void testBeanPattern() {
        Subscription subscription = new Subscription();
        subscription.setAutoRenew(true);
        subscription.setCustomer(new CustomerRelationship(new Organisation()));
        subscription.setDateChanged(new Date());
        subscription.setDateCharged(new Date());
        subscription.setDateCreated(new Date());
        subscription.setEnabled(true);
        subscription.setId("id");
        subscription.setInterval(1);
        subscription.setIntervalUnit(IntervalUnit.Year);
        subscription.setOrganisation(new Organisation());
        subscription.getPriceTags().add(new PriceTag("DKK",100));
        subscription.setPricingType(SubscriptionPricingType.FixedSubsequent);
        
        assertTrue(subscription.isAutoRenew());
        assertNotNull(subscription.getCustomer());
        assertNotNull(subscription.getDateChanged());
        assertNotNull(subscription.getDateCharged());
        assertNotNull(subscription.getDateCreated());
        assertTrue(subscription.isEnabled());
        assertNotNull(subscription.getId());
        assertEquals(1, subscription.getInterval());
        assertNotNull(subscription.getIntervalUnit());
        assertNotNull(subscription.getOrganisation());
        assertEquals(100, subscription.getPriceTags().get(0).getMoney().getAmountMajorLong());
        assertNotNull(subscription.getPricingType());
        
    }
}
