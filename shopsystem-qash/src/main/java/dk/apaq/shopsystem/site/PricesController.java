package dk.apaq.shopsystem.site;

import dk.apaq.shopsystem.management.SubscriptionManagerBean;
import dk.apaq.shopsystem.l10n.Country;
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
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author michael
 */
@Controller

public class PricesController {
    
    @Autowired
    private SubscriptionManagerBean subscriptionManagerBean;
    
    @RequestMapping("/prices.htm")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //Get country based on ip address
        Country country  = Country.getCountryByIpaddress(request.getRemoteAddr());
        
        //Get currency based on country
        String currency;
        if ("DK".equals(country.getCode())) {
            currency = "DKK";
        } else if (country.isWithinEu()) {
            currency = "EUR";
        } else {
            currency = "USD";
        }
        
        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("currency", currency);
        model.put("subscriptionFee", subscriptionManagerBean.getSubscriptionFee(currency));
        return new ModelAndView("prices", model);
    }
}
