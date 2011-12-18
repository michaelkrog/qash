package dk.apaq.shopsystem.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author michael
 */
@Entity
public class OrganisationUserReference implements ContentEntity, User {
 
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable=false)
    private Date dateCreated = new Date();

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable=false)
    private Date dateChanged = new Date();
    
    @ManyToOne(optional=false)
    private Organisation organisation;
    
    @ManyToOne(optional=false)
    private SystemUser user; 
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<String> roles = new ArrayList<String>();

    public OrganisationUserReference() {
    }

    public OrganisationUserReference(SystemUser systemUser) {
        this.user = systemUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public SystemUser getUser() {
        return user;
    }

    public void setUser(SystemUser user) {
        this.user = user;
    }

    @Override
    public String getDisplayName() {
        return user.getDisplayName();
    }

    public void setDisplayName(String displayName) {
        user.setDisplayName(displayName);
    }
    
    @Override
    public String getEmail() {
        return user.getEmail();
    }
    
    public void setEmail(String email) {
        user.setEmail(email);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }
    
    public void setPasswod(String password) {
        user.setPassword(password);
    }

    @Override
    public boolean isEmailVerified() {
        return user.isEmailVerified();
    }
    
    @Override
    public boolean isLocked() {
        return user.isLocked();
    }

    @Override
    public String getTelephone() {
        return user.getTelephone();
    }
    
    public void setTelephone(String telephone) {
        user.setTelephone(telephone);
    }

    @Override
    public String getName() {
        return user.getName();
    }
    
    public void setName(String name) {
        user.setName(name);
    }

    
}
