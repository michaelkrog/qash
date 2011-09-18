package dk.apaq.shopsystem.ui.common;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.ui.ConstructionForm;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a grid combined with common functionality for listing various data
 * 
 * @author Martin Christensen
 */

public class CommonGrid extends CustomComponent implements Container.Viewer {
    
    private Container data;
    private Boolean edit = false;
    private String editCaption = "";
    private List header = new ArrayList();
    private List field = new ArrayList();
    private List fieldType = new ArrayList();
    final private Table table = new Table();
    VerticalLayout content = new VerticalLayout();
    
    
    public void setEdit (Boolean value) {
        this.edit = value;
    }
    
    public void setEditCaption (String value) {
        this.editCaption = value;
    }
        
    public void addHeader(String value) {
        this.header.add(value);
    }

    public void addField(String value, String type) {
	this.field.add(value);
        this.fieldType.add(type);
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
        
        // Create table
        this.table.setSizeFull();
        this.table.setPageLength(30);
        
        if(this.data.size() == 0) {
            this.table.setContainerDataSource(null);
            this.table.addContainerProperty("", String.class,  null);
            this.table.addItem(new Object[] {"No data available!"}, new Integer(1));
        }
        else {
            for (int i = 0; i < this.header.size(); i++) {
                
                this.table.setColumnHeader(this.field.get(i), this.header.get(i).toString());
                
                String colfieldType = this.fieldType.get(i).toString();
                if ("SomeCustomFieldStuffHereIfNeeded".equals(colfieldType)) {
                     //Some custom field stuff can be added here
                }
            }
            
            // Handle selection change, if enabled
            if (this.edit == true) {
                this.table.setSelectable(true);
                this.table.setImmediate(true);
                this.table.addListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        EditItem(table.getValue().toString());
                    }
                });
            }
     
            // Show only the needed columns, hide the rest
            this.table.setVisibleColumns(this.field.toArray());
        }
        
        // Insert components into content
        this.content.addComponent(table);
        

    }
    
    
    public CommonGrid() {
        
        // Define layout root
        setCompositionRoot(this.content);
    }
    
    
    public void EditItem(String itemId) {
        
        // Edit the item, using the common form
        CommonDialog dialog = new CommonDialog(this.editCaption, new ConstructionForm(itemId));
        getApplication().getMainWindow().addWindow(dialog);
    }
        
}
