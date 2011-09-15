package dk.apaq.shopsystem.ui.common;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a grid combined with common functionality for listing various data
 * 
 * @author Martin Christensen
 */

public class Grid extends CustomComponent {
    
    private Object Data;
    private List Header = new ArrayList();
    private List HeaderType = new ArrayList();
    private List Field = new ArrayList();
    
    
    public void addHeader(String value, String type) {
        this.Header.add(value);
        this.HeaderType.add(value);
    }

    public void addField(String value) {
	this.Field.add(value);
    }
    
    @Override
    public void setData(Object data) {
        this.Data = data;
    }
    
    
    public Grid() {
        
        // Create layout
        VerticalLayout content = new VerticalLayout();
        setStyleName("v-orderlist");
        
        // Create table
        Table table = new Table();
        table.setSizeFull();
        table.setPageLength(50);
        //grid.setVisibleColumns(new Object[]{"delete", "number", "status", "dateChanged", "totalWithTax"});      
        
        if(this.Data == null) {
            table.addContainerProperty("Information", String.class,  null);
            table.addItem(new Object[] {"No data available!"}, new Integer(1));
        }
        else {
            for (int i = 0; i < this.Header.size(); i++) {
                
                if (this.HeaderType.get(i) == "string") {
                  table.addContainerProperty(this.Header.get(i), String.class,  null); 
                }
                if (this.HeaderType.get(i) == "boolean") {
                  table.addContainerProperty(this.Header.get(i), Boolean.class,  null); 
                }
                // Which import for date?
                //if (this.HeaderType.get(i) == "date") {
                //  table.addContainerProperty(this.Header.get(i), Date.class,  null); 
                //}
            }
            
            table.addItem(this.Data);
            table.setVisibleColumns(this.Field.toArray());
        }
        
        // Insert components into content
        content.addComponent(table);
        
        // Define layout root
        setCompositionRoot(content);
    }
    
}
