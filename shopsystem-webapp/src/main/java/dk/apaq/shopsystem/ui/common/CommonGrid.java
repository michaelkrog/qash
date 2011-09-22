package dk.apaq.shopsystem.ui.common;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
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
    private List action = new ArrayList();
    private List actionTarget = new ArrayList();
    
    final private Table table = new Table();
    VerticalLayout content = new VerticalLayout();
    
    
    public void setEdit (Boolean edit) {
        this.edit = edit;
    }
    
    public void setEditCaption (String editCaption) {
        this.editCaption = editCaption;
    }
        
    public void addHeader(String header) {
        this.header.add(header);
    }

    public void addField(String field, String fieldType) {
	this.field.add(field);
        this.fieldType.add(fieldType);
    }
    
    public void addAction(String action, Component actionTarget) {
	this.action.add(action);
        this.actionTarget.add(actionTarget);
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
        
        // Create panel
        for (int i = 0; i < this.action.size(); i++) {
            
        }
        
        // Create table
        this.table.setWidth("100%");
        this.table.setHeight("100%");
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
                //this.table.setNullSelectionAllowed(false); 
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
        
    
        
    public Button OpenInDialog(final String buttonText, final Component target) {
        
        Button button = new Button(buttonText);
        //button.setStyleName(Reindeer.BUTTON_LINK);
        //button.addStyleName("v-accordion-button");
        //button.setIcon(new ThemeResource("../shopsystem/icons/7/dot.png"));
        button.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                CommonDialog dialog = new CommonDialog(buttonText, target);
                getApplication().getMainWindow().addWindow(dialog);
            }
        });
        
        return button;
        
        
    }
}
