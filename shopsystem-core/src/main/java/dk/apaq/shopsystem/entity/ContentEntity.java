package dk.apaq.shopsystem.entity;

/**
 *
 * @author krog
 */
public interface ContentEntity extends BasicEntity {

    Organisation getOrganisation();

    void setOrganisation(Organisation organisation);

}
