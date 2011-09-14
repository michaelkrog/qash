package dk.apaq.shopsystem.entity;

import java.io.Serializable;

/**
 * Defines a Template for a Website.
 */
public class Template implements Serializable {

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
