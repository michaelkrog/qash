package dk.apaq.shopsystem.util;

import dk.apaq.filter.Filter;
import dk.apaq.filter.core.AndFilter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.entity.CustomerRelationship;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Subscription;
import dk.apaq.shopsystem.entity.SubscriptionPricingType;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.joda.time.DateTime;

/**
 * Utility class for Subscriptions
 * 
 * SHOULD BE MOVED TO SubscriptionManagerBean
 * 
 * @author krog
 */
public class SubscriptionUtil {

    private static final NumberFormat feeFormatter = NumberFormat.getPercentInstance();
    
    private SubscriptionUtil() {}
    
    

    
    
}
