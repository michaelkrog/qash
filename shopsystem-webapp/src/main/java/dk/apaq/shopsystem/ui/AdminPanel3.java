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
import dk.apaq.shopsystem.ui.qash.settings.SettingsDialog;

/**
 *
 * @author Martin Christensen
 */
public class AdminPanel3 extends CustomComponent {
    
    private HorizontalLayout innerLayout = new HorizontalLayout();
    private VerticalLayout outerLayout = new VerticalLayout();
    private VerticalLayout content = new VerticalLayout();
    private VerticalLayout navigationHolder = new VerticalLayout();
    private VerticalLayout navigation = new VerticalLayout();
    private VerticalLayout dummy = new VerticalLayout();
    
    // Temp company button
    private Button linkOptions = new Button("Company options");
    private final SettingsDialog settingsDialog = new SettingsDialog();
    
    
    public AdminPanel3() {
        
        // Setup layout
        //this.outerLayout.setHeight(this.outerLayout.getApplication().getWindow("application").getHeight());
        //this.outerLayout.setWidth("100%");
        //this.outerLayout.setHeight("100%");
        //this.outerLayout.setStyleName("v-layout-inner");
        //this.innerLayout.setSizeFull();
        this.innerLayout.setWidth("100%");
        this.innerLayout.setHeight("100%");
        //this.innerLayout.setStyleName("v-layout-inner");
        
        this.content.setWidth("100%");
        this.content.setHeight("100%");
        this.navigation.setWidth("195px");
        this.navigation.setHeight("100%");
        this.navigationHolder.setWidth("100%");
        this.navigationHolder.setHeight("100%");
      
        // Create navigation panel
        this.navigation.addComponent(linkOptions);
        linkOptions.addListener(new Button.ClickListener() {

            public void buttonClick(Button.ClickEvent event) {
                settingsDialog.center();
                getApplication().getMainWindow().addWindow(settingsDialog);
            }
        });
        
        this.navigation.setStyleName("v-navigationpanel");
        this.navigation.addComponent(OpenInDialog("Settings",new SettingsDialog()));
        this.navigation.addComponent(OpenInContent("Stores",new StoreList()));
        this.navigation.addComponent(OpenInContent("Products",new StoreList()));
        this.navigation.addComponent(OpenInContent("Customers",new StoreList()));
        this.navigation.addComponent(OpenInContent("Marketing",new StoreList()));
        this.navigation.addComponent(OpenInContent("Import/Export",new StoreList()));
        this.navigation.addComponent(OpenInContent("Stats",new StoreList()));
        this.navigation.addComponent(OpenInContent("File archive",new StoreList()));
        
        // Insert navigation panel and content into layout
        //this.navigationHolder.addComponent(this.navigation);
        //this.navigationHolder.addComponent(this.dummy);
        //this.navigationHolder.setExpandRatio(this.dummy, 1.0f);
        this.innerLayout.addComponent(this.navigation);
        
        this.innerLayout.addComponent(content);
        //this.innerLayout.getComponent(0).setWidth("200px");
        //this.innerLayout.getComponent(0).setHeight("100%");
        this.innerLayout.setExpandRatio(this.content, 1.0f);
        
        //this.outerLayout.addComponent(innerLayout);
        
        setCompositionRoot(this.innerLayout);
    }    
    

    public Button OpenInContent(final String buttonText, final Component target) {
       
        Button button = new Button(buttonText);
        button.setWidth("100%");
        button.setStyleName(Reindeer.BUTTON_LINK);
        button.addStyleName("v-navigationpanel-button");
        //button.setIcon(new ThemeResource("../shopsystem/icons/7/dot.png"));
        button.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                    content.removeAllComponents();
                    content.addComponent(target);
            }
        });
        
        return button;
    }
    
    
    public Button OpenInDialog(final String buttonText, final Component target) {
        
        Button button = new Button(buttonText);
        button.setStyleName(Reindeer.BUTTON_LINK);
        button.addStyleName("v-navigationpanel-button");
        //button.setIcon(new ThemeResource("../shopsystem/icons/7/dot.png"));
        button.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                CommonDialog dialog = new CommonDialog(buttonText, target);
                getApplication().getMainWindow().addWindow(dialog);
            }
        });
        
        return button;
        
        
    }
    
}
    
    


