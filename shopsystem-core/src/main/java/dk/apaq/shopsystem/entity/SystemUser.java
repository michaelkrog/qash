package dk.apaq.shopsystem.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Temporal;

/**
 * Defines a System User. A SystemUser is unique across the system, but can be
 * owned by a specific Organsiation.
 * 
 * @author michael
 */
@Entity
public class SystemUser extends BaseUser {

    private String displayname;
    private String email;
    private boolean emailVerified = false;
    private String name;
    private String password;
    private boolean locked;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date birthDay = new Date();
    
    private String phone;

    

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

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public String getTelephone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
}
