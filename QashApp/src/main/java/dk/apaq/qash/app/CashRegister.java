/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.qash.app;


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

import dk.apaq.qash.share.model.Shop;
import dk.apaq.qash.app.ui.RegisterPanel;
import dk.apaq.qash.server.annex.AnnexService;

import dk.apaq.qash.server.service.Service;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vaadin.artur.icepush.ICEPush;

/**
 *
 */
public class CashRegister extends Application implements HttpServletRequestListener{

    private static final Logger LOG = LoggerFactory.getLogger(CashRegister.class);
    
    //private final Refresher refresher = new Refresher();
    private AnnexService annexService;
    private Service service;
    private String shopId;
    private RegisterPanel registerPanel;
    private CrudContainer<String, Shop> shopContainer;
    private final ICEPush pusher = new ICEPush();
    private final CrudChangeHandler crudChangeHandler = new CrudChangeHandler();

    private class CrudChangeHandler extends BaseCrudListener {

        @Override
        public void onEntityDelete(WithId event) {
            pusher.push();
        }

        @Override
        public void onEntityUpdate(WithEntity event) {
            pusher.push();
        }


    }
    
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
                this.shopId = tmp.substring(3);
                updateShop();
            }
        }
    }

    public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) { /* EMPTY */ }


    @Override
    public void init() {
        LOG.debug("Initializing CashRegister application");
        setLogoutURL("/");
        //refresher.setRefreshInterval(60000);
        ApplicationContext context = VaadinSpringHelper.getSpringContextFromVaadinContext(getContext());
        service = context.getBean("service", Service.class);
        annexService = context.getBean("annexService", AnnexService.class);
        shopContainer = new CrudContainer<String, Shop>(service.getShopCrud(), Shop.class);

        registerPanel = new RegisterPanel(service, annexService);

        setTheme("qash");
        Window mainWindow = new Window();
        setMainWindow(mainWindow);

        
        VerticalLayout outerLayout = new VerticalLayout();
        outerLayout.setMargin(false);
        outerLayout.setSizeFull();
        outerLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        mainWindow.setContent(outerLayout);

        outerLayout.addComponent(pusher);
        outerLayout.addComponent(registerPanel);
        outerLayout.setExpandRatio(registerPanel, 1.0F);
        registerPanel.setSizeFull();

        updateShop();

        LOG.debug("CashRegister application initialized");
        
    }

    private void updateShop() {
        if (service == null || this.shopId == null) {
            return;
        }

        Item item = shopContainer.getItem(shopId);
        registerPanel.setItemDataSource(item);

        Shop shop = service.getShopCrud().read(shopId);
        ((CrudNotifier)service.getItemCrud(shop)).addListener(crudChangeHandler);
        ((CrudNotifier)service.getOrderCrud(shop)).addListener(crudChangeHandler);
        ((CrudNotifier)service.getTaxCrud(shop)).addListener(crudChangeHandler);

        

    }
    

}
