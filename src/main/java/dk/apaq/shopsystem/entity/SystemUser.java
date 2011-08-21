package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Defines a System User. A System User does not belong to any organisation, but belongs
 * to the system and can be used by any organisation in the system.
 * 
 * Example:
 * - System Administrators should be able to gain access to any organisation in the system.
 * - Super Users might maintain several Organisations.
 * - Simple users just access their own organisation.
 * 
 * @author michael
 */
@Entity
public class SystemUser extends AbstractEntity implements Principal, dk.apaq.crud.HasId<String>, Serializable {

    private String displayname;

    private String email;

    private boolean emailVerified=false;

    private String name;

    private String password;

    @ElementCollection(fetch=FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<String> identifiers = new ArrayList<String>();
    
    @ElementCollection(fetch=FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<String> roles = new ArrayList<String>();

    private boolean locked;

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public List<String> getRoles() {
        return roles;
    }
}
