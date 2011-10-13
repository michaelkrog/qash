package dk.apaq.shopsystem.ui.shoppinnet.factory;

import dk.apaq.shopsystem.entity.Module;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonForm;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonGrid;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;

public class ModuleFactory extends AbstractFactory {

    
    @Override
    public void setCrudContainer() {
            this.container = new CrudContainer(this.orgService.getModules(), Module.class);
    }
    
            
    public CommonGrid GetList() {
                
        CommonGrid grid = new CommonGrid(this.orgService);

        grid.setContainerDataSource(this.container);
        grid.setFactoryClass(ModuleFactory.class.getName());
        
        grid.setEditAble(true);
        grid.setSearch(false);
        grid.setPageHeader("Modules");
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
        
        form.setItemId(id);
        form.setContainerDataSource(this.container);
        
        form.setHeaderText("Edit Module");
       
        form.addField("name", "Name", "", "");
        getApplication().getMainWindow().addWindow(form);
    }
    
}