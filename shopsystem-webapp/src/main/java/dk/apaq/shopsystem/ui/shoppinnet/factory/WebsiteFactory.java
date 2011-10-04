package dk.apaq.shopsystem.ui.shoppinnet.factory;

import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonForm;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonGrid;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;

public class WebsiteFactory extends AbstractFactory {

    
    @Override
    public void setCrudContainer() {
            this.container = new CrudContainer(this.orgService.getWebsites(), Website.class);
    }
    
            
    public CommonGrid GetList() {
                
        CommonGrid grid = new CommonGrid(this.orgService);

        grid.setContainerDataSource(this.container);
        grid.setFactoryClass(WebsiteFactory.class.getName()); //
        
        grid.setEditAble(true);
        grid.setSearch(false);
        grid.setPageHeader("Online Stores");
        grid.addDescription("If you have more than one online store, you are able to view those of your product groups in it you want.");
        
        // Add buttons
        grid.addButton("Add","AddItem","");
        grid.addButton("Edit","EditItem","");
        grid.addButton("Delete","DeleteItem","");
        
        // Add grid headers
        grid.addHeader("Name");
        grid.addHeader("Created");
       
        // Add grid fields
        grid.addField("name", "");
        grid.addField("dateCreated", "");
        
        // Insert grid into layout
        return grid;
    }
    
    
    @Override
    public void ShowEdit(String id) {
        
        CommonForm form = new CommonForm();
        
        form.setItemId(id);
        form.setContainerDataSource(this.container);
        
        form.setHeaderText("Edit website");
        
        form.addDescription("Name");
        form.addDescription("Created");
       
        form.addField("name", "");
        form.addField("dateCreated", "");
        
        getApplication().getMainWindow().addWindow(form);
    }
    
}