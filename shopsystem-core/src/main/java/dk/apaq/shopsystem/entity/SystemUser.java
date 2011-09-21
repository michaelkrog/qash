package dk.apaq.shopsystem.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Defines a System User. A SystemUser is unique across the system, but can be
 * owned by a specific Organsiation.
 * 
 * @author michael
 */
@Entity
public class SystemUser extends BaseUser implements dk.apaq.crud.HasId<String> {

    private String displayname;
    private String email;
    private boolean emailVerified = false;
    private String name;
    private String password;
    private boolean locked;

    @Override
    public String getDisplayname() {
        return displayname;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEmailVerified() {
        return emailVerified;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
}
