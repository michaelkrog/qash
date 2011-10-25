package dk.apaq.shopsystem.ui.shoppinnet.factory;

import dk.apaq.shopsystem.entity.Product;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonForm;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonGrid;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;

public class ProductFactory extends AbstractFactory {

    
    @Override
    public void setCrudContainer() {
            this.container = new CrudContainer(this.orgService.getProducts(), Product.class);
    }
    
            
    public CommonGrid GetList() {
                
        CommonGrid grid = new CommonGrid(this.orgService);

        grid.setContainerDataSource(this.container);
        grid.setFactoryClass(ProductFactory.class.getName()); //
        
        grid.setEditAble(true);
        grid.setSearch(false);
        grid.setPageHeader("Products");
        //grid.addDescription("", "");
        
        // Add buttons
        grid.addButton("Add","AddItem","");
        grid.addButton("Edit","EditItem","");
        grid.addButton("Delete","DeleteItem","");
        
        // Add grid headers
        grid.addHeader("Name");
        grid.addHeader("Item Number");
        grid.addHeader("Price");
        grid.addHeader("In Stock");
       
        // Add grid fields
        grid.addField("name", "");
        grid.addField("itemNo", "");
        grid.addField("price", "");
        grid.addField("quantityInStock", "");
        
        // Insert grid into layout
        return grid;
    }
    
    
    @Override
    public void ShowEdit(String id) {
        
        CommonForm form = new CommonForm();
        form.setHeaderText("Edit Product");
        
        form.addForm("General");
        form.addItemId(id);
        form.addContainerDataSource(this.container);
        form.addField("name", "Name", "", "");
        form.addField("itemNo", "Item Number", "", "");
        form.addField("price", "Price", "", "");
        form.addField("quantityInStock", "Quantity In Stock", "", "");
        form.addField("stockProduct", "Stock Product", "If not, product is never in stock but may be ordered anyway", "");
                
        getApplication().getMainWindow().addWindow(form);
    }
    
}