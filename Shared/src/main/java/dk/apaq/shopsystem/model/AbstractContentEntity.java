package dk.apaq.shopsystem.model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author michaelzachariassenkrog
 */
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class AbstractContentEntity extends AbstractEntity {

    private Organisation organisation;

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }
    
}
