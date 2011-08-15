package dk.apaq.qash.app.ui;

import com.github.wolfie.detachedtabs.DetachedTabs;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.crud.Crud;
import dk.apaq.qash.app.print.PrintDocGeneratorImpl;
import dk.apaq.qash.server.annex.AnnexService;
import dk.apaq.qash.server.service.Service;
import dk.apaq.qash.share.model.Item;
import dk.apaq.qash.share.model.Payment;
import dk.apaq.qash.share.model.Shop;
import dk.apaq.vaadin.addon.crudcontainer.HasBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author krog
 */
public class RegisterPanel extends CustomComponent implements com.vaadin.data.Item.Editor {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterPanel.class);
    private final SalesView salesView = new SalesView();
    private final ProductList stockWidget = new ProductList();
    private final SiteHeader header = new SiteHeader();
    private final VerticalLayout outerLayout = new VerticalLayout();
    private Panel leftLayout;
    private final Service service;
    private final AnnexService annexService;
    private com.vaadin.data.Item datasource;
    

    public RegisterPanel(Service service, AnnexService annexService) {
        
        this.service = service;
        this.annexService = annexService;

        stockWidget.setSizeFull();
        salesView.setSizeFull();

        HorizontalSplitPanel mainLayout = new HorizontalSplitPanel();
        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        mainLayout.setStyleName(Reindeer.SPLITPANEL_SMALL);

        leftLayout = new Panel();
        leftLayout.addStyleName("tabwrapper");

        VerticalLayout sheetLayout = new VerticalLayout();
        sheetLayout.setSizeFull();

        DetachedTabs tabSheet = new DetachedTabs.Vertical(sheetLayout);
        tabSheet.addTab(salesView, "Sales View");
        tabSheet.addTab(stockWidget, "Inventory Management");
        tabSheet.setWidth("100%");

        leftLayout.addComponent(tabSheet);
        Component spacer = new Label();
        leftLayout.addComponent(spacer);
        ((VerticalLayout)leftLayout.getContent()).setExpandRatio(spacer, 1.0F);
        ((VerticalLayout)leftLayout.getContent()).setMargin(false);

        mainLayout.addComponent(leftLayout);
        mainLayout.addComponent(sheetLayout);
        mainLayout.setSplitPosition(236, Component.UNITS_PIXELS);

        //outerLayout.addComponent(cookies);
        outerLayout.addComponent(header);
        outerLayout.addComponent(mainLayout);
        outerLayout.setExpandRatio(mainLayout, 1.0F);
        outerLayout.setSizeFull();
        
        setCompositionRoot(outerLayout);
    }

    public com.vaadin.data.Item getItemDataSource() {
        return datasource;
    }

    public void setItemDataSource(com.vaadin.data.Item newDataSource) {
        this.datasource = newDataSource;

        Shop bean = ((HasBean<Shop>)this.datasource).getBean();
        Crud.Complete<String, Item> productCrud = service.getItemCrud(bean);
        Crud.Editable<String, Payment> paymentCrud = service.getPaymentCrud(bean);

        stockWidget.setProductCrud(productCrud);
        stockWidget.setTaxCrud(service.getTaxCrud(bean));
        salesView.setOrderCrud(service.getOrderCrud(bean));
        salesView.setPaymentCrud(paymentCrud);
        salesView.setProductCrud(productCrud);
        salesView.setTaxCrud(service.getTaxCrud(bean));
        salesView.setPrintDocGenerator(new PrintDocGeneratorImpl(annexService, bean));

        leftLayout.setCaption(bean.getName());

        header.getSettingsDialog().setService(service);
        header.getSettingsDialog().setShopDatasource(newDataSource);
    }


    public Service getService() {
        return service;
    }

}
