package dk.apaq.shopsystem.api;

import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.filter.limit.Limit;
import dk.apaq.shopsystem.entity.CustomerRelationship;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.PaymentType;
import dk.apaq.shopsystem.pay.PaymentException;
import dk.apaq.shopsystem.pay.PaymentGateway;
import dk.apaq.shopsystem.pay.PaymentGatewayManager;
import dk.apaq.shopsystem.pay.PaymentGatewayType;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class PaymentController extends AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentController.class);
    public static final String[] QUICKPAY_KEYS = {"msgtype", "ordernumber", "amount", "currency", "time", "state", "qpstat", "qpstatmsg", 
                        "chstat", "chstatmsg", "merchant", "merchantemail", "transaction", "cardtype", "cardnumber",
                        "splitpayment", "fraudprobability", "fraudremarks", "fraudreport", "fee"};
    public static final String[] QUICKPAY_KEYS_SUBSCRIBE = {"msgtype", "ordernumber", "amount", "currency", "time", "state", "qpstat", "qpstatmsg", 
                        "chstat", "chstatmsg", "merchant", "merchantemail", "transaction", "cardtype", "cardnumber",
                        "cardexpire", "splitpayment", "fraudprobability", "fraudremarks", "fraudreport", "fee"};
    @Autowired
    private SystemService service;
    
    @Autowired
    private PaymentGatewayManager paymentGatewayManager;
    
    
    @Autowired
    public PaymentController(SystemService service) {
        super(service);
    }

    /**
     * Retrieves a payment list.
     */
    @RequestMapping(value = "/organisations/{orgInfo}/payments", method = RequestMethod.GET)
    public @ResponseBody
    List getPaymentList(@PathVariable String orgInfo, @RequestParam(value = "limit", required = false) Integer limit, @RequestParam(value = "full", required = false) String full) {

        OrganisationService orgService = getOrganisationService(orgInfo);
        Limit l = new Limit(ControllerUtil.validateLimit(limit));

        LOG.debug("Payments retrieved.");
        
        if ("true".equals(full)) {
            return orgService.getProducts().list(l);
        } else {
            return orgService.getProducts().listIds(l);
        }
    }
    
    @RequestMapping(value = "/organisations/{orgInfo}/payments", method = RequestMethod.POST, params="!gateway")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public @ResponseBody
    void persistPayment(@PathVariable String orgInfo, @RequestParam Payment payment) {

    }
    
    @RequestMapping(value = "/organisations/{orgId}/payments", method = RequestMethod.POST, params="gateway=quickpay")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    void onQuickpayEvent(MultipartHttpServletRequest request, @PathVariable String orgId) throws IOException, ServletException {
        LOG.debug("Payment event recieved");
            
        OrganisationService sellerService = getOrganisationService(orgId);
        Organisation seller = sellerService.readOrganisation();
        String eventType = request.getParameter("msgtype");
        
        
        String[] keys = "subscribe".equals(eventType) ? QUICKPAY_KEYS_SUBSCRIBE : QUICKPAY_KEYS;
        
        if(!validate(request, request.getParameter("md5check"), seller.getMerchantSecret(), keys)) {
            LOG.warn("Payment data is invalid!!! [orgId={}; remoteIp={}]", orgId, request.getRemoteAddr());
            throw new InvalidRequestException("The data sent is not valid(checked against md5check).");
        }
        
                                                
        if (!"000".equals(request.getParameter("qpstat"))) {
            LOG.debug("Payment did not have a qpstat we handle. [qpstat={}]", request.getParameter("qpstat"));
            //We dont care about other requests
            return;
        }
        
        long orderNumber;
        long amount;
        String transaction = request.getParameter("transaction");
        
        LOG.debug("Payment event type is " + eventType);
        
        try {
            orderNumber = Long.parseLong(request.getParameter("ordernumber"));
        } catch(NumberFormatException ex) {
            LOG.warn("Payment data was valid, but ordernumber is not a valid number!!! [ordernumber={}; remoteIp={}]", request.getParameter("ordernumber"), request.getRemoteAddr());
            throw new InvalidRequestException("ordernumber not a valid number [value="+request.getParameter("ordernumber") +"]");
        }
        
        try {
            amount = Long.parseLong(request.getParameter("amount"));
        } catch(NumberFormatException ex) {
            LOG.warn("Payment data was valid, but amount is not a valid number!!! [amount={}; remoteIp={}]", request.getParameter("amount"), request.getRemoteAddr());
            throw new InvalidRequestException("amount not a valid number [value="+request.getParameter("amount") +"]");
        }
        
        

        String orderId = getOrderIdFromOrderNumber(sellerService, orderNumber);
        LOG.debug("Orderid found from orderNumber. [orderId={}; orderNumber={}]", orderId, orderNumber);
        Order order = sellerService.getOrders().read(orderId);
        CustomerRelationship customerRelationship = null;
        PaymentGateway gateway = paymentGatewayManager.createPaymentGateway(PaymentGatewayType.QuickPay, seller.getMerchantId(), seller.getMerchantSecret());
            
        
        if(order.getBuyerId() != null) {
            LOG.debug("Order carried a customer. Loading customerRelationsShip");
            customerRelationship = sellerService.getCustomers().read(order.getBuyerId());
        }
        
        if("subscribe".equals(eventType)) {
            if(customerRelationship != null) {
                LOG.debug("Payment is a subscription and we have loaded a customerrelation. Transaction id will appended to the customer relationship. [tranaction={}; customerRelationsship={}]", 
                        transaction, customerRelationship.getId());
                customerRelationship.setSubscriptionPaymentId(transaction);
                customerRelationship = sellerService.getCustomers().update(customerRelationship);
            }
            
            LOG.debug("Charging money from subscription.");
            try {
                gateway.recurring(request.getParameter("ordernumber"), order.getTotalWithTax(), order.getCurrency(), true, transaction);
                LOG.debug("Money was charged from subscription.");
            } catch(PaymentException ex) {
                LOG.warn("Unable to charge money from subscription.", ex);
                throw ex;
            }
            
        } else {
        
            //Woohoo. :) User is really gonna pay - accept order if it isnt already accepted
            if(!order.getStatus().isConfirmedState()) {
                LOG.debug("Order was not already accepted. Changing it to accepted.");
                order.setStatus(OrderStatus.Accepted);
                order = sellerService.getOrders().update(order);
            }

            //take money
            LOG.debug("Charging money from customers card.");
            try {
                gateway.capture(amount, request.getParameter("transaction"));
                LOG.debug("Money was charged from card.");
            } catch(PaymentException ex) {
                LOG.warn("Unable to charge money from card.", ex);
                throw ex;
            }

            LOG.debug("Persisting payment information..");

            Payment payment = new Payment();
            payment.setAmount(amount);
            payment.setOrderId(orderId);
            payment.setPaymentType(PaymentType.Card);
            payment.setPaymentDetails(request.getParameter("cardtype") + ": " + request.getParameter("cardnumber"));
            sellerService.getPayments().create(payment);

            //Payments may have changed order properties - reload it
            order = sellerService.getOrders().read(orderId);

            if(!order.isPaid()) {
                LOG.warn("A payment went down on an order but was not marked as fully paid. [orderId={}; order total={}; payment amount={}]", 
                        new Object[]{order.getId(), order.getTotalWithTax(), amount});
            }
        }

    }

    

    /**
     * Retrieves a specific payment.
     */
    @RequestMapping(value = "/organisations/{orgInfo}/payments/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Payment getPayment(@PathVariable String orgInfo, @PathVariable String id) {

        OrganisationService orgService = getOrganisationService(orgInfo);
        return orgService.getPayments().read(id);
    }

    
    private String getOrderIdFromOrderNumber(OrganisationService organisationService, long orderNumber) {
        List<String> idList = organisationService.getOrders().listIds(new CompareFilter("number", orderNumber, CompareFilter.CompareType.Equals), null);
        if(idList.isEmpty()) {
            throw new ResourceNotFoundException("Order not found [ordernumber="+orderNumber+"]");
        }
        
        return idList.get(0);
    }
    
    private boolean validate(HttpServletRequest request, String md5check, String secret, String[] keys) {
        StringBuilder sb = new StringBuilder();
        for(String key : keys) {
            sb.append(request.getParameter(key));
        }
        sb.append(secret);
        return md5check.equals(DigestUtils.md5Hex(sb.toString()));
    }
}