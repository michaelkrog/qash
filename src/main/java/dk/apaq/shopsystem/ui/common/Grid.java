package dk.apaq.shopsystem.ui.common;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a grid combined with common functionality for listing various data
 * 
 * @author Martin Christensen
 */

public class Grid extends CustomComponent {
    
    private Object Data;
    private List header = new ArrayList();
    private List field = new ArrayList();
    //private String[] action;
    
    
    public void addHeader(String value) {
	this.header.add(value);
    }

    public void addField(Object value) {
	this.field.add(value);
    }
    
    public void setData(Object data) {
        this.Data = data;
    }
    
    
    public Grid() {
        
        // Create layout
        VerticalLayout content = new VerticalLayout();
        setStyleName("v-orderlist");
        
        // Crete test label
        Label label = new Label("Byggeplads!");
        label.setStyleName(Reindeer.LABEL_H1);
        
        // Create table
        Table table = new Table();
        table.setSizeFull();
        //grid.setVisibleColumns(new Object[]{"delete", "number", "status", "dateChanged", "totalWithTax"});      
        table.addItem(this.Data);
        //table.addItem(new Object[] {"Nicolaus","Copernicus",new Integer(1473)}, new Integer(1));
        
        // Insert grid into layout
        content.addComponent(table);
        
        // Define layout root
        setCompositionRoot(content);
    }
    
}
