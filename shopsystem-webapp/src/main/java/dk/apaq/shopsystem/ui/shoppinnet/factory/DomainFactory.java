package dk.apaq.shopsystem.ui.shoppinnet.factory;

import dk.apaq.shopsystem.entity.Domain;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonForm;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonGrid;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;

public class DomainFactory extends AbstractFactory {

    
    @Override
    public void setCrudContainer() {
            this.container = new CrudContainer(this.orgService.getDomains(), Domain.class);
    }
    
            
    public CommonGrid GetList() {
                
        CommonGrid grid = new CommonGrid(this.orgService);

        grid.setContainerDataSource(this.container);
        grid.setFactoryClass(DomainFactory.class.getName());
        
        grid.setEditAble(true);
        grid.setSearch(false);
        grid.setPageHeader("Domains");
        grid.addDescription("", "If you want to get rid of the Shoppinnet.com extension in the url of your online store, you are able to add your personal domain you have bought anywhere.");
        grid.addDescription("", "The domain must be setup in the system of the domain provider, and must have a CNAME record pointing to: sites.shoppinnet.com");
        
        // Add buttons
        grid.addButton("Add","AddItem","");
        grid.addButton("Edit","EditItem","");
        grid.addButton("Delete","DeleteItem","");
        
        // Add grid headers
        grid.addHeader("Name");
       
        // Add grid fields
        grid.addField("name", "");
        
        // Insert grid into layout
        return grid;
    }
    
    
    @Override
    public void ShowEdit(String id) {
        
        CommonForm form = new CommonForm();
        
        form.addItemId(id);
        form.addContainerDataSource(this.container);
        
        form.setHeaderText("Edit Domain");
       
        form.addField("name", "Name", "Domain name including www.", "");
        getApplication().getMainWindow().addWindow(form);
    }
    
}