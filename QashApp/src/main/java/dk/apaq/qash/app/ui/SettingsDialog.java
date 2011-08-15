package dk.apaq.qash.app.ui;

import com.vaadin.data.Item;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import dk.apaq.qash.app.QashTheme;
import dk.apaq.qash.app.ui.common.CategoryGridPanel;
import dk.apaq.qash.app.ui.common.Spacer;
import dk.apaq.qash.server.service.Service;
import dk.apaq.qash.share.model.Shop;
import dk.apaq.qash.share.model.Tax;
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
    private final ShopForm shopForm = new ShopForm();
    private final ReceiptForm receiptForm = new ReceiptForm();
    private final CurrencyAndTaxPanel currencyAndTaxPanel = new CurrencyAndTaxPanel();
    private final PrinterSettingPanel printerSettingPanel = new PrinterSettingPanel();
    private Item shopDatasource;
    private Service service;

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
        topLayout.setStyleName(QashTheme.LAYOUT_GRAY);
        topLayout.addStyleName("v-settingsdialog-top");
        //topLayout.setMargin(true);

        CategoryGridPanel.Category cat1 = gridPanel.addCategory("General");
        CategoryGridPanel.Category cat2 = gridPanel.addCategory("Pepherials");
        gridPanel.addComponent(cat1, new ThemeResource("img/home_48.png"), "Address", shopForm);
        gridPanel.addComponent(cat1, new ThemeResource("img/taxes_48.png"), "Currency and taxes", currencyAndTaxPanel);
        gridPanel.addComponent(cat1, new ThemeResource("img/receipt_48.png"), "Receipt", receiptForm);
        gridPanel.addComponent(cat2, new ThemeResource("img/printer_48.png"), "Printer", printerSettingPanel);
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

    public void setService(Service service) {
        this.service = service;
    }

    public void setShopDatasource(Item shopDatasource) {
        this.shopDatasource = shopDatasource;
        Shop shop = ((HasBean<Shop>)shopDatasource).getBean();
        //Container c = new CrudContainer(this.service.getShopCrud(), Shop.class);
        //Item shopItem = c.getItem(shop.getId());
        shopForm.setItemDataSource(shopDatasource);
        receiptForm.setItemDataSource(shopDatasource);

        currencyAndTaxPanel.setContainerDataSource(new CrudContainer(service.getTaxCrud(shop), Tax.class));
        currencyAndTaxPanel.setShopDataSource(shopDatasource);
        gridPanel.showGrid();
    }

    @Override
    public void detach() {
        gridPanel.showGrid();
    }


}
