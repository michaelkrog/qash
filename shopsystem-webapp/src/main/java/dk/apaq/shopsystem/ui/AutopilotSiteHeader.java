package dk.apaq.shopsystem.ui;

import dk.apaq.shopsystem.ui.settings.SettingsDialog;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 */
public class AutopilotSiteHeader extends CustomComponent implements SiteHeader {

    private CssLayout layout = new CssLayout();
    private CssLayout linkColumn1 = new CssLayout();
    private CssLayout linkColumn2 = new CssLayout();
    private Resource homeResource = new ExternalResource("/");
    private Resource helpResource = new ExternalResource("http://help.qashapp.com");
    private Resource logoResource = new ThemeResource("images/ge-logo.png");
    private Embedded logo = new Embedded(null, logoResource);
    private Button linkOptions = new Button("Company options");
    private Button linkImportExport = new Button("Import/Export");
    private Link linkHelp = new Link("Help & Support", helpResource, "Help", 500, 500, Link.TARGET_BORDER_MINIMAL);
    private Link linkLogout = new Link("Log out", new ExternalResource("/logout"));
    private Link linkContact = new Link("info@shoppinnet.com", new ExternalResource("../contact.jsp"));
    private final SettingsDialog settingsDialog = new SettingsDialog();


    public AutopilotSiteHeader() {

        logo.setWidth(135, UNITS_PIXELS);

        layout.setMargin(false, true, false, true);
        layout.addComponent(logo);
        layout.addComponent(linkColumn1);
        layout.addComponent(linkColumn2);
        layout.addComponent(linkContact);

        linkColumn1.addComponent(linkOptions);
        linkColumn1.addComponent(linkImportExport);

        linkColumn2.addComponent(linkLogout);
        linkColumn2.addComponent(linkHelp);

        layout.setStyleName("ge-header");
        logo.setStyleName("ge-logo");
        linkColumn1.setStyleName("ge-userinfo");
        linkColumn2.setStyleName("ge-userinfo");
        linkContact.setStyleName("ge-phone");

        linkHelp.setStyleName(Reindeer.BUTTON_LINK);
        linkOptions.setStyleName(Reindeer.BUTTON_LINK);
        linkImportExport.setStyleName(Reindeer.BUTTON_LINK);

        setStyleName("ge-header-container");
        setCompositionRoot(layout);

        logo.addListener(new ClickListener() {

            public void click(ClickEvent event) {
                getApplication().getMainWindow().open(homeResource);
            }
        });

        linkOptions.addListener(new Button.ClickListener() {

            public void buttonClick(Button.ClickEvent event) {
                settingsDialog.center();
                getApplication().getMainWindow().addWindow(settingsDialog);
            }
        });

    }

    public SettingsDialog getSettingsDialog() {
        return settingsDialog;
    }


}
