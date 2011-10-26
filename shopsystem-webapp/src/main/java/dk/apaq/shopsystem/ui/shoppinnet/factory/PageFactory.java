package dk.apaq.shopsystem.ui.shoppinnet.factory;

import dk.apaq.shopsystem.entity.Page;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonForm;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonGrid;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;

public class PageFactory extends AbstractFactory {

    
    @Override
    public void setCrudContainer() {
            this.container = new CrudContainer(this.orgService.getPages(this.orgService.getWebsites().read("4028818832fd94b60132fd9706190002")), Page.class);
    }
    
            
    public CommonGrid GetList() {
                
        CommonGrid grid = new CommonGrid(this.orgService);

        grid.setContainerDataSource(this.container);
        grid.setFactoryClass(PageFactory.class.getName()); //
        
        grid.setEditAble(true);
        grid.setSearch(false);
        grid.setPageHeader("Pages");
        grid.addDescription("", "One page can hold multiple modules. A module may contain a text block, a view of categories, productdetails, special offers etc.");
        
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
        form.addField("name", "Name", "", "");
        form.addField("title", "Title", "Shown in search engine results and page header", "");
        form.addField("description", "Description", "Shown in search engine results", "");
        form.addField("keywords", "Keywords", "Keywords for search engines, must match your website content", "");
        
        getApplication().getMainWindow().addWindow(form);
    }
    
}