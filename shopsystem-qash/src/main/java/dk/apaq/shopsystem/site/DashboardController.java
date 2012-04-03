package dk.apaq.shopsystem.site;

import dk.apaq.filter.Filter;
import dk.apaq.filter.core.AndFilter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.core.OrFilter;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.management.SubscriptionManagerBean;
import dk.apaq.shopsystem.security.SystemUserDetails;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author michael
 */
@Controller
public class DashboardController {

    @Autowired
    private SystemService service;
    
    @Autowired
    private SubscriptionManagerBean subscriptionManagerBean;

    private Filter orderFilter = new CompareFilter("dateInvoiced", DateUtils.addDays(new Date(), -30), CompareFilter.CompareType.GreaterOrEqual);
    
    @RequestMapping("/dashboard.htm")
    public ModelAndView handleRequest(@RequestParam(defaultValue="true") Boolean autoRedirect, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        OrganisationCrud orgCrud = service.getOrganisationCrud();

        SystemUserDetails sud = ((SystemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        List<Organisation> orglist = orgCrud.list(null, sud.getUser());

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("organisations", orglist);
        model.put("user", sud.getUser());
        model.put("service", service);
        model.put("oneMonthFilter", orderFilter);
        model.put("subscription", subscriptionManagerBean);
        return new ModelAndView("dashboard", model);
    }
    
    private Order getOutstandingOrder(Organisation org) {
        Filter filter = new AndFilter(
                            new CompareFilter("buyerId", org.getId(), CompareFilter.CompareType.Equals),
                            new CompareFilter("status", OrderStatus.Accepted, CompareFilter.CompareType.Equals));
        OrganisationService mainOrgService = service.getOrganisationService(service.getMainOrganisation());
        List<String> ids = mainOrgService.getOrders().listIds(filter, null);
        return ids.isEmpty() ? null : mainOrgService.getOrders().read(ids.get(0));
    }

}
