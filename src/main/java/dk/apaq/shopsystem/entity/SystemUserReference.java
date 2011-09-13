package dk.apaq.shopsystem.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Defines an Organisations reference to a SystemUser which it does not own. It is used when
 * an existing user needs access to the organisation. This reference describes the roles the
 * user has for this reference.
 */
@Entity
public class SystemUserReference extends AbstractUser {

    @OneToOne
    private SystemUser user;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<String> roles = new ArrayList<String>();

    @Override
    public List<String> getRoles() {
        return roles;
    }

    public SystemUser getUser() {
        return user;
    }

    public void setUser(SystemUser user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public String getDisplayname() {
        return user == null ? null : user.getDisplayname();
    }

    @Override
    public String getEmail() {
        return user == null ? null : user.getEmail();
    }

    @Override
    public String getPassword() {
        return user == null ? null : user.getPassword();
    }

    @Override
    public boolean isEmailVerified() {
        return user == null ? false : user.isEmailVerified();
    }

    @Override
    public boolean isLocked() {
        return user == null ? false : user.isLocked();
    }
}
