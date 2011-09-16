package dk.apaq.shopsystem.ui.settings;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.entity.Tax;
import dk.apaq.shopsystem.entity.User;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.ui.ShopSystemTheme;
import dk.apaq.shopsystem.ui.common.CategoryGridPanel;
import dk.apaq.shopsystem.ui.common.Spacer;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import dk.apaq.vaadin.addon.crudcontainer.HasBean;

/**
 *
 * @author michaelzachariassenkrog
 */
public class SettingsDialog extends Window {

    private final CategoryGridPanel gridPanel = new CategoryGridPanel();
    private final VerticalLayout layout = new VerticalLayout();
    private final HorizontalLayout topLayout = new HorizontalLayout();
    private final Button buttonShowAll = new Button("Show all");
    private final Spacer spacer = new Spacer();
    private final OrganisationForm organisationForm = new OrganisationForm();
    //private final ReceiptForm receiptForm = new ReceiptForm();
    private final CurrencyAndTaxPanel currencyAndTaxPanel = new CurrencyAndTaxPanel();
    private final UserManagerPanel userManagerPanel = new UserManagerPanel();
    //private final PrinterSettingPanel printerSettingPanel = new PrinterSettingPanel();
    private Item datasource;
    private OrganisationService service;

    public SettingsDialog() {
        
        setCaption("Settings");
        setResizable(false);
        setModal(true);
        setContent(layout);
        setStyleName("v-settingsdialog");
        setWidth(670, UNITS_PIXELS);
        setHeight(440, UNITS_PIXELS);

        topLayout.addComponent(buttonShowAll);
        topLayout.addComponent(spacer);
        topLayout.setExpandRatio(spacer, 1.0F);
        topLayout.setWidth(100, UNITS_PERCENTAGE);
        topLayout.setStyleName(ShopSystemTheme.LAYOUT_GRAY);
        topLayout.addStyleName("v-settingsdialog-top");
        //topLayout.setMargin(true);

        CategoryGridPanel.Category cat1 = gridPanel.addCategory("General");
        CategoryGridPanel.Category cat2 = gridPanel.addCategory("Pepherials");
        CategoryGridPanel.Category cat3 = gridPanel.addCategory("System");
        gridPanel.addComponent(cat1, new ThemeResource("img/home_48.png"), "Address", organisationForm);
        gridPanel.addComponent(cat1, new ThemeResource("img/taxes_48.png"), "Currency and taxes", currencyAndTaxPanel);
        gridPanel.addComponent(cat3, new ThemeResource("img/user_48.png"), "Users", userManagerPanel);
        //gridPanel.addComponent(cat1, new ThemeResource("img/receipt_48.png"), "Receipt", receiptForm);
        //gridPanel.addComponent(cat2, new ThemeResource("img/printer_48.png"), "Printer", printerSettingPanel);
        gridPanel.setSizeFull();

        layout.addComponent(topLayout);
        layout.addComponent(gridPanel);
        layout.setSizeFull();
        layout.setExpandRatio(gridPanel, 1.0F);

        buttonShowAll.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                gridPanel.showGrid();
            }
        });
    }

    public void setService(OrganisationService service) {
        this.service = service;
        organisationForm.setService(service);
        currencyAndTaxPanel.setService(service);
    }

    public void setDatasource(Item datasource) {
        this.datasource = datasource;
        //receiptForm.setItemDataSource(datasource);

        gridPanel.showGrid();
    }

    @Override
    public void detach() {
        gridPanel.showGrid();
    }

    private Organisation getOrganisationFromItem(Item item) {
        if(item instanceof HasBean) {
            return ((HasBean<Organisation>)item).getBean();
        }

        if(item instanceof BeanItem) {
            return ((BeanItem<Organisation>)item).getBean();
        }

        return null;
    }

}
