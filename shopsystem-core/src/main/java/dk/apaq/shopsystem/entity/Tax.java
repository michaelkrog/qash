package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;

/**
 * A tax that is imposed sales.
 * @author michael
 *
 */
@Entity
public class Tax implements Serializable, ContentEntity {

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

    private String name;
    private double rate;
    private boolean defaultEnabled = false;

    public Tax() {
    }

    public Tax(String name, double rate) {
        this.name = name;
        this.rate = rate;
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

    /**
     * Wether this is intended to be enabled by default. Most countries have a default tax which
     * are mostly used. This or these should be enabled by default.
     * @return Wether the tax is enabled by default.
     */
    public boolean isDefaultEnabled() {
        return defaultEnabled;
    }

    public void setDefaultEnabled(boolean defaultEnabled) {
        this.defaultEnabled = defaultEnabled;
    }

    /**
     * The percentage that applies to this tax. fx. 25.0 for 25%
     * @return
     */
    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * Reteives the name of this tax.
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !( obj instanceof Tax )) {
            return false;
        }

        Tax other = (Tax) obj;
        return niceEquals(id, other.id)
                && niceEquals(name, other.name)
                && rate == other.rate;

    }

    private boolean niceEquals(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        }

        if (obj1 == null) {
            return false;
        }

        return obj1.equals(obj2);
    }
}
