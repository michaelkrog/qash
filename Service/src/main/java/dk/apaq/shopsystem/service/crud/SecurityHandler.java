package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.CrudEvent.WithEntity;
import dk.apaq.crud.CrudEvent.WithId;
import dk.apaq.crud.core.BaseCrudListener;
import dk.apaq.shopsystem.model.Account;
import dk.apaq.shopsystem.model.Product;
import dk.apaq.shopsystem.model.Order;
import dk.apaq.shopsystem.model.Organisation;
import dk.apaq.shopsystem.model.Payment;
import dk.apaq.shopsystem.model.Store;
import dk.apaq.shopsystem.model.Tax;
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

    private static boolean isInUserList(Authentication auth, List<Account> users) {
        for(Account user : users) {
            if(auth.getName().equals(user.getName())) {
                return true;
            }
        }
        return false;
    }

    public static class ShopSecurity extends BaseCrudListener<String, Store> {

        
        @Override
        public void onEntityRead(WithEntity<String, Store> event) {
            Store shop = event.getEntity();
            Organisation organisation = shop.getOrganisation();
            if(shop!=null) {
                Authentication auth = getAuthentication();
                if (!isAdministrator(auth) && !isInUserList(auth, organisation.getUsers())) {
                    throw new SecurityException("User is not authorized to retrieve shop.");
                }
            }
        }

        @Override
        public void onBeforeEntityUpdate(WithEntity<String, Store> event) {
            handleEdit(event.getCrud().read(event.getEntity().getId()));
        }

        @Override
        public void onBeforeEntityDelete(WithId<String, Store> event) {
            handleEdit(event.getCrud().read(event.getEntityId()));
        }

        private void handleEdit(Store shop) {
            if(shop==null) {
                return;
            }
            Organisation organisation = shop.getOrganisation();
            Authentication auth = getAuthentication();
            if (!isAdministrator(auth)
                    && !isInUserList(auth, organisation.getUsers())) {
                throw new SecurityException("User is not administrator, not the creator and is not in shops userlist. Cannot edit store.");
            }
        }
    }

    public static class AccountSecurity extends BaseCrudListener<String, Account> {

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
        public void onBeforeEntityUpdate(WithEntity<String, Account> event) {
            handleEdit(event.getCrud().read(event.getEntity().getId()));
        }

        @Override
        public void onBeforeEntityDelete(WithId<String, Account> event) {
            handleEdit(event.getCrud().read(event.getEntityId()));
        }

        private void handleEdit(Account account) {
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

    public static class ProductSecurity extends BaseCrudListener<String, Product> {

        private final Organisation organsiation;

        public ProductSecurity(Organisation organisation) {
            if(organisation == null) {
                throw new NullPointerException("Shop must not be null.");
            }
            this.organsiation = organisation;
        }
        
        @Override
        public void onEntityRead(WithEntity<String, Product> event) {
            Product item = event.getEntity();
            if(!organsiation.getId().equals(item.getOrganisation().getId())) {
                throw new SecurityException("Not allowed to read products from other Shops.");
            }
        }

        @Override
        public void onBeforeEntityUpdate(WithEntity<String, Product> event) {
            handleEdit(event.getCrud().read(event.getEntity().getId()));
        }

        @Override
        public void onBeforeEntityDelete(WithId<String, Product> event) {
            handleEdit(event.getCrud().read(event.getEntityId()));
        }

        private void handleEdit(Product item) {
            if(item==null) {
                return;
            }
            if(!organsiation.getId().equals(item.getOrganisation().getId())) {
                throw new SecurityException("Now allowed to edit products from other Shops.");
            }
        }
    }

    public static class OrderSecurity extends BaseCrudListener<String, Order> {

        private final Organisation organisation;

        public OrderSecurity(Organisation organisation) {
            if(organisation == null) {
                throw new NullPointerException("Shop must not be null.");
            }
            this.organisation = organisation;
        }

        @Override
        public void onEntityRead(WithEntity<String, Order> event) {
            Order order = event.getEntity();
            if(!organisation.getId().equals(order.getOrganisation().getId())) {
                throw new SecurityException("Not allowed to read orders from other Shops.");
            }
        }

        @Override
        public void onBeforeEntityUpdate(WithEntity<String, Order> event) {
            handleEdit(event.getCrud().read(event.getEntity().getId()));
        }

        @Override
        public void onBeforeEntityDelete(WithId<String, Order> event) {
            handleEdit(event.getCrud().read(event.getEntityId()));
        }

        private void handleEdit(Order order) {
            if(order==null) {
                return;
            }
            if(!organisation.getId().equals(order.getOrganisation().getId())) {
                throw new SecurityException("Now allowed to edit orders from other Shops.");
            }
        }
    }

    public static class TaxSecurity extends BaseCrudListener<String, Tax> {

        private final Organisation organisation;

        public TaxSecurity(Organisation organisation) {
            if(organisation == null) {
                throw new NullPointerException("Shop must not be null.");
            }
            this.organisation = organisation;
        }

        @Override
        public void onEntityRead(WithEntity<String, Tax> event) {
            Tax tax = event.getEntity();
            if(!organisation.getId().equals(tax.getOrganisation().getId())) {
                throw new SecurityException("Now allowed to read taxes from other Shops.");
            }
        }

        @Override
        public void onBeforeEntityUpdate(WithEntity<String, Tax> event) {
            handleEdit(event.getCrud().read(event.getEntity().getId()));
        }

        @Override
        public void onBeforeEntityDelete(WithId<String, Tax> event) {
            handleEdit(event.getCrud().read(event.getEntityId()));
        }

        private void handleEdit(Tax tax) {
            if(!organisation.getId().equals(tax.getOrganisation().getId())) {
                throw new SecurityException("Now allowed to edit taxes from other Shops.");
            }
        }
    }

    public static class PaymentSecurity extends BaseCrudListener<String, Payment> {

        private final Organisation organisation;

        public PaymentSecurity(Organisation organisation) {
            if(organisation == null) {
                throw new NullPointerException("Shop must not be null.");
            }
            this.organisation = organisation;
        }

        @Override
        public void onEntityRead(WithEntity<String, Payment> event) {
            Payment payment = event.getEntity();
            if(!organisation.getId().equals(payment.getOrganisation().getId())) {
                throw new SecurityException("Now allowed to read taxes from other Shops.");
            }
        }

        @Override
        public void onBeforeEntityUpdate(WithEntity<String, Payment> event) {
            handleEdit(event.getCrud().read(event.getEntity().getId()));
        }

        @Override
        public void onBeforeEntityDelete(WithId<String, Payment> event) {
            handleEdit(event.getCrud().read(event.getEntityId()));
        }

        private void handleEdit(Payment payment) {
            if(!organisation.getId().equals(payment.getOrganisation().getId())) {
                throw new SecurityException("Now allowed to edit taxes from other Shops.");
            }
        }
    }
}
