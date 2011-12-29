package dk.apaq.shopsystem.site;

import dk.apaq.filter.Filter;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.security.SystemUserDetails;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping("/dashboard.htm")
    public ModelAndView handleRequest(@RequestParam(defaultValue="true") Boolean autoRedirect, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        OrganisationCrud orgCrud = service.getOrganisationCrud();

        //TODO List organisations
        SystemUserDetails sud = ((SystemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        List<Organisation> orglist = orgCrud.list(null, sud.getUser());

        //Wait - if user only has one shop and this is not forced, then redirect to the shop
        /*if (orglist.size() == 1 && autoRedirect) {
            String id = orglist.get(0).getId();
            return new ModelAndView("redirect:register/id:" + id);
        }*/
        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("organisations", orglist);
        model.put("service", service);
        return new ModelAndView("dashboard", model);
    }

}
