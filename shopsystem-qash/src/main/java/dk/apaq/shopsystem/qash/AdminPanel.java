package dk.apaq.shopsystem.qash;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.annex.AnnexService;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.qash.common.CategoryListPanel;
import dk.apaq.shopsystem.qash.settings.SettingsDialog;
import dk.apaq.shopsystem.service.OrganisationService;

/**
 * Panel for handling gui in the admin application.
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
    private BeanItem datasource;
    private final Button btnSettings = new Button("Settings");
    private final Link linkHelp = new Link("Help", new ExternalResource("http://help.qashapp.com"), "Help", 500, 500, Link.TARGET_BORDER_MINIMAL);
    private final Link linkLogout = new Link("Log out", new ExternalResource("/logout"));
    private final Link linkDashboard = new Link("Dashboard", new ExternalResource("/dashboard.jsp"));
    private final CategoryListPanel categoryListPanel = new CategoryListPanel();

    private class SettingsListener implements Button.ClickListener {

        @Override
        public void buttonClick(ClickEvent event) {
            SettingsDialog settingsDialog = new SettingsDialog();
            OrganisationService orgService = VaadinServiceHolder.getService(getApplication());

            Organisation org = orgService.readOrganisation();
            Item datasource = new BeanItem(org);

            settingsDialog.setService(orgService);
            settingsDialog.setDatasource(datasource);

            getApplication().getMainWindow().addWindow(settingsDialog);
        }
    }

    public AdminPanel(SiteHeader siteHeader, AnnexService annexService) {
        this.header = siteHeader;
        this.annexService = annexService;

        stockWidget.setSizeFull();
        salesView.setSizeFull();
        customerList.setSizeFull();
        storeList.setSizeFull();

        categoryListPanel.addCategory("General");
        categoryListPanel.addItem("CUSTOMERS", "Kunder", customerList);
        categoryListPanel.addItem("ORDERS", "Ordrer", salesView);
        categoryListPanel.addItem("STOCK", "Produkter", stockWidget);
        categoryListPanel.addItem("STORES", "Butikker", storeList);

        categoryListPanel.addCategory("Export");
        categoryListPanel.addItem("DAYBOOK", "Export Daybook", new Label("Daybook"));
        
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

    @Override
    public void attach() {
        OrganisationService orgService = VaadinServiceHolder.getService(getApplication());

        Organisation org = orgService.readOrganisation();
        this.datasource = new BeanItem(org);


        stockWidget.setProductCrud(orgService.getProducts());
        stockWidget.setTaxCrud(orgService.getTaxes());

        customerList.setCustomerCrud(orgService.getCustomers());

        storeList.setCrud(orgService.getStores());

        salesView.setOrderCrud(orgService.getOrders());
        salesView.setPaymentCrud(orgService.getPayments());
        salesView.setProductCrud(orgService.getProducts());
        salesView.setTaxCrud(orgService.getTaxes());
        salesView.setAnnexService(annexService);

    }

}
