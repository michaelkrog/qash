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
        grid.addDescription("Multiple stores", "If you have more than one online store, you are able to view those of your product groups in it you want.");
        grid.addDescription("Product groups", "A specific product group, is able to be shown in multiple online stores, for easy administration across stores. In this way, you can have multiple stores, targeting different groups of people.");
        
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
        
        form.setItemId(id);
        form.setContainerDataSource(this.container);
        
        form.setHeaderText("Edit online store");
       
        form.addField("name", "Name test", "This is not the url, but a descriptional name", "");
        form.addField("dateCreated", "Created test", "", "");
        getApplication().getMainWindow().addWindow(form);
    }
    
}