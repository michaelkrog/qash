package dk.apaq.shopsystem.ui;

import com.vaadin.data.Container;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.entity.Product;
import dk.apaq.shopsystem.ui.common.CommonGrid;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;

public class ConstructionList extends CustomComponent {
   
    // Declare variables
    private VerticalLayout layout = new VerticalLayout();
    
    
    @Override
    public void attach() {
        
        // Get data
        OrganisationService orgService = VaadinServiceHolder.getService(getApplication());
        Container c = new CrudContainer(orgService.getProducts(), Product.class);
        
        // Create grid
        CommonGrid grid = new CommonGrid();
        grid.setContainerDataSource(c);
        grid.setEdit(true);
        grid.setEditCaption("Edit product");
        
        // Add grid headers
        grid.addHeader("Name");
        grid.addHeader("Created");
       
        // Add grid fields
        grid.addField("name", "");
        grid.addField("dateCreated", "");
        
        // Insert grid into layout
        this.layout.addComponent(grid);
        //this.layout.addComponent(form);
    }
    
    
    public ConstructionList() {
        
        // Define layout root
        setCompositionRoot(this.layout);
    }
    
}
