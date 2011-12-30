package dk.apaq.shopsystem.site;

import dk.apaq.shopsystem.entity.ContactInformation;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Tax;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.util.Country;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author michael
 */
@Controller
@RequestMapping("/subscribe.htm")
public class SubscribeController {
    
    @Autowired
    private SystemService service;
    
    @RequestMapping(method= RequestMethod.GET)
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
            if (!order.getStatus().isConfirmedState()) {
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
}
