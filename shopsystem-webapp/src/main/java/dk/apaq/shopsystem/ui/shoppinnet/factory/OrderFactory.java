package dk.apaq.shopsystem.ui.shoppinnet.factory;

import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonForm;
import dk.apaq.shopsystem.ui.shoppinnet.common.CommonGrid;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;

public class OrderFactory extends AbstractFactory {

    
    @Override
    public void setCrudContainer() {
            this.container = new CrudContainer(this.orgService.getOrders(), Order.class);
    }
    
            
    public CommonGrid GetList() {
                
        CommonGrid grid = new CommonGrid(this.orgService);

        grid.setContainerDataSource(this.container);
        grid.setFactoryClass(OrderFactory.class.getName());
        
        grid.setEditAble(true);
        grid.setSearch(false);
        grid.setPageHeader("Orders");
        
        // Add buttons
        grid.addButton("View","ShowInvoice","");
        //grid.addButton("Add","AddItem","");
        grid.addButton("Edit","EditItem","");
        //grid.addButton("Delete","DeleteItem","");
        
        // Add grid headers
        grid.addHeader("Invoice");
        grid.addHeader("Date");
        grid.addHeader("Status");
       
        // Add grid fields
        grid.addField("invoiceNumber", "");
        grid.addField("dateCreated", "");
        grid.addField("status", "");
        
        // Insert grid into layout
        return grid;
    }
    
    
    @Override
    public void ShowEdit(String id) {
        
        CommonForm form = new CommonForm(this.orgService);
        form.setHeaderText("Edit Order");
        
        form.addForm("General");
        form.addItemId(id);
        form.addContainerDataSource(this.container);
        form.addField("invoiceNumber", "Invoice", "", "", null);
        
        getApplication().getMainWindow().addWindow(form);
    }
    
    
     public void ShowInvoice(String id) {
        System.out.println("ShowInvoice...");
    }
}