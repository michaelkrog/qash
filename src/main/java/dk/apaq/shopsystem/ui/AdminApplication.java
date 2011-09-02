package dk.apaq.shopsystem.ui;

import dk.apaq.shopsystem.ui.CategoryList.SelectEvent;
import dk.apaq.shopsystem.ui.view.OverView;
import com.vaadin.Application;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.Service;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author krog
 */

public class AdminApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(AdminApplication.class);

    /*@Override
    public void init() {

        ApplicationContext context =  VaadinSpringHelper.getSpringContextFromVaadinContext(this.getContext());
        Service service = context.getBean(Service.class);

        OrganisationCrud crud = service.getOrganisationCrud();
        CrudContainer container = new CrudContainer(crud, Organisation.class);

        Window mainWindow = new Window();
        setMainWindow(mainWindow);

        Table table = new Table();
        table.setContainerDataSource(container);
        table.setVisibleColumns(new Object[] {"name", "address", "telephone"});
        table.setSizeFull();
        
        mainWindow.addComponent(table);
    }*/
    
    private Button newContact = new Button("Add contact");
    private Button search = new Button("Search");
    private Button share = new Button("Share");
    private Button help = new Button("Help");
    private SplitPanel horizontalSplit = new SplitPanel(
            SplitPanel.ORIENTATION_HORIZONTAL);
    private NavigationTree tree = new NavigationTree();
    private CategoryList categoryList = new CategoryList();
    private OverView OverView = null;
    private ListListener listListener = new ListListener();
    
    
    @Override
    public void init() {

        buildMainLayout();
        setMainComponent(getOverView());
    }

    private class ListListener implements CategoryList.SelectListener {

        @Override
        public void onSelect(SelectEvent event) {
            getMainWindow().showNotification(event.getSelection());
        }
    
    }
    
    private void buildMainLayout() {
        
        setTheme("shopsystem");
        setMainWindow(new Window("ShoppinNet - Administration"));
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth(1000,Sizeable.UNITS_PIXELS);
        layout.setHeight("100%");
        
        //@TODO: Center page with a fixed width of 1000px
        //layout.addComponentAsFirst(layout);
        //layout.setComponentAlignment(layout, Alignment.TOP_CENTER);
        
        categoryList.setSizeFull();
        categoryList.addListener(listListener);
        categoryList.addCategory("Generelt");
        categoryList.addItem("Brugere", "USERS");
        categoryList.addItem("Produkter", "PRODUCTS");
        categoryList.addItem("Ordrer", "ORDERS");
        categoryList.addCategory("Web");
        categoryList.addItem("Website", "WEBSITES");
        
        
        horizontalSplit.setFirstComponent(categoryList);
        //layout.addComponent(createToolbar());
        layout.addComponent(horizontalSplit);
        

        /* Allocate all available extra space to the horizontal split panel */
        layout.setExpandRatio(horizontalSplit, 1);

        /* Set the initial split position so we can have a 200 pixel menu to the left */
        horizontalSplit.setSplitPosition(200, SplitPanel.UNITS_PIXELS);

        getMainWindow().setContent(layout);
    }

    
    public HorizontalLayout createToolbar() {

        HorizontalLayout hl = new HorizontalLayout();
        hl.addComponent(newContact);
        hl.addComponent(search);
        hl.addComponent(share);
        hl.addComponent(help);

        return hl;
    }
    
    
    private void setMainComponent(Component c) {
        horizontalSplit.setSecondComponent(c);
     }

    
     private OverView getOverView() {
         
        if (OverView == null) {
         OverView = new OverView();
        }
        
        return OverView;
     }
}
