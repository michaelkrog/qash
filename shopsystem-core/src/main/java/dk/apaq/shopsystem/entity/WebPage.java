package dk.apaq.shopsystem.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 */
@Entity
public class WebPage extends AbstractDocument implements WebContentEntity {

    @ManyToOne
    private Website website;
    
    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }
}
