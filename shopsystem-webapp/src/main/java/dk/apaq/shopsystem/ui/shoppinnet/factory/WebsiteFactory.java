package dk.apaq.shopsystem.ui.shoppinnet.factory;

import com.vaadin.data.Container;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonForm;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonGrid;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;

public class WebsiteFactory extends AbstractFactory {

    
    private VerticalLayout layout = new VerticalLayout();

   
    public WebsiteFactory() {
        setCompositionRoot(this.layout);
    }
    
    
    public void setOrgService(OrganisationService orgService) {
        
        if (this.orgService == null) {
            this.orgService = orgService;
            this.container = new CrudContainer(this.orgService.getWebsites(), Website.class);
        }
    }
    
            
    public CommonGrid GetList() {
                
        CommonGrid grid = new CommonGrid(this.orgService);

        grid.setContainerDataSource(this.container);
        grid.setFactoryClass(WebsiteFactory.class.getName()); //
        
        grid.setEdit(true);
        grid.setEditCaption("Edit Website");

        // Add buttons
        grid.addButton("Add","Add","");
        grid.addButton("Edit","Edit","");
        grid.addButton("Delete","Delete","");
        
        // Add grid headers
        grid.addHeader("Name");
        grid.addHeader("Created");
       
        // Add grid fields
        grid.addField("name", "");
        grid.addField("dateCreated", "");
        
        // Insert grid into layout
        return grid;
    }
    
    
    public void CreateEdit(String id) {
        
        CommonForm form = new CommonForm();
        
        form.setItemId(id);
        form.setContainerDataSource(this.container);
                
        form.addDescription("Name");
        form.addDescription("Created");
       
        form.addField("name", "");
        form.addField("dateCreated", "");
        
        getApplication().getMainWindow().addWindow(form);
    }
    
    
    public void Add(OrganisationService orgService, String id) {
        
        setOrgService(orgService);
        id = this.container.addItem().toString();   
        CreateEdit(id);
    }
    
    
    public void Edit(OrganisationService orgService, String id) {
        
        setOrgService(orgService);
        CreateEdit(id);
    }
    
    
    public void Delete(OrganisationService orgService, String id) {
        
        setOrgService(orgService);
        //this.container.removeItem(id);
        this.orgService.getWebsites().delete(id);
    }
    
}