package dk.apaq.shopsystem.model;

/**
 *
 * @author michaelzachariassenkrog
 */
public class Template extends AbstractOrganisationEntity {

    private String name;
    private String styles;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyles() {
        return styles;
    }

    public void setStyles(String styles) {
        this.styles = styles;
    }

    
}
