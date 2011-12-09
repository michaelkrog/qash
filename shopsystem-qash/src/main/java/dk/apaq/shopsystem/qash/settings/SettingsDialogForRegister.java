package dk.apaq.shopsystem.qash.settings;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.qash.ShopSystemTheme;
import dk.apaq.shopsystem.qash.common.CategoryGridPanel;
import dk.apaq.shopsystem.qash.common.Spacer;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.vaadin.addon.crudcontainer.HasBean;

/**
 *
 * @author michaelzachariassenkrog
 */
public class SettingsDialogForRegister extends Window {

    private final CategoryGridPanel gridPanel = new CategoryGridPanel();
    private final VerticalLayout layout = new VerticalLayout();
    private final HorizontalLayout topLayout = new HorizontalLayout();
    private final Button buttonShowAll = new Button("Show all");
    private final Spacer spacer = new Spacer();
    private final PrinterSettingPanel printerSettingPanel = new PrinterSettingPanel();
    private OrganisationService service;

    public SettingsDialogForRegister() {
        
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

        CategoryGridPanel.Category cat2 = gridPanel.addCategory("Pepherials");
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

    public void setService(OrganisationService service) {
        this.service = service;
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
