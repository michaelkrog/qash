package dk.apaq.shopsystem.qash.common;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author krog
 */
public class CategoryListPanel extends CustomComponent {
    
    private CategoryList categoryList = new CategoryList();
    private VerticalLayout content = new VerticalLayout();
    private HorizontalSplitPanel mainLayout = new HorizontalSplitPanel();
    private Map<String, Component> componentMap = new HashMap<String, Component>();
    private final ListListener listListener = new ListListener();

    private class ListListener implements CategoryList.SelectListener {

        @Override
        public void onSelect(CategoryList.SelectEvent event) {
            String selection = event.getSelection();
            setContent(selection);
        }
    }
    
    public CategoryListPanel() {
        setStyleName("v-categorylistpanel");
        
        categoryList.setSizeFull();
        categoryList.addListener(listListener);
        
        content.setSizeFull();
        
        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        mainLayout.setStyleName(Reindeer.SPLITPANEL_SMALL);
        
        mainLayout.addComponent(categoryList);
        mainLayout.addComponent(content);
        mainLayout.setSplitPosition(147, Component.UNITS_PIXELS);

        setCompositionRoot(mainLayout);
    }

    public void addCategory(String name) {
        categoryList.addCategory(name);
    }
    
    public void addItem(String name, String caption, Component component) {
        categoryList.addItem(caption, name);
        componentMap.put(name, component);
    }
    
    public void setSplitPosition(int position, int unit) {
        mainLayout.setSplitPosition(position, unit);
    }
    
    private void setContent(String name) {

        Component c = componentMap.get(name);

        if (c == null) {
            c = new Label("Ingen widget til dette område endnu");
        }

        content.removeAllComponents();
        content.addComponent(c);
    }
    
    public void select(String name) {
        categoryList.select(name);
    }
}
