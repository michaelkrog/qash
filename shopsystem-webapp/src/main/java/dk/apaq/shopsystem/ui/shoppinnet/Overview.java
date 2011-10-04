package dk.apaq.shopsystem.ui.shoppinnet;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.ui.shoppinnet.common.PageHeader;

public class Overview extends CustomComponent {
    
    private VerticalLayout pageHolder = new VerticalLayout();
    private HorizontalLayout content = new HorizontalLayout();
    private VerticalLayout contentLeft = new VerticalLayout();
    private VerticalLayout contentRight = new VerticalLayout();
    //private Label spacer = new Label();
    
    
    @Override
    public void attach() {

        setCompositionRoot(this.pageHolder);
        
        // Clear all
        this.pageHolder.removeAllComponents();
        this.content.removeAllComponents();
        this.contentLeft.removeAllComponents();
        this.contentRight.removeAllComponents();
        
        
        this.pageHolder.addComponent(new PageHeader("Overview"));
        this.pageHolder.addComponent(this.content);
        this.pageHolder.setSpacing(true);
        
        this.content.addComponent(this.contentLeft);
        this.content.addComponent(this.contentRight);
        
        this.content.setSpacing(true);
        this.contentLeft.setSpacing(true);
        this.contentRight.setSpacing(true);
        this.content.setWidth("100%");
        this.contentLeft.setWidth("100%");
        this.contentRight.setWidth("100%");
        
        // Left content
        
        Panel box1 = new Panel("Test 1");
        box1.setStyleName("v-box");
        box1.addComponent(new Label("A description..."));
        this.contentLeft.addComponent(box1);
        
        Panel box2 = new Panel("Test 2");
        box2.setStyleName("v-box");
        box2.addComponent(new Label("A description..."));
        this.contentLeft.addComponent(box2);
        
        // Right content
        
        Panel box3 = new Panel("Test 3");
        box3.setStyleName("v-box");
        box3.addComponent(new Label("A description..."));
        this.contentRight.addComponent(box3);
        
        Panel box4 = new Panel("Test 4");
        box4.setStyleName("v-box");
        box4.addComponent(new Label("A description..."));
        this.contentRight.addComponent(box4);

    }
    
}