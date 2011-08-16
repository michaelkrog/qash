package dk.apaq.shopsystem.model;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 * Defines a Template for a Website.
 */
@Entity
public class Template extends AbstractContentEntity implements Serializable {

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
