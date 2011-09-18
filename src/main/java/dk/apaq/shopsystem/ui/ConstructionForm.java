package dk.apaq.shopsystem.ui;

import com.vaadin.data.Container;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Window;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.entity.Product;
import dk.apaq.shopsystem.ui.common.CommonGrid;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.ui.common.CommonForm;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;

public class ConstructionForm extends CustomComponent {
   
    // Declare variables
    private VerticalLayout layout = new VerticalLayout();
    String itemId;
    
    @Override
    public void attach() {
        
        // Get data
        OrganisationService orgService = VaadinServiceHolder.getService(getApplication());
        Container c = new CrudContainer(orgService.getProducts(), Product.class);
        
        // Create form
        CommonForm form = new CommonForm();
        form.setContainerDataSource(c);
        
        // Set the items id to be shown in the form
        form.setItemId(this.itemId);
        
        // Add form descriptions
        form.addDescription("Name");
        form.addDescription("Price");
        //form.addDescription("Changed");
       
        // Add form fields
        form.addField("name", "");
        form.addField("price", "");
        //form.addField("dateChanged", "");
        
        // Insert form into layout
        this.layout.addComponent(form);
        //this.layout.addComponent(form);
    }
    
    
    public ConstructionForm(String id) {
        this.itemId = id;
        
        // Define layout root
        setCompositionRoot(this.layout);
    }
    
}
