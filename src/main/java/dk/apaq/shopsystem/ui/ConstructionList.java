package dk.apaq.shopsystem.ui;

import com.vaadin.data.Container;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.ui.common.Grid;
import dk.apaq.shopsystem.entity.User;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;

public class ConstructionList extends CustomComponent {
   
    // Declare variables
    private VerticalLayout Layout = new VerticalLayout();
    
    
    @Override
    public void attach() {
        
        // Get data
        OrganisationService orgService = VaadinServiceHolder.getService(getApplication());
        Container c = new CrudContainer(orgService.getUsers(), User.class);
        
        // Create grid
        Grid grid = new Grid();
        grid.setData(c);
        
        // Add grid headers
        grid.addHeader("Name");
        grid.addHeader("Address");
        grid.addHeader("Zip");
        grid.addHeader("City");
        
        // Add grid fields
        grid.addField(new Object[]{"name"});
        grid.addField(new Object[]{"address"});
        grid.addField(new Object[]{"zip"});
        grid.addField(new Object[]{"city"});
        
        // Insert grid into layout
        this.Layout.addComponent(grid);
    }
    
    
    public ConstructionList() {
        // Define layout root
        setCompositionRoot(this.Layout);
    }
    
}
