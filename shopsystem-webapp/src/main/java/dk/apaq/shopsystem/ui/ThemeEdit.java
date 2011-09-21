package dk.apaq.shopsystem.ui;

import com.vaadin.data.Container;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.entity.Theme;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.ui.common.CommonForm;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;

public class ThemeEdit extends CustomComponent {
   
    // Declare variables
    private VerticalLayout layout = new VerticalLayout();
    String itemId;
    
    @Override
    public void attach() {
        
        // Get data
        OrganisationService orgService = VaadinServiceHolder.getService(getApplication());
        Container c = new CrudContainer(orgService.getThemes(), Theme.class);
        
        // Create form
        CommonForm form = new CommonForm();
        
        // Set the items id to be shown in the form
        form.setItemId(this.itemId);
        
        // Bind data to the form
        form.setContainerDataSource(c);
        
        // Add form descriptions
        
        form.addDescription("Name");
        form.addDescription("Created");
       
        // Add form fields
        form.addField("name", "");
        form.addField("dateCreated", "");
        
        // Insert form into layout
        this.layout.addComponent(form);
    }
    
    
    public ThemeEdit(String id) {
        this.itemId = id;
        
        // Define layout root
        setCompositionRoot(this.layout);
    }
    
    public ThemeEdit() {
        this.itemId = null;
        
        // Define layout root
        setCompositionRoot(this.layout);
    }
    
}

