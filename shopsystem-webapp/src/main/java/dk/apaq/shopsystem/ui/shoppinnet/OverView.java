package dk.apaq.shopsystem.ui.shoppinnet;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class Overview extends CustomComponent {
    
    private HorizontalLayout content = new HorizontalLayout();
    private VerticalLayout contentLeft = new VerticalLayout();
    private VerticalLayout contentRight = new VerticalLayout();
    private Panel box = new Panel();
    
    
    @Override
    public void attach() {

        this.content.setWidth("100%");
        this.contentLeft.setWidth("100%");
        this.contentRight.setWidth("100%");
        this.box.setWidth("100%");
                
        // Left content
        
        this.box.setCaption("Test 1");
        this.box.removeAllComponents();
        this.box.addComponent(new Label("A description..."));
        this.contentLeft.addComponent(this.box);
        
        this.box.setCaption("Test 2");
        this.box.removeAllComponents();
        this.box.addComponent(new Label("Another description..."));
        this.contentLeft.addComponent(this.box);
        
        // Right content
        
        this.box.setCaption("Test 3");
        this.box.removeAllComponents();
        this.box.addComponent(new Label("Another other description..."));
        this.contentRight.addComponent(this.box);
        
        this.box.setCaption("Test 4");
        this.box.removeAllComponents();
        this.box.addComponent(new Label("Another other other description..."));
        this.contentRight.addComponent(this.box);
        
        
        this.content.addComponent(this.contentLeft);
        this.content.addComponent(this.contentRight);
        
        setCompositionRoot(this.content);
    }
    
}
