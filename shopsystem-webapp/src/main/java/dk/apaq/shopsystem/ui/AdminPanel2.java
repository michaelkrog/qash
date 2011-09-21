/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.apaq.shopsystem.ui;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.shopsystem.ui.common.CommonDialog;

/**
 *
 * @author Martin Christensen
 */
public class AdminPanel2 extends CustomComponent {
    
    private HorizontalLayout innerLayout = new HorizontalLayout();
    private VerticalLayout outerLayout = new VerticalLayout();
    private VerticalLayout content = new VerticalLayout();
    private Accordion accordion = new Accordion();
    //private Panel tabContent = new Panel();
    
    
    public AdminPanel2() {
        
        this.outerLayout.setSizeFull();
        this.innerLayout.setSizeFull();
        this.content.setSizeFull();
        this.accordion.setSizeFull();
        
        this.accordion.setStyleName("v-accordion-borderless");
        
        // Navigation contents
        Panel tab1 = new Panel();
        tab1.addComponent(OpenInContent("Stores",new StoreList(), false));
        tab1.addComponent(OpenInDialog("Add new store", new StoreEdit(), true));
        
        tab1.addComponent(OpenInContent("Users",new UserList(), false));
        tab1.addComponent(OpenInDialog("Add new user", new UserEdit(), true));
        
        tab1.addComponent(OpenInContent("Themes",new ThemeList(), false));
        tab1.addComponent(OpenInDialog("Add new theme", new ThemeEdit(), false));
        this.accordion.addTab(tab1, "Settings", null);
        
        Panel tab2 = new Panel();
        tab2.addComponent(OpenInContent("Products",new ConstructionList(), false));
        tab2.addComponent(OpenInDialog("Add new product", new ConstructionForm(), false));
        this.accordion.addTab(tab2, "Products", null);
        
        Panel tab3 = new Panel();
        //tab3.addComponent(OpenInContent("ConstructionList",new ConstructionList()));
        //tab3.addComponent(OpenInContent("ConstructionForm",new ConstructionForm("1")));
        this.accordion.addTab(tab3, "Newsletters", null);
        // ***
        
        
        this.innerLayout.addComponent(this.accordion);
        
        this.innerLayout.addComponent(content);
        this.innerLayout.getComponent(0).setWidth("200px");
        this.innerLayout.setExpandRatio(this.content, 1.0f);
        
        this.outerLayout.addComponent(new SimpleSiteHeader());
        this.outerLayout.addComponent(innerLayout);
        
        setCompositionRoot(this.outerLayout);
    }    
    

    public Button OpenInContent(final String buttonText, final Component target, Boolean buttonMargin) {
       
        Button button = new Button(" " + buttonText);
        button.setStyleName(Reindeer.BUTTON_LINK);
        button.addStyleName("v-accordion-button");
        button.setIcon(new ThemeResource("../shopsystem/icons/7/dot.png"));
        button.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                    content.removeAllComponents();
                    content.addComponent(target);
            }
        });
        
        if (buttonMargin == true) {
            button.addStyleName("v-accordion-button-margin");
        }
        
        return button;
    }
    
    
    public Button OpenInDialog(final String buttonText, final Component target, Boolean buttonMargin) {
        
        Button button = new Button(" " + buttonText);
        button.setStyleName(Reindeer.BUTTON_LINK);
        button.addStyleName("v-accordion-button");
        button.setIcon(new ThemeResource("../shopsystem/icons/7/dot.png"));
        button.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                CommonDialog dialog = new CommonDialog(buttonText, target);
                getApplication().getMainWindow().addWindow(dialog);
            }
        });
               
        if (buttonMargin == true) {
            button.addStyleName("v-accordion-button-margin");
        }
        
        return button;
        
        
    }
    
}
    
    

