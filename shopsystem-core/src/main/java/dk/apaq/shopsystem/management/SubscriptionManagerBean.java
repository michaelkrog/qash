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
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.pay.PaymentException;
import dk.apaq.shopsystem.pay.PaymentGateway;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.util.Country;
import dk.apaq.shopsystem.util.SubscriptionUtil;
import dk.apaq.shopsystem.util.TaxTool;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * SHOULD IMPLEMENT AN INTERFACE AND EXPOSE METHODS FOR OTHERS THAT NEED TO FORCE SUBSCRIPTION MAINTAINING
 * @author krog
 */
public class SubscriptionManagerBean {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionManagerBean.class);
    private static final NumberFormat feeFormatter = NumberFormat.getPercentInstance();
    
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private SystemService service;
    @Autowired
    private PaymentGateway paymentGateway;
    private final NumberFormat orderNumberFormatter = NumberFormat.getIntegerInstance();
    private final NumberFormat amountFormatter = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
    @Autowired
    private MailSender mailSender;
    @Autowired
    private SimpleMailMessage templateMessage;
    

    public SubscriptionManagerBean() {
        this.orderNumberFormatter.setMinimumIntegerDigits(4);
    }

    /**
     * Calculates the revenue made by the organisation that is subscribing since last charged.
     */
    public double getRevenueSinceLastCharge(Subscription subscription) {

        Date dateFrom = subscription.getDateCharged() == null ? subscription.getDateCreated() : subscription.getDateCharged();
        Filter orderFilter = new AndFilter(new CompareFilter("status", OrderStatus.Completed, CompareFilter.CompareType.Equals),
                new CompareFilter("dateChanged", dateFrom, CompareFilter.CompareType.GreaterOrEqual));
        Organisation customer = subscription.getCustomer().getCustomer();
        String currency = customer.getCurrency();

        double revenue = 0;

        OrganisationService customerOrganisationService = service.getOrganisationService(subscription.getCustomer().getCustomer());
        List<Order> orderList = customerOrganisationService.getOrders().list(orderFilter, null);
        for (Order order : orderList) {
            if (!currency.equals(order.getCurrency())) {
                //TODO: Handle customers orders in other currencies than their default
                continue;
            }

            revenue += order.getTotalWithTax();
        }

        return revenue;
    }
    
    /**
     * Generate a new order for the subscription.
     */
    public Order generateOrderFromSubscription(Subscription subscription) {
        CustomerRelationship customerRelationship = subscription.getCustomer();
        if(customerRelationship==null || customerRelationship.getCustomer() == null) {
            throw new NullPointerException("CustomerRelationsShip is not set for Subsciption");
        }
        
        String customerCountryCode = customerRelationship.getCustomer().getCountryCode();
        if(customerCountryCode==null) {
            //if customer has no countrycode set we will default to the organisations countrycode.
            customerCountryCode = subscription.getOrganisation().getCountryCode();
        }
        
        if(customerCountryCode==null) {
            throw new NullPointerException("No countrycode for customer could be resolved.");
        }

        Country customerCountry = Country.getCountry(customerCountryCode, Locale.getDefault());
        String customerCurrency = customerRelationship.getCustomer().getCurrency();

        Order order = new Order();

        //1: Find out how much to collect
        if (subscription.getPricingType() == SubscriptionPricingType.QashUsageBase) {
            //1a: if qashUsageBased subscription calculate usage fee
            order.setCurrency(customerCurrency);
            double revenue = getRevenueSinceLastCharge(subscription);
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
    public boolean isDueForCollection(Subscription subscription) {
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
    
    public void maintainSubscriptions() {
        //To ensure best performance, we will retrieve the subscriptions directly 
        //through the entitymanager instead of traversing all organsiations through 
        //the service.

        String sql = "select s from Subscription s where "
                + "enabled == true and autoRenew == true";

        List<Subscription> subscriptions = em.createQuery(sql).getResultList();
        for (Subscription subscription : subscriptions) {

            //ensure that the subscription is to be collected for
            if(!isDueForCollection(subscription)) {
                continue;
            }
            
            performCollection(subscription);

            //Sleep a bit to make time for other threads also
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
            }
        }
    }

    public void performCollection(Subscription subscription) {
        OrganisationService orgService = service.getOrganisationService(subscription.getOrganisation());
        SystemUser adminUser = getAdminUserForOrganisation(subscription.getCustomer().getCustomer());
        
        //1: generate order
        Order order = generateOrderFromSubscription(subscription);
        
        //2: Create order
        String id = orgService.getOrders().create(order);
        order = orgService.getOrders().read(id);
        
        //3:Update charge date on subscription
        subscription.setDateCharged(new Date());
        orgService.getSubscriptions().update(subscription);
        
        //4: If missing payment info send user mail
        if (subscription.getSubscriptionPaymentId() == null) {
            if (adminUser != null) {
                //TODO Unable to authorize payment - send mail to user regarding missing payment information
                SimpleMailMessage msg = this.templateMessage == null ? new SimpleMailMessage() : new SimpleMailMessage(this.templateMessage);
                msg.setSubject("New account");
                msg.setTo(adminUser.getEmail());
                msg.setText(
                        "Dear " + adminUser.getDisplayName()
                        + "\n\nWe are about to withdraw a payment for your subscription but we dont have your payment information.\n"
                        + "Please go to the following link in order to fulfill your payment.\n"
                        + "http://qashapp.com/payment.html?id="+order.getId()+"\n\n"
                        + "If we are not able to withdraw the payment within 14 days we will automatically cancel your subscription.\n\n"
                        + "Best Regards\n"
                        + "The Qash team.");
                try {
                    this.mailSender.send(msg);
                } catch (MailException ex) {
                    // simply log it and go on...
                    LOG.error("Unable to send mail.", ex);
                }
            }
            return;
        }
        double paymentAmount = order.getTotalWithTax();
        
        //5: If unable to authorize recurring payment send user mail
        try {
            paymentGateway.recurring(orderNumberFormatter.format(order.getNumber()), (int) (paymentAmount * 100), order.getCurrency(), false, subscription.getSubscriptionPaymentId());
        } catch (PaymentException ex) {
            if (adminUser != null) {
                //Unable to authorize payment - send mail to user regarding missing payment information
                SimpleMailMessage msg = this.templateMessage == null ? new SimpleMailMessage() : new SimpleMailMessage(this.templateMessage);
                msg.setSubject("New account");
                msg.setTo(adminUser.getEmail());
                msg.setText(
                        "Dear " + adminUser.getDisplayName()
                        + "\n\nWe were unable withdraw a payment for your subscription using the payment information you gave us earlier.\n"
                        + "Please go to the following link in order to fulfill your payment.\n"
                        + "http://qashapp.com/payment.html?id="+order.getId()+"\n\n"
                        + "If we are not able to withdraw the payment within 14 days we will automatically cancel your subscription.\n\n"
                        + "Best Regards\n"
                        + "The Qash team.");
                try {
                    this.mailSender.send(msg);
                } catch (MailException ex2) {
                    // simply log it and go on...
                    LOG.error("Unable to send mail.", ex);
                }
            }
            return;
        }
        
        //6: register payments for order
        Payment payment = new Payment();
        payment.setAmount(paymentAmount);
        payment.setOrderId(order.getId());
        payment.setPaymentType(PaymentType.Card);
        orgService.getPayments().create(payment);
        
        //7: capture amount
        paymentGateway.capture((int) (paymentAmount * 100), subscription.getSubscriptionPaymentId());
        
        //8: Send receipt
        if (adminUser != null) {
            //Unable to authorize payment - send mail to user regarding missing payment information
            SimpleMailMessage msg = this.templateMessage == null ? new SimpleMailMessage() : new SimpleMailMessage(this.templateMessage);
            msg.setSubject("New account");
            msg.setTo(adminUser.getEmail());
            msg.setText(
                    "Dear " + adminUser.getDisplayName()
                    + "\n\nWe have withdrawn a payment for your subscription with us.\n\n"
                    + "Amount: " + amountFormatter.format(paymentAmount) + "\n\n"
                    + "Please login to your account to retrieve invoices when needed.\n"
                    + "http://qashapp.com\n\n"
                    + "Best Regards\n"
                    + "The Qash team.");
            try {
                this.mailSender.send(msg);
            } catch (MailException ex) {
                // simply log it and go on...
                LOG.error("Unable to send mail.", ex);
            }
        }
    }

    private SystemUser getAdminUserForOrganisation(Organisation organisation) {
        return null;
    }


}
