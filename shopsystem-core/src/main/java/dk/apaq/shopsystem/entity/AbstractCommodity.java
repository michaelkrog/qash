package dk.apaq.shopsystem.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

/**
 *
 * @author michael
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractCommodity implements Commodity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateChanged = new Date();
    @ManyToOne
    private Organisation organisation;
    private String name;
    private String itemNo;
    private String barcode;
    private boolean enabled = true;
    
    @OneToMany(cascade={CascadeType.ALL})
    List<PriceTag> prices = new ArrayList<PriceTag>();
    
    @ManyToOne
    private Tax tax = null;

    @Override
    public String getBarcode() {
        return barcode;
    }

    public Date getDateChanged() {
        return dateChanged;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getItemNo() {
        return itemNo;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Organisation getOrganisation() {
        return organisation;
    }

    @Override
    public Tax getTax() {
        return tax;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    public void setDateCreated(Date date) {
        this.dateCreated = date;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    @Override
    public void setTax(Tax tax) {
        this.tax = tax;
    }

    @Override
    public List<PriceTag> getPriceTags() {
        return prices;
    }

    @Override
    public boolean hasPriceInCurrency(String currencyCode) {
        return getPriceForCurrency(currencyCode) != null;
    }

    @Override
    public Money getPriceForCurrency(String currencyCode) {
        for (PriceTag tag : getPriceTags()) {
            if (tag.getMoney().getCurrencyUnit().getCurrencyCode().equals(currencyCode)) {
                return tag.getMoney();
            }
        }
        return null;
    }
    
    
}
