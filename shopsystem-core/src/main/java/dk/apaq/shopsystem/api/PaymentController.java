package dk.apaq.shopsystem.api;

import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.filter.limit.Limit;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.PaymentType;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
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

@Controller
public class PaymentController extends AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentController.class);
    @Autowired
    private SystemService service;
    
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
    
    @RequestMapping(value = "/organisations/{orgInfo}/payments", method = RequestMethod.POST, params="gateway=quickpay")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    void onQuickpayEvent(@PathVariable String orgId, @RequestParam Long ordernumber, @RequestParam  Integer amount, 
                                    @RequestParam String currency, @RequestParam  String qpstat,
                                    @RequestParam String transaction, @RequestParam  String cardtype, @RequestParam  String cardnumber) throws IOException, ServletException {

        //TODO Check md5
        
        if (!"000".equals(qpstat)) {
            return;
        }

        OrganisationService organisationService = getOrganisationService(orgId);
        String orderId = getOrderIdFromOrderNumber(organisationService, ordernumber);
        
        //Woohoo. :) User is really gonna pay - accept order if it isnt already accepted
        Order order = organisationService.getOrders().read(orderId);
        if(!order.getStatus().isConfirmedState()) {
            order.setStatus(OrderStatus.Accepted);
            organisationService.getOrders().update(order);
        }
        
        Payment payment = new Payment();
        payment.setAmount(((double) amount) / 100);
        payment.setOrderId(orderId);
        payment.setPaymentType(PaymentType.Card);
        payment.setPaymentDetails(cardtype + ": " + cardnumber);
        organisationService.getPayments().create(payment);
        
        //if all paid then Enable subscription and Save transaction
        //order = organisationService.getOrders().read(orderId);
        //if(order.isPaid()) {
            //TODO Which organisation?
            //org.setSubscriber(true);
            //org.setSubscriptionPaymentTransactionId(transaction);
            //organisationService.updateOrganisation(org);
        //}
        
         

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
}