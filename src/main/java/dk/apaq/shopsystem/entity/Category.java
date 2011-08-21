package dk.apaq.shopsystem.entity;

import java.io.Serializable;

import javax.persistence.Entity;

/**
 * Specifies a category for a product.
 */
@Entity
public class Category extends AbstractContentEntity implements Serializable{

    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
