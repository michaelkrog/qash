package dk.apaq.shopsystem.ui.shoppinnet;


import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import dk.apaq.shopsystem.annex.AnnexService;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.ui.shoppinnet.AdminPanel;
import dk.apaq.shopsystem.ui.VaadinServiceHolder;
import dk.apaq.shopsystem.ui.VaadinSpringHelper;
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

public class AdminApplication extends Application implements HttpServletRequestListener{

    private static final Logger LOG = LoggerFactory.getLogger(AdminApplication.class);
    
    //private AnnexService annexService;
    private SystemService service;
    private String organisationId;
    private AdminPanel adminPanel;
    private VerticalLayout outerLayout = new VerticalLayout();
    //private final ICEPush pusher = new ICEPush();
    //private final CrudChangeHandler crudChangeHandler = new CrudChangeHandler();

    /*private class CrudChangeHandler extends BaseCrudListener {

        @Override
        public void onEntityDelete(WithId event) {
            pusher.push();
        }

        @Override
        public void onEntityUpdate(WithEntity event) {
            pusher.push();
        }


    }*/
    
    @Override
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
                updateOrganisation();
            }
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
        //annexService = context.getBean("annexService", AnnexService.class);
        // Spring end
        
        //siteHeader = new AutopilotSiteHeader();
        

        setTheme("shoppinnet");
        Window mainWindow = new Window();
        mainWindow.setSizeFull();
        setMainWindow(mainWindow);

        
        
        outerLayout.setMargin(true);
        outerLayout.setWidth("100%");
        outerLayout.setHeight("100%");
        //outerLayout.setSizeFull();
        outerLayout.setStyleName("v-layout-outer");
        
        mainWindow.setContent(outerLayout);

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
        
        if(adminPanel != null) {
            //Remove existing adminPanel from layout
            outerLayout.removeComponent(adminPanel);
        }

        Organisation org = service.getOrganisationCrud().read(organisationId);
        if(org==null) {
            getMainWindow().showNotification("Organisation with id "+organisationId+" does not exist.", Window.Notification.TYPE_ERROR_MESSAGE);
            return;
        }
        OrganisationService orgService = service.getOrganisationService(org);
        VaadinServiceHolder.setService(this, orgService);
        adminPanel = new AdminPanel(orgService);
        
        outerLayout.removeAllComponents();
        outerLayout.addComponent(adminPanel);
        outerLayout.setComponentAlignment(adminPanel, Alignment.MIDDLE_CENTER);
        outerLayout.setExpandRatio(adminPanel, 1.0F);
        //adminPanel.setSizeFull();
        adminPanel.setWidth("1000px");
        adminPanel.setHeight("100%");
        //adminPanel.setStyleName("v-layout-inner");

        

    }
    

}