package dk.apaq.shopsystem.qash.common;

/**
 *
 * @author krog
 */
public class CategoryListPanel extends AbstractListPanel {
    
    public CategoryListPanel() {
        super(new CategoryList());
        setStyleName("v-categorylistpanel");
    }

    public void addCategory(String name) {
        ((CategoryList)list).addCategory(name);
    }
    
}
