/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.ui.util;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author michaelzachariassenkrog
 */
public class CategoryGridPanel extends CustomComponent  {

    private final VerticalLayout layout = new VerticalLayout();
    private final VerticalLayout outerIconLayout = new VerticalLayout();

    private final Map<Component, Entry> entryMap = new HashMap<Component, Entry>();
    private final Map<Category, Panel> categoryMap = new HashMap<Category, Panel>();
    private final IconClickHandler clickHandler = new IconClickHandler();

    public class Category {
        private final String name;

        private Category(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private class IconClickHandler implements Button.ClickListener {

        public void buttonClick(ClickEvent event) {
            Button button = event.getButton();
            Entry entry = (Entry) button.getData();
            showComponent(entry.getComponent());
        }

    }

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

    public CategoryGridPanel() {
        outerIconLayout.setWidth(100, UNITS_PERCENTAGE);
        layout.setSizeFull();
        layout.addComponent(outerIconLayout);
        setCompositionRoot(layout);
        setStyleName("v-categorygridpanel");
    }

    public Category addCategory(String name) {
        Category cat = new Category(name);
        Panel panel = createPanel(name, outerIconLayout.getComponentCount() % 2 == 1);
        outerIconLayout.addComponent(panel);
        panel.setWidth(100, UNITS_PERCENTAGE);
        panel.setContent(new HorizontalLayout());
        categoryMap.put(cat, panel);
        return cat;
    }
    
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

    public void showGrid() {
        if(layout.getComponent(0) == outerIconLayout) {
            return;
        }
        layout.removeAllComponents();
        layout.addComponent(outerIconLayout);
    }


}
