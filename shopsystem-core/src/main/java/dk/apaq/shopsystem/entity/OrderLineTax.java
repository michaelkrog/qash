package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 * A tax specified on an OrderLine.
 * @author michael
 */
@Entity
public class OrderLineTax implements Serializable, BasicEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
    protected String id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateChanged = new Date();

    private String referenceId;
    private String name;
    private double rate;

    public OrderLineTax() {
    }

    public OrderLineTax(String referenceId, String name, double rate) {
        this.referenceId = referenceId;
        this.name = name;
        this.rate = rate;
    }

    public OrderLineTax(Tax tax) {
        this(tax.getId(), tax.getName(), tax.getRate());
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
    
    public String getName() {
        return name;
    }

    public double getRate() {
        return rate;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OrderLineTax other = (OrderLineTax) obj;
        if ((this.referenceId == null) ? (other.referenceId != null) : !this.referenceId.equals(other.referenceId)) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.referenceId != null ? this.referenceId.hashCode() : 0);
        hash = 31 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 31 * hash + (int) (this.rate);
        return hash;
    }
}
