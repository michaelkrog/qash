

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.apaq.shopsystem.ui.common;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Creates a grid combined with common functionality for listing various data
 * 
 * @author Martin Christensen
 */

public class Grid extends CustomComponent {
    
    private Object VisibleColumns;
    private Object Data;
    
    public void setVisibleColumns(Object visibleColumns) {
        this.VisibleColumns = visibleColumns;
    }

    public void setData(Object data) {
        this.Data = data;
    }
    
    
    public Grid() {
        
        VerticalLayout content = new VerticalLayout();
        setStyleName("v-orderlist");
           
        Label label = new Label("Byggeplads!");
        label.setStyleName(Reindeer.LABEL_H1);
        
        // Create table
        Table table = new Table();
        table.setSizeFull();

        //table.setVisibleColumns(this.VisibleColumns);
        
        table.addItem(this.Data);
        //table.addItem(new Object[] {"Nicolaus","Copernicus",new Integer(1473)}, new Integer(1));
        //table.addItem(new Object[] {"Nicolausbnm","Copernicusvj",new Integer(1474)}, new Integer(2));
        
        content.addComponent(label);
        content.addComponent(table);
        
        setCompositionRoot(content);
    }
    
}
