package dk.apaq.shopsystem.ui.shoppinnet.factory;

import com.vaadin.data.Container;
import dk.apaq.shopsystem.entity.Page;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonForm;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonGrid;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import java.util.List;


public class PageFactory extends AbstractFactory {

    private Container websiteContainer;
    
    @Override
    public void setCrudContainer() {
        
            this.websiteContainer = new CrudContainer(this.orgService.getWebsites(), Website.class);
            List websites = this.orgService.getWebsites().listIds(); 
            this.container = new CrudContainer(this.orgService.getPages(this.orgService.getWebsites().read(websites.get(0).toString())), Page.class);   
    }
    
            
    public CommonGrid GetList() {
                
        CommonGrid grid = new CommonGrid(this.orgService);

        grid.setContainerDataSource(this.container);
        grid.setFactoryClass(PageFactory.class.getName()); //
        
        grid.setEditAble(true);
        grid.setSearch(false);
        grid.setPageHeader("Pages");
        grid.addDescription("", "One page can hold multiple modules. A module may contain a text block, a view of categories, productdetails, special offers etc.");
        
        // Add dropdown
        grid.addFilter("website", "Website", this.websiteContainer.getContainerProperty("id", "name"));
        
        // Add buttons
        grid.addButton("Add","AddItem","");
        grid.addButton("Edit","EditItem","");
        grid.addButton("Delete","DeleteItem","");
        
        // Add grid headers
        grid.addHeader("Name");
        grid.addHeader("Title");
       
        // Add grid fields
        grid.addField("name", "");
        grid.addField("title", "");
        
        // Insert grid into layout
        return grid;
    }
    
    
    @Override
    public void ShowEdit(String id) {
        
        CommonForm form = new CommonForm();
        form.setHeaderText("Edit Page");
        
        form.addForm("General");
        form.addItemId(id);
        form.addContainerDataSource(this.container);
        form.addField("name", "Name", "", "", null);
        form.addField("title", "Title", "Shown in search engine results and page header", "", null);
        form.addField("description", "Description", "Shown in search engine results", "", null);
        form.addField("keywords", "Keywords", "Keywords for search engines", "", null);
        //form.addField("website", "Website", "", "select", this.websiteContainer);
        
        getApplication().getMainWindow().addWindow(form);
    }
    
}