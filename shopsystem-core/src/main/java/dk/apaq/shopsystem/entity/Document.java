package dk.apaq.shopsystem.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author michael
 */
@Entity
public class Document extends AbstractDocument implements ContentEntity {
    

    @ManyToOne
    private Organisation organisation;
    

    public Document() {
    }

    @Override
    public Organisation getOrganisation() {
        return organisation;
    }

    @Override
    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }
    
}
