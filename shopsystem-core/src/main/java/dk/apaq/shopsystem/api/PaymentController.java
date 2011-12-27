package dk.apaq.shopsystem.api;

import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.filter.limit.Limit;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.PaymentType;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PaymentController extends AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentController.class);
    @Autowired
    private SystemService service;
    
    @Autowired
    PaymentGatewayHandler gatewayHandler;
    
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

    /**
     * Retrieves a specific payment.
     */
    @RequestMapping(value = "/organisations/{orgInfo}/payments/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Payment getPayment(@PathVariable String orgInfo, @PathVariable String id) {

        OrganisationService orgService = getOrganisationService(orgInfo);
        return orgService.getPayments().read(id);
    }

    @RequestMapping(value = "/pay/webhook", method = {RequestMethod.GET, RequestMethod.POST})
    public void onGatewayEvent(HttpServletRequest request, HttpServletResponse response) {
        String orderId = gatewayHandler.getOrderId(request);
        double amount = gatewayHandler.getAmount(request);
        String organisationId = gatewayHandler.getOrganisationId(request);

        Organisation org = service.getOrganisationCrud().read(organisationId);
        if (org == null) {
            throw new ResourceNotFoundException("Organisation not found [id=" + organisationId + "]");
        }

        OrganisationService organisationService = service.getOrganisationService(org);
        Order order = organisationService.getOrders().read(orderId);
        if (order == null) {
            throw new ResourceNotFoundException("Order not found [id=" + orderId + "]");
        }
        try {
            gatewayHandler.validate(request, response, order);
        } catch (IOException ex) {
            LOG.warn("Unable to validate payment", ex);
            throw new UnknownErrorException("Unable to validate payment.", ex);
        }

        //We make sure the order is accepted.
        if(!order.getStatus().isConfirmedState()) {
            order.setStatus(OrderStatus.Accepted);
            organisationService.getOrders().update(order);
        }
        
        //Insert payment. If the payment is enough, then the order will be completed automagically.
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setPaymentType(PaymentType.Card);
        payment.setOrderId(orderId);
        organisationService.getPayments().create(payment);
    }
}