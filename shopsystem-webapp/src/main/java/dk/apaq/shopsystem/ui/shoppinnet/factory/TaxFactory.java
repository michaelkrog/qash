package dk.apaq.shopsystem.ui.shoppinnet.factory;

import dk.apaq.shopsystem.entity.Tax;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonForm;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonGrid;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;

public class TaxFactory extends AbstractFactory {

    
    @Override
    public void setCrudContainer() {
            this.container = new CrudContainer(this.orgService.getTaxes(), Tax.class);
    }
    
            
    public CommonGrid GetList() {
                
        CommonGrid grid = new CommonGrid(this.orgService);

        grid.setContainerDataSource(this.container);
        grid.setFactoryClass(TaxFactory.class.getName());
        
        grid.setEditAble(true);
        grid.setSearch(false);
        grid.setPageHeader("Taxes");
        //grid.addDescription("", "");
        
        // Add buttons
        grid.addButton("Add","AddItem","");
        grid.addButton("Edit","EditItem","");
        grid.addButton("Delete","DeleteItem","");
        
        // Add grid headers
        grid.addHeader("Name");
        grid.addHeader("Rate %");
       
        // Add grid fields
        grid.addField("name", "");
        grid.addField("rate", "");
        
        // Insert grid into layout
        return grid;
    }
    
    
    @Override
    public void ShowEdit(String id) {
        
        CommonForm form = new CommonForm(this.orgService);
        form.setHeaderText("Edit Tax");
        
        form.addForm("General");
        form.addItemId(id);
        form.addContainerDataSource(this.container);
        form.addField("name", "Name", "", "", null);
        form.addField("rate", "Rate %", "", "", null);
        
        getApplication().getMainWindow().addWindow(form);
    }
    
}