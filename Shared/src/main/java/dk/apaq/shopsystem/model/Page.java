package dk.apaq.shopsystem.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author michaelzachariassenkrog
 */
@Entity
public class Page extends AbstractOrganisationEntity {

    private String name;
    private String title;
    private String description;
    private String keywords;
    @ManyToOne
    private Website website;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    
}
