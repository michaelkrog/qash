package dk.apaq.shopsystem.service;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.Domain;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;
import dk.apaq.vfs.FileSystem;
import java.io.Serializable;
/**
 *
 * @author michaelzachariassenkrog
 */
public interface SystemService extends Serializable {

    OrganisationCrud getOrganisationCrud();
    Crud.Complete<String, SystemUser> getSystemUserCrud();
    OrganisationService getOrganisationService(Organisation org);

    FileSystem getFileSystem();

    /**
     * Returns a crud for all domains in the system.
     */
    Crud.Filterable<String, Domain> getDomains();
    
    /**
     * Creates a new organisation together with a new user who will be
     * the administrator of the organisation. The organisation and the user
     * must not have been persisted previously.
     */
    Organisation createOrganisation(SystemUser user, Organisation organisation);
    
    Organisation getMainOrganisation();

    boolean hasUserVerifiedEmail(String userName);
    boolean isUsernameInUse(String userName);
}
