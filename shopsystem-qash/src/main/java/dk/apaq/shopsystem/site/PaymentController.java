package dk.apaq.shopsystem.site;

import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.api.ResourceNotFoundException;
import dk.apaq.shopsystem.entity.CustomerRelationship;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.PaymentType;
import dk.apaq.shopsystem.i18n.LocaleUtil;
import dk.apaq.shopsystem.pay.PaymentGateway;
import dk.apaq.shopsystem.pay.PaymentGatewayManager;
import dk.apaq.shopsystem.pay.PaymentGatewayType;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * A controller for the pages that shows the user forms and informations about payments
 * @author krog
 */
@Controller
public class PaymentController {
    
    private static final Logger LOG = LoggerFactory.getLogger(PaymentController.class);
    private final NumberFormat nfQuickPayOrderNumber = NumberFormat.getIntegerInstance();
    
    private SystemService service;
    private PaymentGatewayManager paymentGatewayManager;
    
    @Autowired
    @Qualifier(value="publicUrl")
    private String baseUrl;
    

    @Autowired
    public PaymentController(SystemService service, PaymentGatewayManager paymentGatewayManager) {
        this.service = service;
        this.paymentGatewayManager = paymentGatewayManager;
        nfQuickPayOrderNumber.setMinimumIntegerDigits(4);
        nfQuickPayOrderNumber.setMaximumIntegerDigits(20);
        nfQuickPayOrderNumber.setGroupingUsed(false);
    }

    @PostConstruct
    protected void init(){
        if(baseUrl == null || "".equals(baseUrl)) {
             baseUrl = "http://localhost:8080";
        }
        
        LOG.info("Baseurl for PaymentController is initialized as '{}'.", baseUrl);
        
    }
    
    public void setBaseUrl(String baseUrl) {
        LOG.info("Baseurl for PaymentController is changed to '{}'.", baseUrl);
        this.baseUrl = baseUrl;
    }
    
    
    @RequestMapping("/payment/{orgId}/{orderId}/form.htm")
    public ModelAndView handlePaymentForm(@PathVariable String orgId, @PathVariable String orderId) throws IOException {
        
        Organisation seller = service.getOrganisationCrud().read(orgId);
        OrganisationService sellerService = service.getOrganisationService(seller);
        Order order = sellerService.getOrders().read(orderId);
        
        Map model = new HashMap();
        
        switch(seller.getPaymentGatewayType()) {
            case QuickPay:
                String idPart = "/" + seller.getId() + "/" + order.getId();
                String urlPrefix = baseUrl + "/payment" + idPart;
                
                String returnUrl = urlPrefix + "/return.htm";
                String cancelUrl = urlPrefix + "/cancel.htm";
                String callbackUrl = baseUrl + "/api/organisations/" + seller.getId() + "/payments?gateway=quickpay";
                
                model.put("formUrl", "https://secure.quickpay.dk/form/");
                model.put("formElements", buildFormElementsForQuickPay(seller, order, returnUrl, cancelUrl, callbackUrl));
                return new ModelAndView("payment", model);        
            
            default:
                throw new IOException("Gateway not supported");
        }

    }
    
    @RequestMapping("/payment/{orgId}/{orderId}/return.htm")
    public ModelAndView handlePaymentReturn(@PathVariable String orgId, @PathVariable String orderId) throws IOException {
        //Find organisation and order
        Organisation seller = service.getOrganisationCrud().read(orgId);
        OrganisationService sellerService = service.getOrganisationService(seller);
        Order order = sellerService.getOrders().read(orderId);
        
        //show order and link to pdf
        return new ModelAndView("payment_return"); 
    }
    
