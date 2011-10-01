package dk.apaq.shopsystem.ui.qash;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.shopsystem.annex.AnnexService;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.OrganisationService;
//import dk.apaq.shopsystem.ui.ConstructionList;
import dk.apaq.shopsystem.ui.SiteHeader;
import dk.apaq.shopsystem.ui.VaadinServiceHolder;
import dk.apaq.shopsystem.ui.common.CategoryList;
import dk.apaq.shopsystem.ui.common.CategoryList.SelectEvent;
import dk.apaq.shopsystem.ui.qash.print.PrintDocGeneratorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Panel for handling gui in the admin application.
 * @author krog
 */
public class AdminPanel extends CustomComponent {

    private static final Logger LOG = LoggerFactory.getLogger(AdminPanel.class);
    private final SalesView salesView = new SalesView();
    //private final ConstructionList constructionList = new ConstructionList();
    //private final CommonDialog constructionForm = new CommonDialog("",new ConstructionForm("1"));
    private final ProductList stockWidget = new ProductList();
    private final SiteHeader header;
    private final VerticalLayout outerLayout = new VerticalLayout();
    private Panel leftLayout;
    private final AnnexService annexService;
    private BeanItem datasource;
    private VerticalLayout content = new VerticalLayout();
    private ListListener listListener = new ListListener();

    private class ListListener implements CategoryList.SelectListener {

        @Override
        public void onSelect(SelectEvent event) {
            String selection = event.getSelection();
            setContent(selection);
        }
        
    }

    public AdminPanel(SiteHeader siteHeader, AnnexService annexService) {
        this.header = siteHeader;
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
        categoryList.addItem("Kunder", "CUSTOMERS");
        categoryList.addItem("Ordrer", "ORDERS");
        categoryList.addItem("Produkter", "STOCK");
        
        categoryList.addCategory("Stores");
        categoryList.addItem("Aalborg", "STORE:3123123213123");
        categoryList.addItem("Aarhus", "STORE:3123123213123");
        categoryList.addItem("København", "STORE:3123123213123");
        
        categoryList.addCategory("Websites");
        categoryList.addItem("Bikez", "WEBSITE:1341312312");
        categoryList.addItem("Trendy Jewelry", "WEBSITE:141352424");
        
        categoryList.setSizeFull();
        categoryList.addListener(listListener);
        
        content.setSizeFull();

        mainLayout.addComponent(categoryList);
        mainLayout.addComponent(content);
        mainLayout.setSplitPosition(147, Component.UNITS_PIXELS);

        //outerLayout.addComponent(cookies);
        outerLayout.addComponent(header);
        outerLayout.addComponent(mainLayout);
        outerLayout.setExpandRatio(mainLayout, 1.0F);
        outerLayout.setSizeFull();
        
        setCompositionRoot(outerLayout);
        categoryList.select("ORDERS");
        

    }

    @Override
    public void attach() {
        //This should be cleaned up
        OrganisationService orgService = VaadinServiceHolder.getService(getApplication());

        Organisation org = orgService.readOrganisation();
        this.datasource = new BeanItem(org);


        stockWidget.setProductCrud(orgService.getProducts());
        stockWidget.setTaxCrud(orgService.getTaxes());

        salesView.setOrderCrud(orgService.getOrders());
        salesView.setPaymentCrud(orgService.getPayments());
        salesView.setProductCrud(orgService.getProducts());
        salesView.setTaxCrud(orgService.getTaxes());
        salesView.setPrintDocGenerator(new PrintDocGeneratorImpl(annexService, org));

        leftLayout.setCaption(org.getName());

        header.getSettingsDialog().setService(orgService);
        header.getSettingsDialog().setDatasource(this.datasource);
    }

    
    
    private void setContent(String name) {
        
        Component c = null;
        if("ORDERS".equals(name)) {
            c = salesView;
        }
        
        if("STOCK".equals(name)) {
            c = stockWidget;
        }
        
        //if("CONSTRUCTIONLIST".equals(name)) {
        //    c = constructionList;
        //}
        
        if(c==null) {
            c = new Label("Ingen widget til dette område endnu");
        }
        
        
        content.removeAllComponents();
        content.addComponent(c);
    }

}