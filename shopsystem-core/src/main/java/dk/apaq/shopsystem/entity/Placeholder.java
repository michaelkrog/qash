package dk.apaq.shopsystem.entity;

import java.io.Serializable;

/**
 *
 * @author krog
 */
public class Placeholder implements Serializable {
    
    private final String id;
    private final boolean container;

    public Placeholder(String id, boolean container) {
        this.id = id;
        this.container = container;
    }

    public String getId() {
        return id;
    }

    public boolean isContainer() {
        return container;
    }
    
}
