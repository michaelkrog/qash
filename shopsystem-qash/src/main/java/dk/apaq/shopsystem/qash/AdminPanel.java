package dk.apaq.shopsystem.qash;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.shopsystem.annex.AnnexService;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.qash.common.CategoryList;
import dk.apaq.shopsystem.qash.common.CategoryList.SelectEvent;
import dk.apaq.shopsystem.qash.common.HtmlEditor;
import dk.apaq.shopsystem.qash.settings.SettingsDialog;
import dk.apaq.shopsystem.service.OrganisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Panel for handling gui in the admin application.
 * @author krog
 */
public class AdminPanel extends CustomComponent {

    private static final Logger LOG = LoggerFactory.getLogger(AdminPanel.class);
    private final SalesView salesView = new SalesView();
    private final ProductList stockWidget = new ProductList();
    private final CustomerList customerList = new CustomerList();
    private final StoreList storeList = new StoreList();
    private final SiteHeader header;
    private final VerticalLayout outerLayout = new VerticalLayout();
    private Panel leftLayout;
    private final AnnexService annexService;
    private BeanItem datasource;
    private VerticalLayout content = new VerticalLayout();
    private ListListener listListener = new ListListener();
    private final HtmlEditor htmlEditor = new HtmlEditor();
    private final Button btnSettings = new Button("Settings");
    private final Link linkHelp = new Link("Help", new ExternalResource("http://help.qashapp.com"), "Help", 500, 500, Link.TARGET_BORDER_MINIMAL);
    private final Link linkLogout = new Link("Log out", new ExternalResource("/logout"));
    private final Link linkDashboard = new Link("Dashboard", new ExternalResource("/dashboard.jsp"));

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

    private class ListListener implements CategoryList.SelectListener {

        @Override
        public void onSelect(SelectEvent event) {
            String selection = event.getSelection();
            setContent(selection);
        }
    }

    public AdminPanel(SiteHeader siteHeader, AnnexService annexService) {
        this.header = siteHeader;
        this.annexService = annexService;

        stockWidget.setSizeFull();
        salesView.setSizeFull();
        customerList.setSizeFull();
        storeList.setSizeFull();

        HorizontalSplitPanel mainLayout = new HorizontalSplitPanel();
        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        mainLayout.setStyleName(Reindeer.SPLITPANEL_SMALL);

        leftLayout = new Panel();
        leftLayout.addStyleName("tabwrapper");

        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("General");
        categoryList.addItem("Kunder", "CUSTOMERS");
        categoryList.addItem("Ordrer", "ORDERS");
        categoryList.addItem("Produkter", "STOCK");
        categoryList.addItem("Butikker", "STORES");

        categoryList.addCategory("Import/Export");
        categoryList.addItem("Export Daybook", "DAYBOOK");

        categoryList.setSizeFull();
        categoryList.addListener(listListener);

        content.setSizeFull();

        mainLayout.addComponent(categoryList);
        mainLayout.addComponent(content);
        mainLayout.setSplitPosition(147, Component.UNITS_PIXELS);

        //outerLayout.addComponent(cookies);
        outerLayout.addComponent(header);
        outerLayout.addComponent(mainLayout);
        outerLayout.setExpandRatio(mainLayout, 1.0F);
        outerLayout.setSizeFull();

        siteHeader.addLink(linkDashboard);
        siteHeader.addButton(btnSettings);
        siteHeader.addLink(linkHelp);
        siteHeader.addLink(linkLogout);


        btnSettings.addListener(new SettingsListener());

        setCompositionRoot(outerLayout);
        categoryList.select("ORDERS");


    }

    @Override
    public void attach() {
        //This should be cleaned up
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

        leftLayout.setCaption(org.getCompanyName());

        //header.getSettingsDialog().setService(orgService);
        //header.getSettingsDialog().setDatasource(this.datasource);
    }

    private void setContent(String name) {

        Component c = null;
        if ("ORDERS".equals(name)) {
            c = salesView;
        }

        if ("STOCK".equals(name)) {
            c = stockWidget;
        }

        if ("STORES".equals(name)) {
            c = storeList;
        }

        if ("CUSTOMERS".equals(name)) {
            c = customerList;
        }

        if (c == null) {
            c = new Label("Ingen widget til dette område endnu");
        }


        content.removeAllComponents();
        content.addComponent(c);
    }
}
