package dk.apaq.shopsystem.site;

import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.api.ResourceNotFoundException;
import dk.apaq.shopsystem.entity.ContactInformation;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.PaymentType;
import dk.apaq.shopsystem.entity.Tax;
import dk.apaq.shopsystem.pay.PaymentGateway;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.util.Country;
import dk.apaq.shopsystem.util.StreamUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.wicket.protocol.http.servlet.MultipartServletWebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author michael
 */
@Controller
@RequestMapping()
public class SubscribeController {

    private static final Logger LOG = LoggerFactory.getLogger(SubscribeController.class);
    
    @Autowired
    private SystemService service;
    @Autowired
    PaymentGateway paymentGateway;

    @RequestMapping(value = "/subscribe.htm", method = RequestMethod.GET)
    public ModelAndView handleRequest(@RequestParam(required = true) String organisationId, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Organisation buyingOrg = service.getOrganisationCrud().read(organisationId);
        if (buyingOrg.isSubscriber()) {
            return new ModelAndView("redirect:/dasboard.htm");
        }

        Organisation sellingOrg = service.getMainOrganisation();
        OrganisationService sellingOrgService = service.getOrganisationService(sellingOrg);

        Order order = (Order) request.getSession().getAttribute("order");

        //if user already has an order in session then delete it if it has never been completed.
        if (order != null && order.getId() != null) {
            order = sellingOrgService.getOrders().read(order.getId());
            if (order != null && !order.getStatus().isConfirmedState()) {
                sellingOrgService.getOrders().delete(order.getId());
            }
        }

        order = new Order();

        //In order to support customers from outside Denmark we will use Euro or Dollars and a tax that fits.
        String countryCode = buyingOrg.getCountryCode();
        if (countryCode == null) {
            countryCode = "DK";
        }

        String feeCurrency;
        double startupFee;
        Tax feeTax;

        Country country = Country.getCountry(countryCode, request.getLocale());
        if ("DK".equals(country.getCode())) {
            feeCurrency = "DKK";
            startupFee = 49;
            feeTax = sellingOrgService.getDefaultTax();
        } else if (country.isWithinEu()) {
            feeCurrency = "EUR";
            startupFee = 7;
            feeTax = sellingOrgService.getDefaultTax();
        } else {
            feeCurrency = "USD";
            startupFee = 9;
            feeTax = null;
        }

        order.setCurrency(feeCurrency);
        order.addOrderLine("Qash - signup fee", 1, startupFee, feeTax);

        order.setBuyer(new ContactInformation(buyingOrg));
        String orderId = sellingOrgService.getOrders().create(order);
        order = sellingOrgService.getOrders().read(orderId);
        request.getSession().setAttribute("order", order);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("feeCurrency", feeCurrency);
        model.put("startupFee", startupFee);
        model.put("order", order);

        return new ModelAndView("subscribe", model);
    }

    @RequestMapping("/payment_ok.htm")
    public ModelAndView onPaymentSuccess(@RequestParam(required = true) Long ordernumber, @RequestParam String redirectUrl) {

        //Vis kvittering på ordre
        return new ModelAndView("payment_confirmation");
    }

    @RequestMapping("/payment_cancel.htm")
    public String onPaymentCancelled() {
        //Redirect direkte tilbage til dashboard
        return "redirect:/dashboard.htm";
    }

    @RequestMapping(value="/quickpay_callback.htm", method= RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void onQuickpayCallback(@RequestParam Long ordernumber, @RequestParam  Integer amount, 
                                    @RequestParam String currency, @RequestParam  String qpstat,
                                    @RequestParam String transaction, @RequestParam  String cardtype, @RequestParam  String cardnumber) throws IOException, ServletException {

        //TODO Check md5
        
        if (!"000".equals(qpstat)) {
            return;
        }

        Organisation org = service.getMainOrganisation();
        OrganisationService organisationService = service.getOrganisationService(org);
        
        List<String> idList = organisationService.getOrders().listIds(new CompareFilter("number", ordernumber, CompareFilter.CompareType.Equals), null);
        if(idList.isEmpty()) {
            throw new ResourceNotFoundException("Order not found [ordernumber="+ordernumber+"]");
        }
        
        String orderId = idList.get(0);
        
        //User is really gonna pay - accept order if it isnt already accepted
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
        order = organisationService.getOrders().read(orderId);
        if(order.isPaid()) {
            //TODO Which organisation?
            //org.setSubscriber(true);
            //org.setSubscriptionPaymentTransactionId(transaction);
            //organisationService.updateOrganisation(org);
        }
        
         

    }

    private String readPart(Part part) throws IOException {
        if (part == null) {
            return null;
        }
        InputStream in = part.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StreamUtils.copy(in, out);
        return new String(out.toByteArray());
    }
}
