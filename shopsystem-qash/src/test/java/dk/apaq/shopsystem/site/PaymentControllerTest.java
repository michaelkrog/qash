package dk.apaq.shopsystem.site;

import dk.apaq.shopsystem.entity.CustomerRelationship;
import dk.apaq.shopsystem.entity.IntervalUnit;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Subscription;
import dk.apaq.shopsystem.entity.SubscriptionPricingType;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.management.SubscriptionManagerBean;
import dk.apaq.shopsystem.pay.PaymentGatewayManager;
import dk.apaq.shopsystem.pay.PaymentGatewayType;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import static org.junit.Assert.*;

/**
 *
 * @author michael
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/defaultspringcontext.xml"})
@Transactional
public class PaymentControllerTest {

    @Autowired
    private SystemService service;
    @Autowired
    private SubscriptionManagerBean subscriptionManagerBean;
    @Autowired
    private PaymentGatewayManager paymentGatewayManager;
    private Organisation seller;
    private OrganisationService sellerService;
    private Organisation buyer;
    private CustomerRelationship relationship;
    private Subscription subscription;
    private DateFormat df = new SimpleDateFormat("yyMMddHHmmss");

    @Before
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("John", "Doe"));

        SystemUser user = new SystemUser();
        user.setName("John");
        user.setPassword("Doe");
        user = service.getSystemUserCrud().createAndRead(user);

        seller = new Organisation();
        seller = service.createOrganisation(user, seller);
        seller.setPaymentGatewayType(PaymentGatewayType.QuickPay);
        seller.setMerchantId("12121212");
        seller.setMerchantSecret("qwerty");
        service.getOrganisationCrud().update(seller);

        sellerService = service.getOrganisationService(seller);

        buyer = new Organisation();
        buyer = service.createOrganisation(user, buyer);

        relationship = sellerService.getCustomerRelationship(buyer, true);

        String currency = subscriptionManagerBean.getPaymentCurrencyForOrganisation(buyer);
        long price = subscriptionManagerBean.getSubscriptionFee(currency);

        if (price <= 0) {
            currency = "USD";
            price = subscriptionManagerBean.getSubscriptionFee(currency);
        }

        subscription = new Subscription();
        subscription.setCurrency(currency);
        subscription.setAutoRenew(true);
        subscription.setCustomer(relationship);
        subscription.setEnabled(true);
        subscription.setInterval(1);
        subscription.setPrice(price);
        subscription.setIntervalUnit(IntervalUnit.Month);
        subscription.setPricingType(SubscriptionPricingType.FixedSubsequent);
        subscription = sellerService.getSubscriptions().createAndRead(subscription);


        //subscriptionManagerBean.performCollection(seller, null, subscription)

    }

    /**
     * Test of handlePaymentForm method, of class PaymentController.
     */
    @Test
    public void testHandlePayment() throws Exception {
        System.out.println("handlePaymentForm");

        Order order = new Order();
        order.setBuyer(buyer);
        order.setBuyerId(buyer.getId());
        order.addOrderLine("Test", 1, 200, null);
        order.setCurrency("DKK");
        order = sellerService.getOrders().createAndRead(order);

        String orgId = seller.getId();
        String orderId = order.getId();

        PaymentController instance = new PaymentController(service, paymentGatewayManager);

        //Test form
        ModelAndView result = instance.handlePaymentForm(orgId, orderId);

        Map<String, Object> model = result.getModel();

        assertNotNull(model.get("formElements"));
        assertNotNull(model.get("formUrl"));

        Map<String, Object> formElements = (Map<String, Object>) model.get("formElements");


        //test callback by arguments and md5
        String msgtype="authorize";
        String ordernumber="000" + order.getNumber();
        String amount=Long.toString(order.getTotalWithTax());
        String currency=order.getCurrency();
        String time=df.format(new Date());
        String state="authorized";
        String qpstat="000";
        String qpstatmsg="OK";
        String chstat="1";
        String chstatmsg="OK";
        String merchant=seller.getMerchantId();
        String merchantemail="john@doe.dk";
        String transaction="54321";
        String cardtype="VISA";
        String cardnumber="XXXX XXXX XXXX 1234";
        String cardexpire="";
        String splitpayment="1";
        String fraudprobability="low";
        String fraudremarks="";
        String fraudreport="";
        String fee="0";
        StringBuilder sb = new StringBuilder();
        sb.append(msgtype). append(ordernumber).append(amount).append(currency).append(time).append(state).append(qpstat).
                append(qpstatmsg).append(chstat).append(chstatmsg).append(merchant).append(merchantemail).append(transaction).
                append(cardtype).append(cardnumber).append(cardexpire).append(splitpayment).append(fraudprobability).
                append(fraudremarks).append(fraudreport).append(fee).append(seller.getMerchantSecret());
        String md5check = DigestUtils.md5Hex(sb.toString());
        
        instance.handleQuickpayCallback(orgId, orderId, msgtype, ordernumber, amount, currency, time, state, qpstat, qpstatmsg, 
                                        chstat, chstatmsg, merchant, merchantemail, transaction, cardtype, cardnumber, cardexpire, 
                                        splitpayment, fraudprobability, fraudremarks, fraudreport, fee, md5check);


        
    }
}
