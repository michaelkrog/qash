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

    /**
     * Ensures that a Limit is not null and does not exceed maxLimit.
     * If limit is null it will se set to defaultLimit. If limit exceeds
     * maxLimit it will be set to maxLimit.
     */
    protected Integer validateLimit(Integer limit, int defaultLimit, int maxLimit) {

        // For server protection, a limit of 1000 can't be exceeded
        if (limit == null) {
            limit = defaultLimit;
        }
        if (limit > maxLimit) {
            limit = maxLimit;
        }

        return limit;
    }
    
    protected Integer validateLimit(Integer limit) {
        return validateLimit(limit, 100, 1000);
    }
}
