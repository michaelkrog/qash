package dk.apaq.shopsystem.ui;

import dk.apaq.shopsystem.ui.view.OverView;
import com.vaadin.Application;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
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
    private SplitPanel verticalSplit = new SplitPanel(
            SplitPanel.ORIENTATION_VERTICAL);
    private SplitPanel horizontalSplit = new SplitPanel(
            SplitPanel.ORIENTATION_HORIZONTAL);
    private NavigationTree tree = new NavigationTree();
    private OverView OverView = null;
    
    
    @Override
    public void init() {

        buildMainLayout();
        setMainComponent(getOverView());
    }

    
    private void buildMainLayout() {
        
        setTheme("reindeermods");
        //setTheme("reindeer");
        setMainWindow(new Window("ShoppinNet - Administration"));
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        
        //@TODO: Center page with a fixed width of 1000px
        //layout.setWidth(1000,Sizeable.UNITS_PIXELS);
        //layout.setHeight("100%");
        //layout.addComponentAsFirst(layout);
        //layout.setComponentAlignment(layout, Alignment.TOP_CENTER);
        
        // Insert top bar
        verticalSplit.setFirstComponent(createTopBar());
        
        // Set the initial split position for a 15 pixel bar in the top
        verticalSplit.setSplitPosition(25, SplitPanel.UNITS_PIXELS);
        
        // Insert tree menu to the left
        horizontalSplit.setFirstComponent(tree);
              
        // Set the initial split position for a 200 pixel menu to the left
        horizontalSplit.setSplitPosition(200, SplitPanel.UNITS_PIXELS);
        
        // Insert horizontal split in the lower (second) part of the vertical split.
        verticalSplit.setSecondComponent(horizontalSplit);

        // Insert vertical split which includes the horizontal split, into the layout
        layout.addComponent(verticalSplit);

        // Allocate all available extra space to the horizontal split panel
        //verticalSplit.setExpandRatio(horizontalSplit, 1);
        
        // Build window
        getMainWindow().setContent(layout);
    }

      
    public MenuBar createTopBar() {
        
        MenuBar mb = new MenuBar();
        mb.addItem("ShoppinNet.com", null);
        mb.setWidth("100%");
        mb.setHeight("100%");

        return mb;
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
