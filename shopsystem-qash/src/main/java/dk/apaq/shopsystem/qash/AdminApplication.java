package dk.apaq.shopsystem.qash;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.shopsystem.annex.AnnexService;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.vaadin.addon.printservice.VaadinPrintPdfPlugin;
import dk.apaq.vaadin.addon.printservice.VaddinPrintAppletPlugin;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * The application class for the Admin application. This class contains no gui elements. Instead it uses <code>AdminPanel</code> which contains
 * all Gui.
 * @author krog
 */
public class AdminApplication extends Application implements HttpServletRequestListener {

    private static final Logger LOG = LoggerFactory.getLogger(AdminApplication.class);
    private AnnexService annexService;
    private SystemService service;
    private String organisationId;
    private AdminPanel adminPanel;
    private SiteHeader siteHeader;
    private VerticalLayout outerLayout = new VerticalLayout();

    @Override
    /**
     * Called on each new request. The method will detect which id is written in the url and then the
     * the chosen organisation by calling <code>updateOrganisation</code>.
     */
    public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getPathInfo();
        if (path == null) {
            try {
                response.sendRedirect("/");
                return;
            } catch (IOException ex) {
                LOG.error("Unable to redirect user.", ex);
            }
        }
        if (path.startsWith("/org/")) {
            this.organisationId = path.substring(5);
            updateOrganisation();
        }
    }

    @Override
    public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) { /* EMPTY */ }

    @Override
    /**
     * Called when the application is initialized. It will instantiate needed classes like service, AdminPanel etc.
     */
    public void init() {
        LOG.debug("Initializing ShopSystem application");
        setLogoutURL("/");

        //Load classes from spring
        ApplicationContext context = VaadinSpringHelper.getSpringContextFromVaadinContext(getContext());
        service = context.getBean("service", SystemService.class);
        annexService = context.getBean("annexService", AnnexService.class);
        // Spring end

        setTheme("shopsystem");
        
        Window mainWindow = new Window();
        mainWindow.setContent(outerLayout);
        mainWindow.setStyleName("v-adminapplication");
        setMainWindow(mainWindow);

        PrintFacade.getManager(this).addPlugin(new VaadinPrintPdfPlugin(this));
        PrintFacade.getManager(this).addPlugin(new VaddinPrintAppletPlugin(this));
        //PrintFacade.getManager(this).addPlugin(new VaadinGoogleCloudPrintPlugin(this, gcpClientId, gcpClientSecret));


        outerLayout.setMargin(false);
        outerLayout.setSizeFull();
        outerLayout.setStyleName(Reindeer.LAYOUT_WHITE);


        //outerLayout.addComponent(pusher);

        updateOrganisation();

        LOG.debug("ShopSystem application initialized");

    }

    /**
     * Updates the organisation for this application and adds listeners for the cruds.
     */
    private void updateOrganisation() {
        if (service == null || this.organisationId == null) {
            return;
        }

        if (adminPanel != null) {
            //Remove existing adminPanel from layout
            outerLayout.removeComponent(adminPanel);
        }

        Organisation org = service.getOrganisationCrud().read(organisationId);
        if (org == null) {
            getMainWindow().showNotification("Organsiation with id " + organisationId + " does not exist.", Window.Notification.TYPE_ERROR_MESSAGE);
            return;
        }

        OrganisationService organisationService = service.getOrganisationService(org);
        siteHeader = new AutopilotSiteHeader();
        adminPanel = new AdminPanel(siteHeader, annexService);

        outerLayout.addComponent(adminPanel);
        outerLayout.setExpandRatio(adminPanel, 1.0F);
        adminPanel.setSizeFull();
        adminPanel.setOrganisationService(organisationService);

    }
}