    @RequestMapping(value="/payment/{orgId}/{orderId}/quickpay_callback.htm", method = RequestMethod.POST)
    @Transactional
    public void handleQuickpayCallback(@PathVariable String orgId, @PathVariable String orderId, 
                                        @RequestParam String msgtype, @RequestParam String ordernumber, @RequestParam String amount, 
                                        @RequestParam String currency, @RequestParam String time, @RequestParam String state, 
                                        @RequestParam String qpstat, @RequestParam String qpstatmsg, @RequestParam String chstat, 
                                        @RequestParam String chstatmsg, @RequestParam String merchant, @RequestParam String merchantemail, 
                                        @RequestParam String transaction, @RequestParam String cardtype,  @RequestParam String cardnumber, 
                                        @RequestParam String cardexpire, @RequestParam String splitpayment, @RequestParam String fraudprobability, 
                                        @RequestParam String fraudremarks, @RequestParam String fraudreport, @RequestParam String fee,
                                        @RequestParam String md5check) throws IOException, ServletException {
//Gør noget med MultipartHttpServletRequest
    
        //TODO Check md5
        
        if (!"000".equals(qpstat)) {
            LOG.debug("Ignored callback because it was not a valid payment callback.");
            return;
        }

        Organisation seller = service.getOrganisationCrud().read(orgId);
        Long lAmount = Long.parseLong(amount);
        
        //Check md5
        StringBuilder sb = new StringBuilder();
        sb.append(msgtype).append(ordernumber).append(amount).
                append(currency).append(time).append(state).
                append(qpstat).append(qpstatmsg).append(chstat).
                append(chstatmsg).append(merchant).append(merchantemail).
                append(transaction).append(cardtype).append(cardnumber).
                append(cardexpire).append(splitpayment).append(fraudprobability).
                append(fraudremarks).append(fraudreport).append(fee).
                append(seller.getMerchantSecret());
        String md5 = DigestUtils.md5Hex(sb.toString());
        
        if(!md5.equals(md5check)) {
            LOG.warn("md5check did not match. [orderid={}]", orderId);
            return;
        }
        
        OrganisationService sellerService = service.getOrganisationService(seller);
        Order order = sellerService.getOrders().read(orderId);
        CustomerRelationship customerRelationship = null;
        
        if(order.getBuyerId() != null) {
            customerRelationship = sellerService.getCustomers().read(order.getBuyerId());
        }
        
        if(order.getNumber() != Long.parseLong(ordernumber)) {
            LOG.warn("Ignored callback because the given ordernumber did not match. [orderid={}; ordernumber={}; given ordernumber={}]", new Object[]{orderId, order.getNumber(), ordernumber});
        }
        
        if("subscribe".equals(msgtype) && customerRelationship != null) {
            customerRelationship.setSubscriptionPaymentId(transaction);
            customerRelationship = sellerService.getCustomers().update(customerRelationship);
        }
        
        //Woohoo. :) User is really gonna pay - accept order if it isnt already accepted
        if(!order.getStatus().isConfirmedState()) {
            order.setStatus(OrderStatus.Accepted);
            order = sellerService.getOrders().update(order);
        }
        
        //take money
        PaymentGateway gateway = paymentGatewayManager.createPaymentGateway(PaymentGatewayType.QuickPay, merchant, seller.getMerchantSecret());
        gateway.capture(lAmount, transaction);
        
        //persist payment information
        Payment payment = new Payment();
        payment.setAmount(lAmount);
        payment.setOrderId(orderId);
        payment.setPaymentType(PaymentType.Card);
        payment.setPaymentDetails(cardtype + ": " + cardnumber);
        sellerService.getPayments().create(payment);
        
        //Payments may have changed order properties - reload it
        order = sellerService.getOrders().read(orderId);
        
        if(!order.isPaid()) {
            LOG.warn("A payment went down on an order but was not marked as fully paid. [orderId={}; order total={}; payment amount={}]", 
                    new Object[]{order.getId(), order.getTotalWithTax(), lAmount});
        }

    }
    
    
    
    
    @RequestMapping("/payment/{orgId}/{orderId}/cancel.htm")
    public ModelAndView handlePaymentCancel(@PathVariable String orgId, @PathVariable String orderId) throws IOException {
        //redirect to front dashboard
        
        return null;
    }
    
    private Map<String, String> buildFormElementsForQuickPay(Organisation seller, Order order, String returnUrl, String cancelUrl, String callbackUrl) {
        
        String customerLanguage = "en";
        if(order.getBuyer() != null && order.getBuyer().getCountryCode() != null) {
            Locale locale = LocaleUtil.getLocaleFromCountryCode(order.getBuyer().getCountryCode());
            if(locale != null && locale.getLanguage() != null) {
                customerLanguage = locale.getLanguage();
            }
        }
        
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("protocol", "4");
        map.put("msgtype", "subscribe"); //or authorize?
        map.put("merchant", seller.getMerchantId());
        map.put("language", customerLanguage);
        map.put("ordernumber", nfQuickPayOrderNumber.format(order.getNumber()));  //
        map.put("amount", Long.toString(order.getTotalWithTax()));
        map.put("currency", order.getCurrency());
        map.put("continueurl", returnUrl);
        map.put("cancelurl", cancelUrl);
        map.put("callbackurl", callbackUrl);
        map.put("autocapture", "0");
        map.put("cardtypelock", "");
        map.put("description", "Subscription for order #"+order.getNumber());
        map.put("splitpayment", "1");
        
        //md5
        StringBuilder builder = new StringBuilder();
        for(String value : map.values()) {
            builder.append(value);
        }
        builder.append(seller.getMerchantSecret());
        map.put("md5check", DigestUtils.md5Hex(builder.toString()));
        
        return map;
    }
    
    private String getOrderIdFromOrderNumber(OrganisationService organisationService, long orderNumber) {
        List<String> idList = organisationService.getOrders().listIds(new CompareFilter("number", orderNumber, CompareFilter.CompareType.Equals), null);
        if(idList.isEmpty()) {
            throw new ResourceNotFoundException("Order not found [ordernumber="+orderNumber+"]");
        }
        
        return idList.get(0);
    }
}
