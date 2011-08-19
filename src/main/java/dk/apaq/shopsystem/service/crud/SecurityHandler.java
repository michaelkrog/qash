package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.CrudEvent.WithEntity;
import dk.apaq.crud.CrudEvent.WithId;
import dk.apaq.crud.core.BaseCrudListener;
import dk.apaq.shopsystem.model.AbstractContentEntity;
import dk.apaq.shopsystem.model.SystemUser;
import dk.apaq.shopsystem.model.Organisation;
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
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                return true;

            }
        }
        return false;
    }

    private static boolean isInUserList(Authentication auth, List<SystemUser> users) {
        for(SystemUser user : users) {
            if(auth.getName().equals(user.getName())) {
                return true;
            }
        }
        return false;
    }

    public static class OrganisationSecurity extends BaseCrudListener<String, Organisation> {

        
        @Override
        public void onEntityRead(WithEntity<String, Organisation> event) {
            /*Organisation organisation = event.getEntity();
            if(organisation!=null) {
                Authentication auth = getAuthentication();
                if (!isAdministrator(auth) && !isInUserList(auth, organisation.getUsers())) {
                    throw new SecurityException("User is not authorized to retrieve organisation.");
                }
            }*/
        }

        @Override
        public void onBeforeEntityUpdate(WithEntity<String, Organisation> event) {
            handleEdit(event.getCrud().read(event.getEntity().getId()));
        }

        @Override
        public void onBeforeEntityDelete(WithId<String, Organisation> event) {
            handleEdit(event.getCrud().read(event.getEntityId()));
        }

        private void handleEdit(Organisation organisation) {
            /*if(organisation==null) {
                return;
            }
            Authentication auth = getAuthentication();
            if (!isAdministrator(auth)
                    && !isInUserList(auth, organisation.getUsers())) {
                throw new SecurityException("User is not administrator, not the creator and is not in organisations userlist. Cannot edit organisation.");
            }*/
        }
    }

    public static class AccountSecurity extends BaseCrudListener<String, SystemUser> {

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
        public void onBeforeEntityUpdate(WithEntity<String, SystemUser> event) {
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

    public static class ContentSecurity<T extends AbstractContentEntity> extends BaseCrudListener<String, T> {

        private final Organisation organisation;

        public ContentSecurity(Organisation organisation) {
            if(organisation == null) {
                throw new NullPointerException("Organisation must not be null.");
            }
            this.organisation = organisation;
        }

        @Override
        public void onEntityRead(WithEntity<String, T> event) {
            T entity = event.getEntity();

            if(entity==null) {
                return;
            }
            
            if(!organisation.getId().equals(entity.getOrganisation().getId())) {
                throw new SecurityException("Now allowed to read content from other organisations.");
            }
        }

        @Override
        public void onBeforeEntityUpdate(WithEntity<String, T> event) {
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
