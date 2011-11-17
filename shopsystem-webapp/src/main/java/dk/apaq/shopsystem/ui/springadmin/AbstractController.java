package dk.apaq.shopsystem.ui.springadmin;

import dk.apaq.shopsystem.api.ResourceNotFoundException;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class AbstractController {

    @Autowired
    protected SystemService service;
    protected Organisation org;
    protected OrganisationService orgService;

    protected OrganisationService GetOrgService(String orgInfo) {

        // @TODO: orgInfo should be a secret string. Should it be possible to grant access to different parties?
        String id = orgInfo;
        this.org = service.getOrganisationCrud().read(id);
        if (this.org == null) {
            throw new ResourceNotFoundException("Organisation not found. [id=" + id + "]");
        }
        this.orgService = service.getOrganisationService(this.org);

        return orgService;
    }

    protected Integer ValidateLimit(Integer limit) {

        // For server protection, a limit of 1000 can't be exceeded
        if (limit == null) {
            limit = 1000;
        }
        if (limit > 1000) {
            limit = 1000;
        }

        return limit;
    }

}
