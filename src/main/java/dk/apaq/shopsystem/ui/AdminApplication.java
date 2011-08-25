package dk.apaq.shopsystem.ui;

import com.vaadin.Application;
import com.vaadin.ui.Table;
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

    @Override
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



        

    }

    
}
