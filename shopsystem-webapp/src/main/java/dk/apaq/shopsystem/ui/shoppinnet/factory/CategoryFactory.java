package dk.apaq.shopsystem.ui.shoppinnet.factory;


import dk.apaq.shopsystem.entity.ProductCategory;
import dk.apaq.shopsystem.ui.HierarchicalCrudContainer;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonForm;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonGrid;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;

public class CategoryFactory extends AbstractFactory {

    HierarchicalCrudContainer categoryContainer;
   
    
    @Override
    public void setCrudContainer() {
        //this.categoryContainer.s
        this.categoryContainer = new HierarchicalCrudContainer(this.orgService.getProductCategories(), ProductCategory.class);
    }
    

    public CommonGrid GetList() {
                
        CommonGrid grid = new CommonGrid(this.orgService);

        grid.setContainerDataSource(this.container);
        grid.setFactoryClass(CategoryFactory.class.getName());
        
        grid.setEditAble(true);
        grid.setSearch(false);
        //grid.setPageHeader("Product Categories");
        //grid.addDescription("", "A product category may be shown in multiple online stores.");
        
        // Add buttons
        grid.addButton("Add","AddItem","");
        //grid.addButton("Edit","EditItem","");
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
        
        CommonForm form = new CommonForm(this.orgService);
        
        form.addItemId(id);
        form.addContainerDataSource(this.container);
        
        form.setHeaderText("Edit Category");
       
        form.addField("name", "Name", "", "", null);
        getApplication().getMainWindow().addWindow(form);
    }
    
}