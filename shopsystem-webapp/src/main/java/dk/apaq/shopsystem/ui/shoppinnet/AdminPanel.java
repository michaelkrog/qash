package dk.apaq.shopsystem.ui.shoppinnet;


import com.vaadin.terminal.Resource;
import dk.apaq.shopsystem.ui.shoppinnet.factory.WebsiteFactory;
import dk.apaq.shopsystem.ui.shoppinnet.factory.DomainFactory;
import dk.apaq.shopsystem.ui.shoppinnet.factory.CategoryFactory;
import dk.apaq.shopsystem.ui.shoppinnet.factory.ProductFactory;
import dk.apaq.shopsystem.ui.shoppinnet.factory.PageFactory;
import dk.apaq.shopsystem.ui.shoppinnet.factory.PaymentFactory;
import dk.apaq.shopsystem.ui.shoppinnet.factory.ThemeFactory;
import dk.apaq.shopsystem.ui.shoppinnet.factory.ModuleFactory;
import dk.apaq.shopsystem.ui.shoppinnet.factory.TaxFactory;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.shopsystem.service.OrganisationService;


public class AdminPanel extends CustomComponent {
    
    private HorizontalLayout innerLayout = new HorizontalLayout();
    private VerticalLayout layoutHolder = new VerticalLayout();
    private VerticalLayout outerLayout = new VerticalLayout();
    private VerticalLayout leftLayout = new VerticalLayout();
    private VerticalLayout content = new VerticalLayout();
    private VerticalLayout spacer = new VerticalLayout();
    private Accordion accordion = new Accordion();
    
    private Overview overview = new Overview();
    private WebsiteFactory websiteFactory = new WebsiteFactory();
    private DomainFactory domainFactory = new DomainFactory();
    private CategoryFactory categoryFactory = new CategoryFactory();
    private ProductFactory productFactory = new ProductFactory();
    private PageFactory pageFactory = new PageFactory();
    private PaymentFactory paymentFactory = new PaymentFactory();
    private ThemeFactory themeFactory = new ThemeFactory();
    private ModuleFactory moduleFactory = new ModuleFactory();
    private TaxFactory taxFactory = new TaxFactory();
    
    public AdminPanel(OrganisationService orgService) {
        
        // Set services in factories
        this.websiteFactory.setOrgService(orgService);
        this.domainFactory.setOrgService(orgService);
        this.categoryFactory.setOrgService(orgService);
        this.productFactory.setOrgService(orgService);
        this.pageFactory.setOrgService(orgService);
        this.paymentFactory.setOrgService(orgService);
        this.themeFactory.setOrgService(orgService);
        this.moduleFactory.setOrgService(orgService);
        this.taxFactory.setOrgService(orgService);
        
        
        this.layoutHolder.setWidth("100%");
        this.layoutHolder.setHeight("100%");
        
        this.outerLayout.setWidth("100%");
        this.outerLayout.setHeight("100%");
        this.outerLayout.setStyleName("v-layout-inner");
        
        this.innerLayout.setWidth("100%");
        this.innerLayout.setHeight("100%");
        
        this.leftLayout.setWidth("100%");
        this.leftLayout.setHeight("100%");

        this.content.setWidth("100%");
        this.spacer.setSizeFull();
        
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
        tab1.addComponent(OpenInContent("Online Stores", this.websiteFactory.GetList(), false));
        tab1.addComponent(OpenInContent("Themes", this.themeFactory.GetList(), false));
        tab1.addComponent(OpenInContent("Modules", this.moduleFactory.GetList(), true));
        
        tab1.addComponent(OpenInContent("Domains", this.domainFactory.GetList(), false));
        //tab1.addComponent(OpenInContent("Freights", this.freightFactory.GetList(), false));
        tab1.addComponent(OpenInContent("Taxes", this.taxFactory.GetList(), false));
        tab1.addComponent(OpenInContent("Payment Methods", this.paymentFactory.GetList(), false));
        this.accordion.addTab(tab1, "Settings", null);
        
        Panel tab8 = new Panel();
        tab8.addComponent(OpenInContent("Pages",this.pageFactory.GetList(), false));
        this.accordion.addTab(tab8, "Pages", null);       
        
        Panel tab5 = new Panel();
        tab5.addComponent(OpenInContent("Categories", this.categoryFactory.GetList(), false));
        tab5.addComponent(OpenInContent("Products", this.productFactory.GetList(), false));
        //tab5.addComponent(OpenInContent("Product Groups",new ProductCategoryList(), false));
        //tab5.addComponent(OpenInContent("Price Groups",new UserList(), false));
        this.accordion.addTab(tab5, "Products", null);   
       
        /*
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
        this.leftLayout.addComponent(this.spacer);
        this.leftLayout.setExpandRatio(this.spacer, 1.0f);
        
        //this.innerLayout.setExpandRatio(this.leftLayout, 1.0f);
        
        this.innerLayout.addComponent(this.leftLayout);
        this.innerLayout.addComponent(content);
        this.innerLayout.getComponent(0).setWidth("200px");
        this.innerLayout.setExpandRatio(this.content, 1.0f);
        
        //this.outerLayout.addComponent(new Header());
        
        this.outerLayout.addComponent(innerLayout);
        this.content.addComponent(new Overview());
        
        Resource logoResource = new ThemeResource("layout/img/logo.png");
        Embedded logo  = new Embedded(null, logoResource);
        logo.setStyleName("v-logo");
        this.layoutHolder.addComponent(logo);
        this.layoutHolder.setComponentAlignment(logo, Alignment.MIDDLE_RIGHT);
        //this.layoutHolder.getComponent(0).setHeight("36px");
        this.layoutHolder.addComponent(this.outerLayout);
        this.layoutHolder.setExpandRatio(this.outerLayout, 1.0f);
        
        setCompositionRoot(this.layoutHolder);
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
    
    

