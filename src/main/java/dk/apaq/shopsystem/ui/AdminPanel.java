package dk.apaq.shopsystem.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.annex.AnnexService;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.Product;
import dk.apaq.shopsystem.entity.Store;
import dk.apaq.shopsystem.service.Service;
import dk.apaq.shopsystem.ui.CategoryList.SelectEvent;
import dk.apaq.shopsystem.ui.print.PrintDocGeneratorImpl;
import dk.apaq.vaadin.addon.crudcontainer.HasBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author krog
 */
public class AdminPanel extends CustomComponent implements com.vaadin.data.Item.Editor {

    private static final Logger LOG = LoggerFactory.getLogger(AdminPanel.class);
    private final SalesView salesView = new SalesView();
    private final ProductList stockWidget = new ProductList();
    private final SiteHeader header = new SiteHeader();
    private final VerticalLayout outerLayout = new VerticalLayout();
    private Panel leftLayout;
    private final Service service;
    private final AnnexService annexService;
    private com.vaadin.data.Item datasource;
    private VerticalLayout content = new VerticalLayout();
    private ListListener listListener = new ListListener();
    
    private class ListListener implements CategoryList.SelectListener {

        @Override
        public void onSelect(SelectEvent event) {
            String selection = event.getSelection();
            setContent(selection);
        }
        
    }

    public AdminPanel(Service service, AnnexService annexService) {
        
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

        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("General");
        categoryList.addItem("Brugere", "USERS");
        categoryList.addItem("Ordrer", "ORDERS");
        categoryList.addItem("Produkter", "STOCK");
        categoryList.addCategory("Web");
        categoryList.addItem("Sites", "WEBSITES");
        
        categoryList.setSizeFull();
        categoryList.addListener(listListener);
        
        content.setSizeFull();

        mainLayout.addComponent(categoryList);
        mainLayout.addComponent(content);
        mainLayout.setSplitPosition(236, Component.UNITS_PIXELS);

        //outerLayout.addComponent(cookies);
        outerLayout.addComponent(header);
        outerLayout.addComponent(mainLayout);
        outerLayout.setExpandRatio(mainLayout, 1.0F);
        outerLayout.setSizeFull();
        
        setCompositionRoot(outerLayout);
        categoryList.select("ORDERS");
    }

    public com.vaadin.data.Item getItemDataSource() {
        return datasource;
    }

    public void setItemDataSource(com.vaadin.data.Item newDataSource) {
        this.datasource = newDataSource;

        Organisation bean = ((HasBean<Organisation>)this.datasource).getBean();
        Crud.Complete<String, Product> productCrud = service.getProductCrud(bean);
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
        header.getSettingsDialog().setDatasource(newDataSource);
    }


    public Service getService() {
        return service;
    }
    
    private void setContent(String name) {
        Component c = null;
        if("ORDERS".equals(name)) {
            c = salesView;
        }
        
        if("STOCK".equals(name)) {
            c = stockWidget;
        }
        
        if(c==null) {
            c = new Label("Ingen widget til dette område endnu");
        }
        content.removeAllComponents();
        content.addComponent(c);
    }

}
