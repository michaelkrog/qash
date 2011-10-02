package dk.apaq.shopsystem.ui.shoppinnet;


import dk.apaq.shopsystem.ui.shoppinnet.factory.WebsiteFactory;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.shopsystem.service.OrganisationService;


public class AdminPanel extends CustomComponent {
    
    private HorizontalLayout innerLayout = new HorizontalLayout();
    private VerticalLayout outerLayout = new VerticalLayout();
    private VerticalLayout leftLayout = new VerticalLayout();
    private VerticalLayout content = new VerticalLayout();
    private Accordion accordion = new Accordion();
    
    private WebsiteFactory websiteFactory = new WebsiteFactory();
    private Overview overview = new Overview();
    
    public AdminPanel(OrganisationService orgService) {
        
        // Set services in factories
        this.websiteFactory.setOrgService(orgService);
        
        
        this.outerLayout.setWidth("100%");
        this.innerLayout.setWidth("100%");
        this.leftLayout.setWidth("100%");
        this.leftLayout.setHeight("500px");
        this.content.setWidth("100%");
        //this.leftLayout.setSizeFull();
        
        this.leftLayout.setMargin(true);
        this.content.setMargin(true);
        
        this.leftLayout.setStyleName("v-layout-left");
        this.accordion.setStyleName("v-accordion-borderless");
        this.accordion.addStyleName("v-navigationpanel");
        
        // Navigation contents
        Panel tab9 = new Panel();
        tab9.addComponent(OpenInContent("Overview", this.overview, false));
        tab9.addComponent(OpenInContent("Graphs", this.websiteFactory.GetList(), false));
        this.accordion.addTab(tab9, "Overview", null);

        Panel tab1 = new Panel();
        tab1.addComponent(OpenInContent("Websites",this.websiteFactory.GetList(), false));
        /*tab1.addComponent(OpenInContent("Websites",this.websiteFactory.GetList(), false));
        tab1.addComponent(OpenInContent("Templates",new ThemeList(), false));
        tab1.addComponent(OpenInContent("Modules",new ModuleList(), true));
        
        tab1.addComponent(OpenInContent("Domains",new DomainList(), false));
        tab1.addComponent(OpenInContent("Freights",new WebsiteList(), false));
        tab1.addComponent(OpenInContent("Taxes",new TaxList(), false));
        tab1.addComponent(OpenInContent("Payment Methods",new PaymentList(), false));*/
        this.accordion.addTab(tab1, "Settings", null);
        
        Panel tab8 = new Panel();
        tab8.addComponent(OpenInContent("Pages",this.websiteFactory.GetList(), false));
        this.accordion.addTab(tab8, "Pages", null);   
        
        /*
        Panel tab5 = new Panel();
        tab5.addComponent(OpenInContent("Products",new ProductList(), false));
        tab5.addComponent(OpenInContent("Product Groups",new ProductCategoryList(), false));
        tab5.addComponent(OpenInContent("Price Groups",new UserList(), false));
        this.accordion.addTab(tab5, "Products", null);   
        
        Panel tab4 = new Panel();
        tab4.addComponent(OpenInContent("Users",new UserList(), false));
        tab4.addComponent(OpenInContent("User Groups",new UserList(), false));
        tab4.addComponent(OpenInContent("Discount Codes",new UserList(), false));
        tab4.addComponent(OpenInContent("Newsletters",new UserList(), false));
        this.accordion.addTab(tab4, "Customers", null);     
        
        Panel tab7 = new Panel();
        tab7.addComponent(OpenInContent("Orders",new OrderList(), false));
        tab7.addComponent(OpenInContent("ePay Administration",new UserList(), false));
        tab7.addComponent(OpenInContent("PayPal Administration",new UserList(), false));
        this.accordion.addTab(tab7, "Orders", null);   
        
        Panel tab2 = new Panel();
        tab2.addComponent(OpenInContent("Google Adwords",new UserList(), false));
        tab2.addComponent(OpenInContent("Facebook Advertising",new UserList(), false));
        this.accordion.addTab(tab2, "Marketing", null);
        
        Panel tab3 = new Panel();
        tab3.addComponent(OpenInContent("Google Analytics",new UserList(), false));
        this.accordion.addTab(tab3, "Stats", null);
        
        Panel tab6 = new Panel();
        //
        this.accordion.addTab(tab6, "Import/Export", null); 
        */

        this.leftLayout.addComponent(this.accordion);
        this.innerLayout.addComponent(this.leftLayout);
        
        this.innerLayout.addComponent(content);
        this.innerLayout.getComponent(0).setWidth("200px");
        this.innerLayout.setExpandRatio(this.leftLayout, 1.0f);
        this.innerLayout.setExpandRatio(this.content, 1.0f);
        
        //this.outerLayout.addComponent(new Header());
        this.outerLayout.addComponent(innerLayout);
        this.content.addComponent(new Overview());
        
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
    
    
    /*public Button OpenInDialog(final String buttonText, final Component target, Boolean buttonMargin) {
        
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
        
        
    }*/
    
}
    
    

