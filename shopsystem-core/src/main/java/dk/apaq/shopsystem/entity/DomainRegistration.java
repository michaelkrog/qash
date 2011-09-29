package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 */
@Entity
public class DomainRegistration implements Serializable, BasicEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
    private String id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateChanged = new Date();
    @ManyToOne
    private Domain domain;
    private String subDomain;
    private String languageCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public void setDateCreated(Date date) {
        this.dateCreated = date;
    }

    @Override
    public Date getDateChanged() {
        return dateChanged;
    }

    @Override
    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }
}
