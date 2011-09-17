package dk.apaq.shopsystem.ui;

import com.vaadin.data.Container;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.entity.BaseUser;
import dk.apaq.shopsystem.entity.Product;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.ui.common.Grid;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.ui.common.Form;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;

public class ConstructionForm extends CustomComponent {
   
    // Declare variables
    private VerticalLayout layout = new VerticalLayout();
    
    
    @Override
    public void attach() {
        
        // Get data
        OrganisationService orgService = VaadinServiceHolder.getService(getApplication());
        //Container c = new CrudContainer(orgService.getProducts().read("1"), Product.class);
        
        // Create grid
        Grid grid = new Grid();
        //grid.setContainerDataSource(c);
        
        // Add grid headers
        grid.addHeader("Name", "");
        grid.addHeader("Created", "");
       
        // Add grid fields
        grid.addField("name");
        grid.addField("dateCreated");
        
        //Form form = new Form();
        
        
        // Insert grid into layout
        this.layout.addComponent(grid);
        //this.layout.addComponent(form);
    }
    
    
    public ConstructionForm() {
        
        // Define layout root
        setCompositionRoot(this.layout);
    }
    
}
