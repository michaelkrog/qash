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
 * @author krog
 */
public class SubscriptionUtil {

    private static final NumberFormat feeFormatter = NumberFormat.getPercentInstance();
    
    private SubscriptionUtil() {}
    
    /**
     * Calculates the revenue made by the organisation that is subscribing since last charged.
     */
    public static double getRevenueSinceLastCharge(SystemService service, Subscription subscription) {

        Date dateFrom = subscription.getDateCharged() == null ? subscription.getDateCreated() : subscription.getDateCharged();
        Filter orderFilter = new AndFilter(new CompareFilter("status", OrderStatus.Completed, CompareFilter.CompareType.Equals),
                new CompareFilter("dateChanged", dateFrom, CompareFilter.CompareType.GreaterOrEqual));
        Organisation customer = subscription.getCustomer().getCustomer();
        String currency = customer.getCurrency();

        double fee = 0;

        OrganisationService customerOrganisationService = service.getOrganisationService(subscription.getCustomer().getCustomer());
        List<Order> orderList = customerOrganisationService.getOrders().list(orderFilter, null);
        for (Order order : orderList) {
            if (!currency.equals(order.getCurrency())) {
                //TODO: Handle customers orders in other currencies than their default
                continue;
            }

            fee += order.getTotalWithTax();
        }

        return fee;
    }

    /**
     * Generated a new order for the subscription.
     */
    public static Order generateOrderFromSubscription(SystemService service, Subscription subscription) {
        CustomerRelationship customerRelationship = subscription.getCustomer();
        Country customerCountry = Country.getCountry(customerRelationship.getCustomer().getCountryCode(), Locale.getDefault());

        String customerCurrency = customerRelationship.getCustomer().getCurrency();

        Order order = new Order();

        //1: Find out how much to collect
        if (subscription.getPricingType() == SubscriptionPricingType.QashUsageBase) {
            //1a: if qashUsageBased subscription calculate usage fee
            order.setCurrency(customerCurrency);
            double revenue = getRevenueSinceLastCharge(service, subscription);
            double fee = revenue * customerRelationship.getCustomer().getFeePercentage();

            order.addOrderLine("Fee for revenue (" + feeFormatter.format(fee) + ")", 1, fee, TaxTool.getTaxBasedOnCountry(customerCountry));
        } else {
            order.setCurrency(subscription.getCurrency());

            //1b: else use price specified on subscription
            order.addOrderLine("Subscription", 1, subscription.getPrice(), TaxTool.getTaxBasedOnCountry(customerCountry));
        }
        return order;
    }

    /**
     * Detects wether subscription is due for calculation.
     */
    public static boolean isDueForCollection(Subscription subscription) {
        Date startOfCurrentInterval = subscription.getDateCharged() == null ? subscription.getDateCreated() : subscription.getDateCharged();

        DateTime cutOffTime = new DateTime(startOfCurrentInterval.getTime());

        switch (subscription.getIntervalUnit()) {
            case Hour:
                cutOffTime.plusHours(subscription.getInterval());
                break;
            case Day:
                cutOffTime.plusDays(subscription.getInterval());
                break;
            case Week:
                cutOffTime.plusWeeks(subscription.getInterval());
                break;
            case Month:
                cutOffTime.plusMonths(subscription.getInterval());
                break;
            case Year:
                cutOffTime.plusYears(subscription.getInterval());
                break;
        }

        //If time has passed cutOffTime then return true
        return cutOffTime.isBeforeNow();

    }
    
}
