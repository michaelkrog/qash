package dk.apaq.shopsystem.entity;

import dk.apaq.shopsystem.entity.Company;
import dk.apaq.shopsystem.entity.ContentEntity;
import dk.apaq.shopsystem.entity.Organisation;
import java.util.Date;
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
public class CustomerRelationship implements ContentEntity, HasContactInformation {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateChanged = new Date();

    @ManyToOne
    private Organisation organisation;
    
    @ManyToOne
    private Organisation customer;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date date) {
        this.dateCreated = date;
    }

    public Date getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    @Override
    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Organisation getCustomer() {
        return customer;
    }

    public void setCustomer(Organisation customer) {
        this.customer = customer;
    }

    @Override
    public String getCompanyName() {
        return customer == null ? null : customer.getCompanyName();
    }

    @Override
    public String getContactName() {
        return customer == null ? null : customer.getContactName();
    }

    @Override
    public String getEmail() {
        return customer == null ? null : customer.getEmail();
    }

    @Override
    public String getTelephone() {
        return customer == null ? null : customer.getTelephone();
    }

    @Override
    public String getCity() {
        return customer == null ? null : customer.getCity();
    }

    @Override
    public String getCountryCode() {
        return customer == null ? null : customer.getCountryCode();
    }

    @Override
    public String getPostalCode() {
        return customer == null ? null : customer.getPostalCode();
    }

    @Override
    public String getStateOrProvince() {
        return customer == null ? null : customer.getStateOrProvince();
    }

    @Override
    public String getStreet() {
        return customer == null ? null : customer.getStreet();
    }

    @Override
    public void setCity(String city) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCountryCode(String countryCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPostalCode(String postalCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setStateOrProvince(String stateOrProvince) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setStreet(String street) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    
}
