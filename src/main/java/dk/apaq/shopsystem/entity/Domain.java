package dk.apaq.shopsystem.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Specifies a web domain for a website.
 */
@Entity
@Table(name="DomainModel")
public class Domain extends AbstractEntity implements Serializable {

    private String name;;

    private boolean isdefault;

    private String languageCode;

    private String currencyCode;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public boolean isDefault() {
        return isdefault;
    }

    public void setDefault(boolean value) {
        this.isdefault = value;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}