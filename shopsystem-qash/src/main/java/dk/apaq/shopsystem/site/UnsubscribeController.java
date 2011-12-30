package dk.apaq.shopsystem.site;

import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.SystemService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("/unsubscribe.htm")
public class UnsubscribeController {
    
    @Autowired
    private SystemService service;
    
    @RequestMapping(method= RequestMethod.GET)
    public ModelAndView handleRequest(@RequestParam(required=true) String organisationId, @RequestParam(required=false) Boolean unsubscribe,  HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Organisation org = service.getOrganisationCrud().read(organisationId);
        if(!org.isSubscriber()) {
            return new ModelAndView("/dashboard.htm");
        }
        
        if(Boolean.TRUE.equals(unsubscribe)) {
            //Do unsubscribe
            org.setSubscriber(false);
            service.getOrganisationCrud().update(org);
            
            //Do charge for orders not charged for
            
            return new ModelAndView("/dashboard.htm");
        }
        
        return new ModelAndView("unsubscribe", "organisationId", organisationId);
    }

}
