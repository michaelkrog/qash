package dk.apaq.shopsystem.entity;

import dk.apaq.crud.HasId;
import java.io.Serializable;
import javax.persistence.Entity;

/**
 * A tax that is imposed sales.
 * @author michael
 *
 */
@Entity
public class Tax extends AbstractContentEntity implements Serializable {

    private String name;
    private double rate;
    private boolean defaultEnabled = false;

    public Tax() {
    }

    public Tax(String name, double rate) {
        this.name = name;
        this.rate = rate;
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
