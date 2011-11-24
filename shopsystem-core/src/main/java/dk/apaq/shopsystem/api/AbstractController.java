package dk.apaq.shopsystem.api;

import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * An abtract class for Rest API controllers.
 */
public abstract class AbstractController {

    @Autowired
    protected SystemService service;
    protected Organisation org;
    protected OrganisationService orgService;

    /**
     * Retrieve organaistion from id.
     */
    protected OrganisationService getOrganisationService(String id) {

        this.org = service.getOrganisationCrud().read(id);
        if (this.org == null) {
            throw new ResourceNotFoundException("Organisation not found. [id=" + id + "]");
        }
        this.orgService = service.getOrganisationService(this.org);

        return orgService;
    }


}
