package dk.apaq.qash.app;

import com.github.wolfie.refresher.Refresher;
import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.qash.app.ui.RegisterPanel;
import dk.apaq.qash.server.annex.AnnexService;
import dk.apaq.qash.server.service.Service;
import dk.apaq.qash.share.model.Shop;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author krog
 */
public class AdminApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(CashRegister.class);

    private final Refresher refresher = new Refresher();
    private AnnexService annexService;
    private Service service;
    private Shop shop;
    private String shopId;
    private RegisterPanel registerPanel;
    private final ComboBox cmbShop = new ComboBox("Shops");
    private HorizontalLayout topLayout = new HorizontalLayout();
    private BeanContainer container;


    
    @Override
    public void init() {
        LOG.debug("Initializing Adminapplication");
        setLogoutURL("/");
        refresher.setRefreshInterval(60000);
        ApplicationContext context = VaadinSpringHelper.getSpringContextFromVaadinContext(getContext());
        service = context.getBean("service", Service.class);
        annexService = context.getBean("annexService", AnnexService.class);

        registerPanel = new RegisterPanel(service, annexService);

        setTheme("qash");
        Window mainWindow = new Window();
        setMainWindow(mainWindow);


        VerticalLayout outerLayout = new VerticalLayout();
        outerLayout.setMargin(false);
        outerLayout.setSizeFull();
        outerLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        mainWindow.setContent(outerLayout);

        topLayout.addComponent(cmbShop);
        topLayout.setMargin(true);
        topLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        topLayout.setWidth(100, Component.UNITS_PERCENTAGE);

        outerLayout.addComponent(refresher);
        outerLayout.addComponent(topLayout);
        outerLayout.addComponent(registerPanel);
        outerLayout.setExpandRatio(registerPanel, 1.0F);
        registerPanel.setSizeFull();

        container = new BeanContainer(Shop.class);
        container.setBeanIdProperty("id");
        cmbShop.setImmediate(true);
        cmbShop.setItemCaptionPropertyId("name");
        cmbShop.setContainerDataSource(container);
        List<String> shopIds = service.getShopCrud().listIds(null, null);
        for(String id : shopIds) {
            container.addBean(service.getShopCrud().read(id));
        }
        cmbShop.addListener(new ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                Object id = event.getProperty().getValue();
                if(id==null) {
                    registerPanel.setItemDataSource(null);
                }

                Item item = container.getItem(id);
                Shop shop = ((BeanItem<Shop>)item).getBean();
                registerPanel.setItemDataSource(item);

            }
        });

        LOG.debug("AdminApplication initialized");

    }

    
}
