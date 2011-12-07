package dk.apaq.shopsystem.qash;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.annex.AnnexService;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.qash.settings.SettingsDialogForAdmin;
import dk.apaq.shopsystem.qash.settings.SettingsDialogForRegister;
import dk.apaq.shopsystem.service.OrganisationService;

/**
 *
 * @author michaelzachariassenkrog
 */
public class RegisterPanel extends CustomComponent {

    private final VerticalLayout outerLayout = new VerticalLayout();
    private final SalesView salesView = new SalesView();
    private final SiteHeader header;
    private final AnnexService annexService;
    private OrganisationService organisationService;
    private final Button btnSettings = new Button("Settings");
    private final Link linkHelp = new Link("Help", new ExternalResource("http://help.qashapp.com"), "Help", 500, 500, Link.TARGET_BORDER_MINIMAL);
    private final Link linkLogout = new Link("Log out", new ExternalResource("/logout"));
    private final Link linkDashboard = new Link("Dashboard", new ExternalResource("/dashboard.jsp"));
    
    private class SettingsListener implements Button.ClickListener {

        @Override
        public void buttonClick(ClickEvent event) {
            SettingsDialogForRegister settingsDialog = new SettingsDialogForRegister();
            
            Organisation org = organisationService.readOrganisation();
            Item datasource = new BeanItem(org);

            settingsDialog.setService(organisationService);
            settingsDialog.setDatasource(datasource);

            getApplication().getMainWindow().addWindow(settingsDialog);
        }
    }

    public RegisterPanel(SiteHeader siteHeader, AnnexService annexService) {
        this.header = siteHeader;
        this.annexService = annexService;

        salesView.setSizeFull();
        salesView.setAnnexService(annexService);
        
        siteHeader.addLink(linkDashboard);
        siteHeader.addButton(btnSettings);
        siteHeader.addLink(linkHelp);
        siteHeader.addLink(linkLogout);

        outerLayout.addComponent(header);
        outerLayout.addComponent(salesView);
        outerLayout.setExpandRatio(salesView, 1.0F);
        outerLayout.setSizeFull();
        
        btnSettings.addListener(new SettingsListener());

        setCompositionRoot(outerLayout);
        

    }

    public void setOrganisationService(OrganisationService organisationService) {
        this.organisationService = organisationService;
        salesView.setOrganisationService(organisationService);
    }

    
}
