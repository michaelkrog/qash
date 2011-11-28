package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author krog
 */
@Entity
public class Customer extends AbstractCompany implements Serializable, ContentEntity {

    @ManyToOne
    private Organisation organisation;
    
    private String username;
    private String password;
    private boolean emailVerified;
    private boolean loginAllowed=true;
    private boolean enabled = true;
    
    @Column(length=4096)
    private String notes;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date birthDay = new Date();
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    
    @Override
    public Organisation getOrganisation() {
        return organisation;
    }

    @Override
    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isLocked() {
        return !loginAllowed;
    }

    public void setLoginAllowed(boolean loginAllowed) {
        this.loginAllowed = loginAllowed;
    }

    public boolean isLoginAllowed() {
        return loginAllowed;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    
    
}
