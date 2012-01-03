package dk.apaq.shopsystem.qash;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.annex.AnnexService;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.qash.common.CategoryListPanel;
import dk.apaq.shopsystem.qash.settings.SettingsDialogForAdmin;
import dk.apaq.shopsystem.service.OrganisationService;

/**
 * Panel for handling gui in the admin application. It links all views together.
 * @author krog
 */
public class AdminPanel extends CustomComponent {

    private final SalesView salesView = new SalesView();
    private final ProductList stockWidget = new ProductList();
    private final CustomerList customerList = new CustomerList();
    private final StoreList storeList = new StoreList();
    private final SiteHeader header;
    private final VerticalLayout outerLayout = new VerticalLayout();
    private final AnnexService annexService;
    private OrganisationService organisationService;
    private final Button btnSettings = new Button("Settings");
    private final Link linkHelp = new Link("Help", new ExternalResource("http://help.qashapp.com"), "Help", 500, 500, Link.TARGET_BORDER_MINIMAL);
    private final Link linkLogout = new Link("Log out", new ExternalResource("/logout"));
    private final Link linkDashboard = new Link("Dashboard", new ExternalResource("/dashboard.htm"));
    private final CategoryListPanel categoryListPanel = new CategoryListPanel();
    private final DaybookExportPanel daybookExportPanel = new DaybookExportPanel();

    private class SettingsListener implements Button.ClickListener {

        @Override
        public void buttonClick(ClickEvent event) {
            SettingsDialogForAdmin settingsDialog = new SettingsDialogForAdmin();
            
            Organisation org = organisationService.readOrganisation();
            settingsDialog.setOrganisationService(organisationService);
            getApplication().getMainWindow().addWindow(settingsDialog);
        }
    }

    public AdminPanel(SiteHeader siteHeader, AnnexService annexService) {
        this.header = siteHeader;
        this.annexService = annexService;
        
        salesView.setAnnexService(annexService);

        stockWidget.setSizeFull();
        salesView.setSizeFull();
        customerList.setSizeFull();
        storeList.setSizeFull();

        categoryListPanel.addCategory("General");
        categoryListPanel.addItem("CUSTOMERS", "Customers", customerList);
        categoryListPanel.addItem("ORDERS", "Orders", salesView);
        categoryListPanel.addItem("STOCK", "Products", stockWidget);
        categoryListPanel.addItem("STORES", "Stores", storeList);

        categoryListPanel.addCategory("Data");
        categoryListPanel.addItem("DAYBOOK", "Export Daybook", daybookExportPanel);
        
        categoryListPanel.setSizeFull();

        //outerLayout.addComponent(cookies);
        outerLayout.addComponent(header);
        outerLayout.addComponent(categoryListPanel);
        outerLayout.setExpandRatio(categoryListPanel, 1.0F);
        outerLayout.setSizeFull();

        siteHeader.addLink(linkDashboard);
        siteHeader.addButton(btnSettings);
        siteHeader.addLink(linkHelp);
        siteHeader.addLink(linkLogout);


        btnSettings.addListener(new SettingsListener());

        setCompositionRoot(outerLayout);
        categoryListPanel.select("ORDERS");

    }

    public void setOrganisationService(OrganisationService organisationService) {
        this.organisationService = organisationService;
    
        
        stockWidget.setProductCrud(organisationService.getProducts());
        stockWidget.setTaxCrud(organisationService.getTaxes());

        customerList.setCustomerCrud(organisationService.getCustomers());

        storeList.setCrud(organisationService.getStores());

        salesView.setOrganisationService(organisationService);
        
        daybookExportPanel.setOrganisationService(organisationService);

    }

}
