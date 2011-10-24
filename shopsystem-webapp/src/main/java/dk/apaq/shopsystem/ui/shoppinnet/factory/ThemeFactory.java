package dk.apaq.shopsystem.ui.shoppinnet.factory;

import dk.apaq.shopsystem.entity.Theme;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonForm;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonGrid;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;

public class ThemeFactory extends AbstractFactory {

    
    @Override
    public void setCrudContainer() {
            this.container = new CrudContainer(this.orgService.getThemes(), Theme.class);
    }
    
            
    public CommonGrid GetList() {
                
        CommonGrid grid = new CommonGrid(this.orgService);

        grid.setContainerDataSource(this.container);
        grid.setFactoryClass(ThemeFactory.class.getName());
        
        grid.setEditAble(true);
        grid.setSearch(false);
        grid.setPageHeader("Themes");
        //grid.addDescription("", "");
        
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
        
        form.setHeaderText("Edit Theme");
       
        form.addField("name", "Name", "", "");
        getApplication().getMainWindow().addWindow(form);
    }
    
}