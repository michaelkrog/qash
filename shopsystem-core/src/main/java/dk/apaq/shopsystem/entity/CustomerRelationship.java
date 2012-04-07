package dk.apaq.shopsystem.entity;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author krog
 */
@Entity
public class CustomerRelationship implements ContentEntity/*, HasContactInformation*/ {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateChanged = new Date();

    @NotNull
    @ManyToOne
    private Organisation organisation;
    
    @NotNull
    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private Organisation customer;
    
    private String subscriptionPaymentId;
    
    private boolean editable;
    
    public CustomerRelationship() {
    }

    public CustomerRelationship(Organisation customer) {
        this.customer = customer;
    }
    
    
    
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

       
    public String getSubscriptionPaymentId() {
        return subscriptionPaymentId;
    }

    public void setSubscriptionPaymentId(String subscriptionPaymentId) {
        this.subscriptionPaymentId = subscriptionPaymentId;
    }

    /**
     * Wether the customer should be allowed to edit the customer or not.
     * Only customers created by the organisation should be allowed to be edited by the organisation
     */
    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    
    
}
