package dk.apaq.shopsystem.ui.common;

import com.vaadin.data.Container;
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

public class Grid extends CustomComponent implements Container.Viewer {
    
    private Container data;
    private List header = new ArrayList();
    private List headerType = new ArrayList();
    private List field = new ArrayList();
    private Table table = new Table();
    
    
    public void addHeader(String value, String type) {
        this.header.add(value);
        this.headerType.add(value);
    }

    public void addField(String value) {
	this.field.add(value);
    }

    @Override
    public Container getContainerDataSource() {
        return this.data;
    }

    
    @Override
    public void setContainerDataSource(Container data) {
        this.data = data;
        table.setContainerDataSource(data);
    }
    
    
    public Grid() {
        
        // Create layout
        VerticalLayout content = new VerticalLayout();
        setStyleName("v-orderlist");
        
        // Create table
        table.setSizeFull();
        table.setPageLength(50);
        //grid.setVisibleColumns(new Object[]{"delete", "number", "status", "dateChanged", "totalWithTax"});      
        
        if(this.data == null) {
            table.addContainerProperty("Information", String.class,  null);
            table.addItem(new Object[] {"No data available!"}, new Integer(1));
        }
        else {
            for (int i = 0; i < this.header.size(); i++) {
                
                if (this.headerType.get(i) == "string") {
                  table.addContainerProperty(this.header.get(i), String.class,  null); 
                }
                if (this.headerType.get(i) == "boolean") {
                  table.addContainerProperty(this.header.get(i), Boolean.class,  null); 
                }
                // Which import for date?
                //if (this.HeaderType.get(i) == "date") {
                //  table.addContainerProperty(this.Header.get(i), Date.class,  null); 
                //}
            }
            
            table.addItem(this.data);
            table.setVisibleColumns(this.field.toArray());
        }
        
        // Insert components into content
        content.addComponent(table);
        
        // Define layout root
        setCompositionRoot(content);
    }
    
}
