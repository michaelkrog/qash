package dk.apaq.shopsystem.site;

import dk.apaq.shopsystem.entity.ContactInformation;
import dk.apaq.shopsystem.entity.CustomerRelationship;
import dk.apaq.shopsystem.entity.IntervalUnit;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Subscription;
import dk.apaq.shopsystem.entity.SubscriptionPricingType;
import dk.apaq.shopsystem.entity.Tax;
import dk.apaq.shopsystem.pay.PaymentGateway;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author michael
 */
@Controller
@RequestMapping()
public class SubscriptionController {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionController.class);
    
    @Autowired
    private SystemService service;
    @Autowired
    PaymentGateway paymentGateway;

    @RequestMapping(value = "/subscribe.htm", method = RequestMethod.GET)
    public ModelAndView handleSubscriptionRequest(@RequestParam(required = true) String organisationId, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Organisation org = service.getOrganisationCrud().read(organisationId);
        if (org.isSubscriber()) {
            return new ModelAndView("redirect:/dasboard.htm");
        }

        return new ModelAndView("subscribe");
    }

    @RequestMapping(value="/subscribe.htm", method= RequestMethod.POST)
    @Transactional
    public ModelAndView onSubscribe(@RequestParam(required = true) String organisationId) {

        Organisation org = service.getOrganisationCrud().read(organisationId);
        OrganisationService mainOrganisationService = service.getOrganisationService(service.getMainOrganisation());
        
        if (!org.isSubscriber()) {
            org.setSubscriber(true);
            service.getOrganisationCrud().update(org);
            
            //Get or create CustomerRelationShip
            CustomerRelationship relationship = mainOrganisationService.getCustomerRelationship(org, true);
            
            Subscription subscription = new Subscription();
            subscription.setAutoRenew(true);
            subscription.setCurrency(getPaymentCurrencyForOrganisation(org));
            subscription.setInterval(1);
            subscription.setIntervalUnit(IntervalUnit.Month);
            subscription.setPrice(0.2);
            subscription.setPricingType(SubscriptionPricingType.QashUsageBase);
            subscription.setEnabled(true);
            subscription.setCustomer(relationship);
            
            mainOrganisationService.getSubscriptions().create(subscription);
        }
        
        return new ModelAndView("redirect:/dasboard.htm");
    }

    @RequestMapping(value="/unsubscribe.htm",method= RequestMethod.GET)
    public ModelAndView handleUnsubscribeRequest(@RequestParam(required=true) String organisationId, @RequestParam(required=false) Boolean unsubscribe,  HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Organisation org = service.getOrganisationCrud().read(organisationId);
        if(!org.isSubscriber()) {
            return new ModelAndView("redirect:/dashboard.htm");
        }
        
        if(Boolean.TRUE.equals(unsubscribe)) {
            //Do unsubscribe
            org.setSubscriber(false);
            service.getOrganisationCrud().update(org);
            
            //Do charge for orders not charged for
            
            //Delete subscription
            
            return new ModelAndView("redirect:/dashboard.htm");
        }
        
        return new ModelAndView("unsubscribe", "organisationId", organisationId);
    }
        
    private String getPaymentCurrencyForOrganisation(Organisation org) {
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
    
}
