package dk.apaq.shopsystem.ui;

import com.vaadin.data.Container;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.entity.BaseUser;
import dk.apaq.shopsystem.ui.common.Grid;
import dk.apaq.shopsystem.entity.User;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;

public class ConstructionList extends CustomComponent {
   
    // Declare variables
    private VerticalLayout Layout = new VerticalLayout();
    
    
    @Override
    public void attach() {
        
        // Get data
        OrganisationService orgService = VaadinServiceHolder.getService(getApplication());
        Container c = new CrudContainer(orgService.getUsers(), BaseUser.class);
        
        // Create grid
        Grid grid = new Grid();
        grid.setData(c);
        
        // Add grid headers
        grid.addHeader("Name", "string");
        grid.addHeader("Created", "string");
       
        // Add grid fields
        grid.addField("name");
        grid.addField("dateCreated");
        
        // Insert grid into layout
        this.Layout.addComponent(grid);
    }
    
    
    public ConstructionList() {
        
        // Define layout root
        setCompositionRoot(this.Layout);
    }
    
}
