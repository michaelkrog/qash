package dk.apaq.shopsystem.management;

import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.PaymentType;
import dk.apaq.shopsystem.entity.Subscription;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.pay.PaymentException;
import dk.apaq.shopsystem.pay.PaymentGateway;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.util.SubscriptionUtil;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 *
 * @author krog
 */
public class SubscriptionManagerBean {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionManagerBean.class);
    
    private final EntityManager em;
    private final SystemService service;
    private final PaymentGateway paymentGateway;
    private final NumberFormat orderNumberFormatter = NumberFormat.getIntegerInstance();
    private final NumberFormat amountFormatter = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
    private final MailSender mailSender;
    private final SimpleMailMessage templateMessage;

    @Autowired
    public SubscriptionManagerBean(EntityManager em, SystemService service, PaymentGateway paymentGateway, MailSender mailSender, SimpleMailMessage templateMessage) {
        this.em = em;
        this.service = service;
        this.paymentGateway = paymentGateway;
        this.mailSender = mailSender;
        this.templateMessage = templateMessage;
        this.orderNumberFormatter.setMinimumIntegerDigits(4);
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
            if(!SubscriptionUtil.isDueForCollection(subscription)) {
                continue;
            }
            
            OrganisationService orgService = service.getOrganisationService(subscription.getOrganisation());
            SystemUser adminUser = getAdminUserForOrganisation(subscription.getCustomer().getCustomer());

            //1: generate order
            Order order = SubscriptionUtil.generateOrderFromSubscription(service, subscription);

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
                continue;
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
                continue;
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

            //9: Sleep a bit to make time for other threads also
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
            }
        }
    }

    private SystemUser getAdminUserForOrganisation(Organisation organisation) {
        return null;
    }


}
