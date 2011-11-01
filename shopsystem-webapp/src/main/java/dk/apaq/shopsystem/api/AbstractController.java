package dk.apaq.shopsystem.api;

import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;


public class AbstractController {
    
    @Autowired
    protected SystemService service;
    
    protected Organisation org;
    protected OrganisationService orgService;
    
    
    protected OrganisationService GetOrgService(String orgInfo) {
        
        // TODO: orgInfo should be a secret string. Should it be possible to grant access to different parties?
        this.org = service.getOrganisationCrud().read(orgInfo);
        if (this.org == null) {
            
            // @TODO: Exception should be a http 404
            throw new UnsupportedOperationException("Please provide a valid request");
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
