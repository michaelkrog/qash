package dk.apaq.shopsystem.ui.springadmin;

import dk.apaq.shopsystem.api.ResourceNotFoundException;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class AbstractController {

    @Autowired
    protected SystemService service;
    protected Organisation org;
    protected OrganisationService orgService;
    @Autowired
    protected MessageSource messageSource;

    
    
    protected AbstractController() {
       
    }
    
    protected final void GetOrgService() {

        // @TODO: orgInfo should be a session.
        String id = "1";
        this.org =  this.service.getOrganisationCrud().read(id);
        
        if (this.org == null) {
            throw new ResourceNotFoundException("Organisation not found. [id=" + id + "]");
        }
        this.orgService = this.service.getOrganisationService(this.org);
     }

}
