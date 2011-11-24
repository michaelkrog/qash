package dk.apaq.shopsystem.entity;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author krog
 */
public interface ContentEntity extends BasicEntity {

    @JsonIgnore(true)
    Organisation getOrganisation();

    void setOrganisation(Organisation organisation);

}
