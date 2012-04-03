package dk.apaq.shopsystem.management;

import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dk.apaq.filter.Filter;
import dk.apaq.filter.core.AndFilter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.entity.CustomerRelationship;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.OrganisationUserReference;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.PaymentType;
import dk.apaq.shopsystem.entity.Subscription;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.pay.PaymentException;
import dk.apaq.shopsystem.pay.PaymentGateway;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.util.Country;
import dk.apaq.shopsystem.util.TaxTool;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles managing of subscriptions. This is done by finding those that nned to be maintained because
 * its time for them to get paid etc.
 * @author krog
 */
public class SubscriptionManagerBean {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionManagerBean.class);
    private static final NumberFormat feeFormatter = NumberFormat.getPercentInstance();
    
    @PersistenceContext private EntityManager em;
    @Autowired private SystemService service;
    @Autowired private PaymentGateway paymentGateway;
    @Autowired private MailSender mailSender;
    @Autowired private SimpleMailMessage templateMessage;
    private final NumberFormat orderNumberFormatter = NumberFormat.getIntegerInstance();
    private final NumberFormat amountFormatter = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
    private Map<String, Long> subscriptionFeeMap;

    public SubscriptionManagerBean() {
        this.orderNumberFormatter.setMinimumIntegerDigits(4);
    }

    @PostConstruct
    protected void init() {
        if (subscriptionFeeMap == null) {
            subscriptionFeeMap = new HashMap<String, Long>();
        }
    }

    /**
     * Sets the subscription fee as a map in different currencies. The key is the currency code and
     * the double value is the amount to charge as fee in the given currency. The fee is for a month of subscription.
     * @param subscriptionFeeMap 
     */
    public void setSubscriptionFeeMap(Map<String, Long> subscriptionFeeMap) {
        this.subscriptionFeeMap = subscriptionFeeMap;
    }

    /**
     * Retrieves the fee in the given currencycode. If no fee is set then 0 is returned. The fee is for 
     * 1 month of subscription.
     * @param currency The 2 letter currencycode.
     * @return The fee in the given currency.
     */
    public long getSubscriptionFee(String currency) {
        if (subscriptionFeeMap.containsKey(currency)) {
            return subscriptionFeeMap.get(currency);
        } else {
            return 0;
        }
    }

    /**
     * Calculates the revenue made by the organisation that is subscribing since last charged. It is
     * based on the orders that has been marked completed since last charge. If the subscription has
     * never been charged, the create date of the subscription is used for calculation instead.
     */
   /* public double getRevenueSinceLastCharge(Subscription subscription) {

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
    }*/
    
    /**
     * Returns number of completede orders sinces last charge. If subscription has never been charged,
     * the date of creation is used instead.
     * @param subscription
     * @return 
     */
    /*public int countCompletedOrdersSinceLastCharge(Subscription subscription) {

        Date dateFrom = subscription.getDateCharged() == null ? subscription.getDateCreated() : subscription.getDateCharged();
        Filter orderFilter = new AndFilter(new CompareFilter("status", OrderStatus.Completed, CompareFilter.CompareType.Equals),
                new CompareFilter("dateChanged", dateFrom, CompareFilter.CompareType.GreaterOrEqual));
        Organisation customer = subscription.getCustomer().getCustomer();
        OrganisationService customerOrganisationService = service.getOrganisationService(customer);
        List<String> orderList = customerOrganisationService.getOrders().listIds(orderFilter, null);
        return orderList.size();
    }*/

    /**
     * Generate a new order for the subscription.
     */
    private Order generateOrderFromSubscription(Subscription subscription) {
        CustomerRelationship customerRelationship = subscription.getCustomer();
        if (customerRelationship == null || customerRelationship.getCustomer() == null) {
            throw new NullPointerException("CustomerRelationsShip is not set for Subsciption");
        }

        String customerCountryCode = customerRelationship.getCustomer().getCountryCode();
        if (customerCountryCode == null) {
            //if customer has no countrycode set we will default to the organisations countrycode.
            customerCountryCode = subscription.getOrganisation().getCountryCode();
        }

        if (customerCountryCode == null) {
            throw new NullPointerException("No countrycode for customer could be resolved.");
        }

        Country customerCountry = Country.getCountry(customerCountryCode, Locale.getDefault());
        String paymentCurrency = getPaymentCurrencyForOrganisation(customerRelationship.getCustomer());
        
        Order order = new Order();

        //1: Find out how much to collect
        
        order.setCurrency(subscription.getCurrency());
        order.setStatus(OrderStatus.Accepted);

        //1b: else use price specified on subscription
        order.addOrderLine("Subscription", 1, subscription.getPrice(), TaxTool.getTaxBasedOnCountry(customerCountry));
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
                cutOffTime = cutOffTime.plusHours(subscription.getInterval());
                break;
            case Day:
                cutOffTime = cutOffTime.plusDays(subscription.getInterval());
                break;
            case Week:
                cutOffTime = cutOffTime.plusWeeks(subscription.getInterval());
                break;
            case Month:
                cutOffTime = cutOffTime.plusMonths(subscription.getInterval());
                break;
            case Year:
                cutOffTime = cutOffTime.plusYears(subscription.getInterval());
                break;
        }

        //If time has passed cutOffTime then return true
        return cutOffTime.isBeforeNow();

    }

    public void maintainSubscriptions() {
        //To ensure best performance, we will retrieve the subscriptions directly 
        //through the entitymanager instead of traversing all organsiations through 
        //the service.

        String sql = "from Subscription s where s.enabled = true and s.autoRenew = true";

        List<Subscription> subscriptions = em.createQuery(sql).getResultList();
        for (Subscription subscription : subscriptions) {

            //ensure that the subscription is to be collected for
            if (!isDueForCollection(subscription)) {
                continue;
            }

            performCollection(subscription);
            
        }
    }

    /**
     * Performs a collection for a subscription. This is normally called automatically by the subscriptionManagerBean
     * when the subscription is due for collection, but sometimes other actions makes it neccesary to collect in the middle
     * of a period, and then this method can be used.<br><br>
     * 
     * The method can collect a valid subscription at any time. It will do the following:<br>
     * 1: Generate an order for the subscription.<br>
     * 2: Persist order at the main system organisation for the collection<br>
     * 3: Update charge date for the subscription.<br>
     * 4: If paymentinfo is missing an email is sent to the organisations adminuser<br>
     * 5: Authorizes withdrawal. If unable to authorize recurring payment an email is sent to the organisations adminuser<br>
     * 6: Register payments for order.<br>
     * 7: Capture amount<br>
     * 8: Send receipt via email to adminuser<br>
     * @param subscription 
     */
    @Transactional
    public Order performCollection(Subscription subscription) {
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
                        "Dear " + getNiceCustomerName(adminUser)
                        + "\n\nWe are about to withdraw a payment for your subscription but we dont have your payment information.\n"
                        + "Please go to the following link in order to fulfill your payment.\n"
                        + "http://qashapp.com/payment.html?id=" + order.getId() + "\n\n"
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
            return order;
        }
        long paymentAmount = order.getTotalWithTax();

        //5: If unable to authorize recurring payment send user mail
        try {
            paymentGateway.recurring(orderNumberFormatter.format(order.getNumber()), paymentAmount, order.getCurrency(), false, subscription.getSubscriptionPaymentId());
        } catch (PaymentException ex) {
            if (adminUser != null) {
                //Unable to authorize payment - send mail to user regarding missing payment information
                SimpleMailMessage msg = this.templateMessage == null ? new SimpleMailMessage() : new SimpleMailMessage(this.templateMessage);
                msg.setSubject("New account");
                msg.setTo(adminUser.getEmail());
                msg.setText(
                        "Dear " + getNiceCustomerName(adminUser)
                        + "\n\nWe were unable withdraw a payment for your subscription using the payment information you gave us earlier.\n"
                        + "Please go to the following link in order to fulfill your payment.\n"
                        + "http://qashapp.com/payment.html?id=" + order.getId() + "\n\n"
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
            return order;
        }

        //6: register payments for order
        Payment payment = new Payment();
        payment.setAmount(paymentAmount);
        payment.setOrderId(order.getId());
        payment.setPaymentType(PaymentType.Card);
        orgService.getPayments().create(payment);

        //7: capture amount
        paymentGateway.capture(paymentAmount, subscription.getSubscriptionPaymentId());

        //8: Send receipt
        if (adminUser != null) {
            //Unable to authorize payment - send mail to user regarding missing payment information
            SimpleMailMessage msg = this.templateMessage == null ? new SimpleMailMessage() : new SimpleMailMessage(this.templateMessage);
            msg.setSubject("New account");
            msg.setTo(adminUser.getEmail());
            msg.setText(
                    "Dear " + getNiceCustomerName(adminUser)
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
        
        //Return a fresh order
        return orgService.getOrders().read(id);
    }

    public String getPaymentCurrencyForOrganisation(Organisation org) {
        String countryCode = org.getCountryCode();
        if (countryCode == null) {
            countryCode = "DK";
        }

        String currency = null;

        Country country = Country.getCountry(countryCode, Locale.getDefault());
        if ("DK".equals(country.getCode())) {
            currency = "DKK";
        } else if (country.isWithinEu()) {
            currency = "EUR";
        } else {
            currency = "USD";
        }
        return currency;
    }

    private SystemUser getAdminUserForOrganisation(Organisation organisation) {
        for(OrganisationUserReference ref: service.getOrganisationService(organisation).getUsers().list()) {
            if(ref.getRoles().contains("ROLE_ORGPAYER")) {
                return ref.getUser();
            }
        }
        return null;
    }
    
    private String getNiceCustomerName(SystemUser user) {
        return user.getDisplayName() == null ? "Customer" : user.getDisplayName();
    }
}
