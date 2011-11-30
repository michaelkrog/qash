package dk.apaq.shopsystem.qash;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.shopsystem.annex.AnnexService;
import dk.apaq.shopsystem.entity.Organisation;
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
 *
 * @author michaelzachariassenkrog
 */
public class RegisterApplication extends Application implements HttpServletRequestListener {
    private static final Logger LOG = LoggerFactory.getLogger(RegisterApplication.class);

    private RegisterPanel registerPanel;
    private VerticalLayout outerLayout = new VerticalLayout();
    private SiteHeader siteHeader;

    private AnnexService annexService;
    private SystemService service;
    private String organisationId;

    /**
     * Called on each new request. The method will detect which id is written in the url and then the
     * the chosen organisation by calling <code>updateOrganisation</code>.
     */
    public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getPathInfo();
        if(path==null) {
            try {
                response.sendRedirect("/");
                return;
            } catch (IOException ex) {
                LOG.error("Unable to redirect user.", ex);
            }
        }
        int idStartIndex = path.lastIndexOf("/");
        if(idStartIndex>=0) {
            String tmp = path.substring(idStartIndex + 1);
            if(tmp.startsWith("id:")) {
                this.organisationId = tmp.substring(3);
                updateStore();
            }
        }
    }

    @Override
    public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) { /* EMPTY */ }

    /**
     * Called when the application is initialized. It will instantiate needed classes like service, AdminPanel etc.
     */
    @Override
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
        setMainWindow(mainWindow);

        PrintFacade.getManager(this).addPlugin(new VaadinPrintPdfPlugin(this));
        PrintFacade.getManager(this).addPlugin(new VaddinPrintAppletPlugin(this));
        //PrintFacade.getManager(this).addPlugin(new VaadinGoogleCloudPrintPlugin(this, gcpClientId, gcpClientSecret));


        outerLayout.setMargin(false);
        outerLayout.setSizeFull();
        outerLayout.setStyleName(Reindeer.LAYOUT_WHITE);


        //outerLayout.addComponent(pusher);

        updateStore();

        LOG.debug("ShopSystem application initialized");

    }

    /**
     * Updates the organisation for this application and adds listeners for the cruds.
     */
    private void updateStore() {
        if (service == null || this.organisationId == null) {
            return;
        }

        if(registerPanel != null) {
            //Remove existing adminPanel from layout
            outerLayout.removeComponent(registerPanel);
        }

        Organisation org = service.getOrganisationCrud().read(organisationId);
        if(org==null) {
            getMainWindow().showNotification("Organsiation with id "+organisationId+" does not exist.", Window.Notification.TYPE_ERROR_MESSAGE);
            return;
        }

        VaadinServiceHolder.setService(this, service.getOrganisationService(org));
        siteHeader = new AutopilotSiteHeader();
        registerPanel = new RegisterPanel(siteHeader, annexService);

        outerLayout.addComponent(registerPanel);
        outerLayout.setExpandRatio(registerPanel, 1.0F);
        registerPanel.setSizeFull();

    }


}
