package dk.apaq.shopsystem.api;

import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * An abtract class for Rest API controllers.
 */
public abstract class AbstractController {

    private final SystemService service;

    public AbstractController(SystemService service) {
        this.service = service;
    }
    
    
    /**
     * Retrieve organaistion from id.
     */
    protected OrganisationService getOrganisationService(String id) {

        Organisation org = service.getOrganisationCrud().read(id);
        if (org == null) {
            throw new ResourceNotFoundException("Organisation not found. [id=" + id + "]");
        }
        return service.getOrganisationService(org);
    }


}
