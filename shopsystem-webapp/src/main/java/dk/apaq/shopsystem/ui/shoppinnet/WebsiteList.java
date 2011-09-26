package dk.apaq.shopsystem.ui.shoppinnet;

import dk.apaq.shopsystem.ui.common.CommonGrid;
import com.vaadin.data.Container;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.ui.VaadinServiceHolder;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;

public class WebsiteList extends CustomComponent {
   
    // Declare variables
    private VerticalLayout layout = new VerticalLayout();
    
    
    @Override
    public void attach() {
        
        // Get data
        OrganisationService orgService = VaadinServiceHolder.getService(getApplication());
        Container c = new CrudContainer(orgService.getWebsites(), Website.class);
        
        // Create grid
        CommonGrid grid = new CommonGrid();
        grid.setContainerDataSource(c);
        grid.setEdit(true);
        grid.setEditCaption("Edit Website");

        // Add buttons
        grid.addButton("Add",WebsiteList.class.getName(),"Add","");
        grid.addButton("Edit",WebsiteList.class.getName(),"Edit","");
        grid.addButton("Delete",WebsiteList.class.getName(),"Delete","");
        
        // Add grid headers
        grid.addHeader("Name");
        grid.addHeader("Created");
       
        // Add grid fields
        grid.addField("name", "");
        grid.addField("dateCreated", "");
        
        // Insert grid into layout
        this.layout.addComponent(grid);
    }
    
    
    public WebsiteList() {
        
        // Define layout root
        setCompositionRoot(this.layout);
    }
    
    
}
