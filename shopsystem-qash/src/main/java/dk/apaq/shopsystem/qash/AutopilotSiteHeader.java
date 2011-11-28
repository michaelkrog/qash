package dk.apaq.shopsystem.qash;

import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Link;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.shopsystem.qash.settings.SettingsDialog;

/**
 *
 */
public class AutopilotSiteHeader extends CustomComponent implements SiteHeader {

    private CssLayout layout = new CssLayout();
    private CssLayout innerLayout = new CssLayout();

    private Resource homeResource = new ExternalResource("/");
    private Resource logoResource = new ThemeResource("images/ge-logo.png");
    private Embedded logo = new Embedded(null, logoResource);
    private Link linkContact = new Link("info@qashapp.com", new ExternalResource("../contact.jsp"));
    private int linkCount = 0;
    private CssLayout currentColumn;

    public AutopilotSiteHeader() {

        logo.setWidth(135, UNITS_PIXELS);

        layout.setMargin(false, true, false, true);
        layout.addComponent(logo);
        layout.addComponent(innerLayout);
        layout.addComponent(linkContact);

        layout.setStyleName("ge-header");
        logo.setStyleName("ge-logo");
        linkContact.setStyleName("ge-phone");

        setStyleName("ge-header-container");
        setCompositionRoot(layout);

        logo.addListener(new ClickListener() {

            public void click(ClickEvent event) {
                getApplication().getMainWindow().open(homeResource);
            }
        });

    }

    @Override
    public void addLink(Link link) {
        link.setStyleName(Reindeer.BUTTON_LINK);
        add(link);
    }

    @Override
    public void addButton(Button link) {
        link.setStyleName(Reindeer.BUTTON_LINK);
        add(link);
    }

    private void add(Component c) {
        if(linkCount % 2 == 0) {
            currentColumn = new CssLayout();
            currentColumn.setStyleName("ge-userinfo");
            innerLayout.addComponent(currentColumn);
        }
        currentColumn.addComponent(c);
        linkCount++;
    }

    /*public SettingsDialog getSettingsDialog() {
        return settingsDialog;
    }*/



}
