package dk.apaq.shopsystem.ui.shoppinnet.factory;

import dk.apaq.shopsystem.entity.Category;
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
        grid.setSearch(true);
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
        
        CommonForm form = new CommonForm(this.orgService);
        form.setHeaderText("Edit Product");
        
        form.addForm("General");
        form.addItemId(id);
        form.addContainerDataSource(this.container);
        //form.addField("category", "Categories", "", "external_select", new CrudContainer(this.orgService.getCategories(), Category.class));
        form.addField("name", "Name", "", "", null);
        form.addField("itemNo", "Item Number", "", "", null);
        form.addField("price", "Price", "", "", null);
        form.addField("quantityInStock", "Quantity In Stock", "", "", null);
        form.addField("stockProduct", "Stock Product", "If not, product is never in stock but may be ordered anyway", "", null);
        
        // Add category grid
        CategoryFactory categoryFactory = new CategoryFactory();
        categoryFactory.setOrgService(orgService);
        form.addTab("Categories", categoryFactory.GetList());
        
        getApplication().getMainWindow().addWindow(form);
    }
    
}