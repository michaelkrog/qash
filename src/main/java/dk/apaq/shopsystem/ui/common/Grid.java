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
    VerticalLayout content = new VerticalLayout();
    
    
    public void addHeader(String value, String type) {
        this.header.add(value);
        this.headerType.add(type);
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
    
    
    @Override
    public void attach() {
        
        // Create layout
        setStyleName("v-orderlist");
        
        // Create table
        this.table.setSizeFull();
        this.table.setPageLength(30);
        
        if(this.data.size() == 0) {
            this.table.setContainerDataSource(null);
            this.table.addContainerProperty("Information", String.class,  null);
            this.table.addItem(new Object[] {"No data available!"}, new Integer(1));
        }
        else {
            for (int i = 0; i < this.header.size(); i++) {
                
                this.table.setColumnHeader(this.field.get(i), this.header.get(i).toString());
                
                String colHeaderType = this.headerType.get(i).toString();
                if ("SomeCustomFieldStuffHereIfNeeded".equals(colHeaderType)) {
                     //Some custom field stuff can be added here
                }
            }
            
            // Show only the needed columns, hide the rest
            table.setVisibleColumns(this.field.toArray());
        }
        
        // Insert components into content
        this.content.addComponent(table);
        
        //Form form = new Form();
        //this.content.addComponent(form);
    }
    
    
    public Grid() {
        
        // Define layout root
        setCompositionRoot(this.content);
    }
        
}
