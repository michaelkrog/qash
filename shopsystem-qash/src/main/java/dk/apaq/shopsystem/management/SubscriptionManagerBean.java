package dk.apaq.shopsystem.management;

import dk.apaq.filter.Filter;
import dk.apaq.filter.core.AndFilter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.entity.CustomerRelationship;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.PaymentType;
import dk.apaq.shopsystem.entity.Subscription;
import dk.apaq.shopsystem.entity.SubscriptionPricingType;
import dk.apaq.shopsystem.entity.Tax;
import dk.apaq.shopsystem.pay.PaymentException;
import dk.apaq.shopsystem.pay.PaymentGateway;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.util.Country;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author krog
 */
public class SubscriptionManagerBean {

    @Autowired
    private SystemService service;
    @Autowired
    private PaymentGateway paymentGateway;
    private NumberFormat orderNumberFormatter = NumberFormat.getIntegerInstance();
    private NumberFormat feeFormatter = NumberFormat.getPercentInstance();

    public SubscriptionManagerBean() {
        orderNumberFormatter.setMinimumIntegerDigits(4);
    }

    public void maintainSubscriptions() {
        //To ensure best performance, we will retrieve the subscriptions directly 
        //through the entitymanager instead of traversing all organsiations through 
        //the service.

        List<Subscription> subscriptions = null; //Get subscriptions ready to be charged order by organisation
        for (Subscription subscription : subscriptions) {

            OrganisationService orgService = service.getOrganisationService(subscription.getOrganisation());
            
            CustomerRelationship customerRelationship = subscription.getCustomer();
            String customerCurrency = customerRelationship.getCustomer().getCurrency();
            Country customerCountry = Country.getCountry(customerRelationship.getCustomer().getCountryCode(), Locale.getDefault());

            Order order = new Order();

            //1: Find out how much to collect
            if (subscription.getPricingType() == SubscriptionPricingType.QashUsageBase) {
                //1a: if qashUsageBased subscription calculate usage fee
                order.setCurrency(customerCurrency);
                double revenue = getUsageFee(subscription);
                double fee = revenue * customerRelationship.getCustomer().getFeePercentage();

                order.addOrderLine("Fee for revenue (" + feeFormatter.format(fee) + ")", 1, fee, getTaxBasedOnCountry(customerCountry));
            } else {
                order.setCurrency(subscription.getCurrency());
            
                //1b: else use price specified on subscription
                order.addOrderLine("Subscription", 1, subscription.getPrice(), getTaxBasedOnCountry(customerCountry));
            }
            

            //2: Create order
            String id = orgService.getOrders().create(order);
            order = orgService.getOrders().read(id);
            
            //3: If missing payment info send user mail
            if (subscription.getSubscriptionPaymentId() == null) {
                //TODO Unable to authorize payment - send mail to user regarding missing payment information
                continue;
            }

            double paymentAmount = order.getTotalWithTax();
            
            //4: If unable to authorize recurring payment send user mail
            try {
                paymentGateway.recurring(orderNumberFormatter.format(order.getNumber()), (int) (paymentAmount * 100), order.getCurrency(), false, subscription.getSubscriptionPaymentId());
            } catch (PaymentException ex) {
                //TODO Unable to authorize payment - send user a mail regarding payment trouble
                continue;
            }

            //5: register payments for order
            Payment payment = new Payment();
            payment.setAmount(paymentAmount);
            payment.setOrderId(order.getId());
            payment.setPaymentType(PaymentType.Card);
            orgService.getPayments().create(payment);
            
            //6:Update charge date on subscription
            subscription.setDateCharged(new Date());
            orgService.getSubscriptions().update(subscription);

            //7: capture amount
            paymentGateway.capture((int) (paymentAmount * 100), subscription.getSubscriptionPaymentId());

            //8: Send invoice/receipt
            //TODO Send invoice

        }

    }

    private double getUsageFee(Subscription subscription) {

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
    
    private Tax getTaxBasedOnCountry(Country country) {
        //TODO This should actually be chosen from taxes the organisation has registered.
        //Right now this is harcoded for salers inside EU.
        if(country.isWithinEu()) {
            return new Tax("Vat", 25);
        } else {
            return null;
        }
    }
}
