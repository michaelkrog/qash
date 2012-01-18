package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.CrudEvent.WithEntity;
import dk.apaq.crud.CrudEvent.WithId;
import dk.apaq.crud.CrudEvent.WithIdAndEntity;
import dk.apaq.crud.core.BaseCrudListener;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.core.NotFilter;
import dk.apaq.shopsystem.entity.ContentEntity;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.OrganisationUserReference;
import dk.apaq.shopsystem.security.SecurityRoles;
import dk.apaq.shopsystem.security.SystemUserDetails;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.ServiceException;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.service.SystemServiceHolder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author michaelzachariassenkrog
 */
public final class SecurityHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityHandler.class);

    private SecurityHandler() {
    }
    
    public static SystemService getSystemService() {
        SystemService service = SystemServiceHolder.getSystemService();
        if(service==null) {
            throw new ServiceException("SystemService not available.");
        }
        return service;
    }

    public static Authentication getAuthentication() {
        LOG.debug("Getting user authentication");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            LOG.debug("User not authenticated.");
            throw new SecurityException("User not authenticated.");
        }
        return auth;
    }

    public static boolean isAdministrator(Authentication auth) {
        if (auth == null) {
            return false;
        }

        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (SecurityRoles.ROLE_ADMIN.name().equals(authority.getAuthority())) {
                return true;

            }
        }
        return false;
    }

    private static boolean isInUserList(Authentication auth, List<OrganisationUserReference> users) {
        for(OrganisationUserReference user : users) {
            if(auth.getName().equals(user.getUser().getName())) {
                return true;
            }
        }
        return false;
    }

    public static class OrganisationSecurity extends BaseCrudListener<String, Organisation> {

        private SystemService service;

        public OrganisationSecurity(SystemService service) {
            this.service = service;
        }

        @Override
        public void onBeforeEntityUpdate(WithIdAndEntity<String, Organisation> event) {
            handleEdit(event.getCrud().read(event.getEntity().getId()));
        }

        @Override
        public void onBeforeEntityDelete(WithId<String, Organisation> event) {
            handleEdit(event.getCrud().read(event.getEntityId()));
        }

        private void handleEdit(Organisation organisation) {
            if(organisation!=null) {
                OrganisationService orgService = service.getOrganisationService(organisation);
                List<OrganisationUserReference> users = orgService.getUsers().list();


                Authentication auth = getAuthentication();
                if (!isAdministrator(auth) && !isInUserList(auth, users)) {
                    throw new SecurityException("User is not authorized to edit organisation.");
                }
            }
        }
    }

    public static class SystemUserSecurity extends BaseCrudListener<String, SystemUser> {

        
        /*
        @Override
        public void onEntityRead(WithEntity<String, Account> event) {
            Account account = event.getEntity();
            Authentication auth = getAuthentication();
            if (account.getName() != null && !isAdministrator(auth)
                    && !auth.getName().equals(account.getName())) {
                throw new SecurityException("User is not authorized to retrieve account.[Account=" + account.getName() + "; User=" + auth.getName() + "]");
            }
        }*/

        @Override
        public void onBeforeEntityUpdate(WithIdAndEntity<String, SystemUser> event) {
            handleEdit(event.getCrud().read(event.getEntity().getId()));
        }

        @Override
        public void onBeforeEntityDelete(WithId<String, SystemUser> event) {
            handleEdit(event.getCrud().read(event.getEntityId()));
        }

        private void handleEdit(SystemUser account) {
            if(account == null) {
                return;
            }
            Authentication auth = getAuthentication();
            if (account.getName() != null && !isAdministrator(auth)
                    && !auth.getName().equals(account.getName())) {
                throw new SecurityException("User is not administrator or owner of account. Cannot edit account.");
            }
        }
    }
    
    public static class OrganisationUserReferenceSecurity extends BaseCrudListener<String, OrganisationUserReference> {

        @Override
        public void onBeforeEntityUpdate(WithIdAndEntity<String, OrganisationUserReference> event) {
            handleEdit(event.getCrud().read(event.getEntity().getId()));
        }

        @Override
        public void onBeforeEntityDelete(WithId<String, OrganisationUserReference> event) {
            OrganisationUserReference entityFromPersistence = event.getCrud().read(event.getEntityId());
            handleEdit(entityFromPersistence);
            
            //In order to delete the Organisation must have other OrganisationUsers
            OrganisationService service = getSystemService().getOrganisationService(entityFromPersistence.getOrganisation());
            List<String> ids = service.getUsers().listIds(new NotFilter(new CompareFilter("id", entityFromPersistence.getId(), CompareFilter.CompareType.Equals)), null);
            if(ids.isEmpty()) {
                throw new ServiceException("Cannot delete the OrganisationUSer because it is the last one.");
            }
        }

        private void handleEdit(OrganisationUserReference user) {
            Authentication auth = getAuthentication();
            if(!(auth.getPrincipal() instanceof SystemUserDetails)) {
                throw new ServiceException("Authentication does not have SystemUserDetails as principal.");
            }
            
            SystemUserDetails sud = (SystemUserDetails) auth.getPrincipal();
            OrganisationService service = getSystemService().getOrganisationService(user.getOrganisation());
            
            //In order to edit the prinipal must:
            //- Have an OrganisationUser in the same Organisation.
            //- That OrganisationUser must have the Adminstrator role.
            boolean allowed = false;
            List<OrganisationUserReference> orgUsers = service.getUsers().list(new CompareFilter("user", sud.getUser(), CompareFilter.CompareType.Equals), null);
            for(OrganisationUserReference current : orgUsers) {
                if(current.getRoles().contains(SecurityRoles.ROLE_ORGADMIN.name())) {
                    allowed = true;
                    break;
                }
            }
            
            if(!allowed) {
                throw new SecurityException("Cannot edit the organisationuser because principal is not administrator of the organisation");
            }
        }
    }

    public static class ContentSecurity<T extends ContentEntity> extends BaseCrudListener<String, T> {

        private final Organisation organisation;

        public ContentSecurity(Organisation organisation) {
            if(organisation == null) {
                throw new NullPointerException("Organisation must not be null.");
            }
            this.organisation = organisation;
        }

        @Override
        public void onEntityRead(WithIdAndEntity<String, T> event) {
            T entity = event.getEntity();

            if(entity==null) {
                return;
            }
            
            if(entity.getOrganisation()==null || !organisation.getId().equals(entity.getOrganisation().getId())) {
                throw new SecurityException("Now allowed to read content from other organisations.");
            }
        }

        @Override
        public void onBeforeEntityUpdate(WithIdAndEntity<String, T> event) {
            handleEdit(event.getCrud().read(event.getEntity().getId()));
        }

        @Override
        public void onBeforeEntityDelete(WithId<String, T> event) {
            handleEdit(event.getCrud().read(event.getEntityId()));
        }

        private void handleEdit(T entity) {
            if(!organisation.getId().equals(entity.getOrganisation().getId())) {
                throw new SecurityException("Now allowed to edit content from other organisations.");
            }
        }
    }
}
