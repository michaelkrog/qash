package dk.apaq.shopsystem.ui.util;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import java.util.Iterator;

/**
 *
 * @author michaelzachariassenkrog
 */
public class SwitchButtonGroup extends CustomComponent {

    private HorizontalLayout layout = new HorizontalLayout();
    private ClickHandler clickHandler = new ClickHandler();

    private class ClickHandler implements Button.ClickListener {

        public void buttonClick(ClickEvent event) {
            for(int i=0;i<getButtonCount();i++) {
                Button button = getButton(i);
                setSelected(button, button  == event.getButton());
            }
        }

    }

    public SwitchButtonGroup() {
        setStyleName("v-switchbuttongroup");
        setCompositionRoot(layout);
        layout.setWidth(100, UNITS_PERCENTAGE);
    }

    public Button addButton(String caption) {
        return this.addButton(caption, null);
    }

    public Button addButton(String caption, Resource icon) {
        Button button = new Button(caption);
        button.setWidth(100, UNITS_PERCENTAGE);
        if(icon!=null) {
            button.setIcon(icon);
        }

        button.setStyleName("v-switchbutton");
        
        if(layout.getComponentCount()==0) {
            button.addStyleName("first");
            setSelected(button, true);
        } else {
            layout.getComponent(layout.getComponentCount()-1).removeStyleName("last");
        }

        button.addStyleName("last");
        button.addListener(clickHandler);
        layout.addComponent(button);
        return button;
    }

    public int getButtonCount() {
        return layout.getComponentCount();
    }

    public Button getButton(int index) {
        return (Button)layout.getComponent(index);
    }

    
    public void removeAllButtons() {
        layout.removeAllComponents();
    }

    private void setSelected(Button button, boolean selected) {
        if(selected) {
            button.addStyleName("down");
        } else {
            button.removeStyleName("down");
        }
     }

}
