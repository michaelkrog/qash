package dk.apaq.shopsystem.ui.common;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates a Grid of categorized Buttons. Each button is linked to a component that will be shown in place of the
 * grid when clicked.
 *
 * This is a layout similar to the one used by Gnome 3 and Mac OS for System Settings.
 * @author michaelzachariassenkrog
 */
public class CategoryGridPanel extends CustomComponent  {

    private final VerticalLayout layout = new VerticalLayout();
    private final VerticalLayout outerIconLayout = new VerticalLayout();

    private final Map<Component, Entry> entryMap = new HashMap<Component, Entry>();
    private final Map<Category, Panel> categoryMap = new HashMap<Category, Panel>();
    private final IconClickHandler clickHandler = new IconClickHandler();

    /**
     * Category used within the panel.
     */
    public class Category {
        private final String name;

        private Category(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * Handler for Icon Clicks
     */
    private class IconClickHandler implements Button.ClickListener {

        @Override
        public void buttonClick(ClickEvent event) {
            Button button = event.getButton();
            Entry entry = (Entry) button.getData();
            showComponent(entry.getComponent());
        }

    }

    /**
     * An entry in the Grid.
     */
    private class Entry {
        private final Resource resource;
        private final String text;
        private final Component component;

        public Entry(Resource resource, String text, Component component) {
            this.resource = resource;
            this.text = text;
            this.component = component;
        }

        public Component getComponent() {
            return component;
        }

        public Resource getResource() {
            return resource;
        }

        public String getText() {
            return text;
        }
    }

    /**
     * Creates a new instance of the Grid.
     */
    public CategoryGridPanel() {
        outerIconLayout.setWidth(100, UNITS_PERCENTAGE);
        layout.setSizeFull();
        layout.addComponent(outerIconLayout);
        setCompositionRoot(layout);
        setStyleName("v-categorygridpanel");
    }

    /**
     * Adds a new Category.
     * @param name The name of the Category.
     * @return the new Category.
     */
    public Category addCategory(String name) {
        Category cat = new Category(name);
        Panel panel = createPanel(name, outerIconLayout.getComponentCount() % 2 == 1);
        outerIconLayout.addComponent(panel);
        panel.setWidth(100, UNITS_PERCENTAGE);
        panel.setContent(new HorizontalLayout());
        categoryMap.put(cat, panel);
        return cat;
    }

    /**
     * Adds a new entry.
     * @param category The category to place the button within.
     * @param resource The resource for the icon.
     * @param text The text for the icon.
     * @param component The component to link the button with.
     */
    public void addComponent(Category category, Resource resource, String text, Component component) {
        Panel panel = categoryMap.get(category);
        Entry entry = new Entry(resource, text, component);
        //Icon icon = new Icon(entry);
        Button button = new Button(entry.getText());
        button.setStyleName("CategoryGrid");
        button.setIcon(entry.getResource());
        button.setData(entry);
        button.addListener(this.clickHandler);
        panel.addComponent(button);
        entryMap.put(component, entry);
    }

    /**
     * Removes an entry.
     * @param component
     */
    public void remove(Component component) {
        entryMap.remove(component);
    }


    private Panel createPanel(String caption, boolean odd) {
        Panel panel = new Panel(caption);
        panel.setWidth(100, UNITS_PERCENTAGE);
        panel.setContent(new HorizontalLayout());
        if (odd) {
            panel.setStyleName("CategoryGridRow-odd");
        } else {
            panel.setStyleName("CategoryGridRow");
        }
        return panel;
    }

    private void showComponent(Component component) {
        layout.removeAllComponents();
        layout.addComponent(component);
    }

    /**
     * Programaticly force the Panel to show the Grid of buttons instead of a selected component.
     */
    public void showGrid() {
        if(layout.getComponent(0) == outerIconLayout) {
            return;
        }
        layout.removeAllComponents();
        layout.addComponent(outerIconLayout);
    }


}
