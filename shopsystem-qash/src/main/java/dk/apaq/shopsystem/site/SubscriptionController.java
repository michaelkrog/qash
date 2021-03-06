package dk.apaq.shopsystem.site;

import dk.apaq.filter.Filter;
import dk.apaq.filter.core.AndFilter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.entity.CustomerRelationship;
import dk.apaq.shopsystem.entity.IntervalUnit;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Subscription;
import dk.apaq.shopsystem.entity.SubscriptionPricingType;
import dk.apaq.shopsystem.management.SubscriptionManagerBean;
import dk.apaq.shopsystem.pay.PaymentGateway;
import dk.apaq.shopsystem.pay.PaymentGatewayManager;
import dk.apaq.shopsystem.pay.PaymentGatewayType;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author michael
 */
@Controller
public class SubscriptionController {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionController.class);
    
    private SystemService service;
    private SubscriptionManagerBean subscriptionManagerBean;
    private PaymentGatewayManager paymentGatewayManager;

    @Autowired
    public SubscriptionController(SystemService service, PaymentGatewayManager paymentGatewayManager, SubscriptionManagerBean managerBean) {
        this.service = service;
        this.subscriptionManagerBean = managerBean;
        this.paymentGatewayManager = paymentGatewayManager;
    }
    
    
    @RequestMapping(value = "/subscribe/{orgId}/{planId}/plan.htm")
    public ModelAndView handleSubscriptionRequest(@PathVariable String organisationId,@PathVariable String planId, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Organisation org = service.getOrganisationCrud().read(organisationId);
        if (org.isSubscriber()) {
            return new ModelAndView("redirect:/dashboard.htm");
        }

        String currency = subscriptionManagerBean.getPaymentCurrencyForOrganisation(org);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("organisationId", organisationId);
        model.put("currency", currency);
        model.put("subscriptionFee", subscriptionManagerBean.getSubscriptionFee(currency));
        return new ModelAndView("subscribe", model);
    }

    @RequestMapping(value="/subscribe/{orgId}/{planId}/commit.htm")
    @Transactional
    public ModelAndView onSubscribe(@RequestParam(required = true) String organisationId) {

        Organisation org = service.getOrganisationCrud().read(organisationId);
        OrganisationService mainOrganisationService = service.getOrganisationService(service.getMainOrganisation());
        
        if (!org.isSubscriber()) {
            org.setSubscriber(true);
            
            service.getOrganisationCrud().update(org);
            
            //Get or create CustomerRelationShip
            CustomerRelationship relationship = mainOrganisationService.getCustomerRelationship(org, true);
            String currency = subscriptionManagerBean.getPaymentCurrencyForOrganisation(org);
                    
            Subscription subscription = new Subscription();
            subscription.setAutoRenew(true);
            
            //TODO Get price som SubscriptionPlan
            //subscription.setPrice(subscriptionManagerBean.getSubscriptionFee(currency));
            
            subscription.setInterval(1);
            subscription.setIntervalUnit(IntervalUnit.Month);
            subscription.setPricingType(SubscriptionPricingType.FixedSubsequent);
            subscription.setEnabled(true);
            subscription.setCustomer(relationship);
            
            mainOrganisationService.getSubscriptions().create(subscription);
        }
        
        return new ModelAndView("redirect:/dashboard.htm");
    }

    @RequestMapping(value="/unsubscribe.htm")
    public ModelAndView handleUnsubscribeRequest(@RequestParam(required=true) String organisationId, @RequestParam(required=false) Boolean unsubscribe,  HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Organisation org = service.getOrganisationCrud().read(organisationId);
        if(!org.isSubscriber()) {
            return new ModelAndView("redirect:/dashboard.htm");
        }
        
        if(Boolean.TRUE.equals(unsubscribe)) {
            
            return new ModelAndView("redirect:/dashboard.htm");
        }
        
        return new ModelAndView("unsubscribe", "organisationId", organisationId);
    }
    
    @RequestMapping(value="/unsubscribe_do.htm",method= RequestMethod.GET)
    @Transactional
    public ModelAndView onUnsubscribe(@RequestParam(required=true) String organisationId) {
            Organisation org = service.getOrganisationCrud().read(organisationId);
            OrganisationService mainOrganisationService = service.getOrganisationService(service.getMainOrganisation());
                
            if(org.isSubscriber()) {
                //Do unsubscribe
                org.setSubscriber(false);
                service.getOrganisationCrud().update(org);

                //Find subscription
                Subscription subscription = null;
                CustomerRelationship relationship = mainOrganisationService.getCustomerRelationship(org, false);
                if(relationship!=null) {
                    Filter filter = new AndFilter(new CompareFilter("customer", relationship, CompareFilter.CompareType.Equals),
                                                new CompareFilter("pricingType", SubscriptionPricingType.FixedSubsequent, CompareFilter.CompareType.Equals));
                    List<String> idlist = mainOrganisationService.getSubscriptions().listIds(filter, null);
                    if(!idlist.isEmpty()) {
                        subscription = mainOrganisationService.getSubscriptions().read(idlist.get(0));
                    }
                    
                }
                
                if(subscription != null) {
                    //Delete subscription
                    mainOrganisationService.getSubscriptions().delete(subscription.getId());
                    
                    Organisation seller = subscription.getOrganisation();
                    PaymentGateway paymentGateway = paymentGatewayManager.createPaymentGateway(seller.getPaymentGatewayType(), seller.getMerchantId(), seller.getMerchantSecret());
                    
                    //Do charge for orders not charged for
                    subscriptionManagerBean.performCollection(seller, paymentGateway, subscription);

                }
                
            }
            return new ModelAndView("redirect:/dashboard.htm");
    }
        
   
    
}
