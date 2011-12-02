package dk.apaq.shopsystem.qash.common;

import com.vaadin.ui.Label;

/**
 * A list of categorized items. This can be made to imitate the look of the iTunes navigation bar.
 * @author michael
 */
public class CategoryList extends List {

    public CategoryList() {
        super();
        
        
        setStyleName("v-categorylist");
    }

    
    /**
     * Adds a new category.
     * @param name The name of the category.
     */
    public void addCategory(String name) {
        Label label = new Label(name);
        layout.addComponent(label);
    }

    
}
