/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.apaq.shopsystem.ui;

import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Martin Christensen
 */
public class AdminPanel2 extends CustomComponent {
    
    private HorizontalLayout innerLayout = new HorizontalLayout();
    private VerticalLayout outerLayout = new VerticalLayout();
    final private VerticalLayout content = new VerticalLayout();
    private Accordion accordion = new Accordion();
    //private Panel tabContent = new Panel();
    
    
    public AdminPanel2() {
        
        this.outerLayout.setSizeFull();
        this.innerLayout.setSizeFull();
        this.content.setSizeFull();
        this.accordion.setSizeFull();
        
        Panel tab1 = new Panel();
        tab1.addComponent(OpenInContent("ConstructionList",new ConstructionList()));
        tab1.addComponent(OpenInContent("ConstructionForm",new ConstructionForm("1")));
        this.accordion.addTab(tab1, "Indstillinger", null);
        
        Panel tab2 = new Panel();
        tab2.addComponent(OpenInContent("ConstructionList",new ConstructionList()));
        tab2.addComponent(OpenInContent("ConstructionForm",new ConstructionForm("1")));
        this.accordion.addTab(tab2, "Varer", null);
        
        Panel tab3 = new Panel();
        tab3.addComponent(OpenInContent("ConstructionList",new ConstructionList()));
        tab3.addComponent(OpenInContent("ConstructionForm",new ConstructionForm("1")));
        this.accordion.addTab(tab3, "Nyhedsbreve", null);

        // A container for the this.accordion.
        Panel panel = new Panel();
        panel.setSizeFull();
        //panel.setWidth("200px");
        //panel.setWidth("200px");
        
        panel.addComponent(this.accordion);

        // Trim its layout to allow the Accordion take all space.
        panel.getLayout().setSizeFull();
        panel.getLayout().setMargin(false);
        
        this.innerLayout.addComponent(panel);
        this.innerLayout.addComponent(content);
        this.innerLayout.getComponent(0).setWidth("200px");
        this.innerLayout.setExpandRatio(panel, 1.0f);
        
        this.outerLayout.addComponent(new SimpleSiteHeader());
        this.outerLayout.addComponent(innerLayout);
        
        setCompositionRoot(this.outerLayout);
    }    
    

    public Button OpenInContent(final String text, Object target) {
        
        //final Panel button = new Panel();

        final NativeButton button = new NativeButton();
        button.setCaption(text);
        button.setStyleName("v-button-text-only");
        button.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                //button.setCaption("You pushed: " + text);
                //this.OpenInDialog();
                
            }
        });
        

        this.content.removeAllComponents();
        //this.content.addComponent(tab3);
        
        return button;
    }
    
    
    public void openInDialog() {
    
    }
    
}
    
    

