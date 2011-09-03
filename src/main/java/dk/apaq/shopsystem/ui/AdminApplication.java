package dk.apaq.shopsystem.ui;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.crud.CrudEvent.WithEntity;
import dk.apaq.crud.CrudEvent.WithId;
import dk.apaq.crud.CrudNotifier;
import dk.apaq.crud.core.BaseCrudListener;
import dk.apaq.shopsystem.annex.AnnexService;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.Service;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author krog
 */

public class AdminApplication extends Application implements HttpServletRequestListener{

    private static final Logger LOG = LoggerFactory.getLogger(AdminApplication.class);
    
    private AnnexService annexService;
    private Service service;
    private String organisationId;
    private AdminPanel adminPanel;
    private CrudContainer<String, Organisation> orgContainer;
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
                updateShop();
            }
        }
    }

    public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) { /* EMPTY */ }


    @Override
    public void init() {
        LOG.debug("Initializing ShopSystem application");
        setLogoutURL("/");
        //refresher.setRefreshInterval(60000);
        ApplicationContext context = VaadinSpringHelper.getSpringContextFromVaadinContext(getContext());
        service = context.getBean("service", Service.class);
        annexService = context.getBean("annexService", AnnexService.class);
        orgContainer = new CrudContainer<String, Organisation>(service.getOrganisationCrud(), Organisation.class);

        adminPanel = new AdminPanel(service, annexService);

        setTheme("shopsystem");
        Window mainWindow = new Window();
        setMainWindow(mainWindow);

        
        VerticalLayout outerLayout = new VerticalLayout();
        outerLayout.setMargin(false);
        outerLayout.setSizeFull();
        outerLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        mainWindow.setContent(outerLayout);

        //outerLayout.addComponent(pusher);
        outerLayout.addComponent(adminPanel);
        outerLayout.setExpandRatio(adminPanel, 1.0F);
        adminPanel.setSizeFull();

        updateShop();

        LOG.debug("ShopSystem application initialized");
        
    }

    private void updateShop() {
        if (service == null || this.organisationId == null) {
            return;
        }

        Item item = orgContainer.getItem(organisationId);
        adminPanel.setItemDataSource(item);

        /*
        Organisation shop = service.get.read(shopId);
        ((CrudNotifier)service.getItemCrud(shop)).addListener(crudChangeHandler);
        ((CrudNotifier)service.getOrderCrud(shop)).addListener(crudChangeHandler);
        ((CrudNotifier)service.getTaxCrud(shop)).addListener(crudChangeHandler);
*/
        

    }
    

}