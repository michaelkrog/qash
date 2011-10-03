package dk.apaq.shopsystem.ui.shoppinnet.common;

import com.vaadin.data.Buffered;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a form combined with common functionality for editing various data
 * 
 * @author Martin Christensen
 */

public class CommonForm extends Window {
    
    private Container data;
    private Item item;
    private String itemId; 
    private List description = new ArrayList();
    private List field = new ArrayList();
    private List fieldType = new ArrayList();
    private Form form = new Form();
    private VerticalLayout content = new VerticalLayout();
    private Label label = new Label();
    
        
    public void setItemId(String value) {
        this.itemId = value;
    }
        
    public void addDescription(String value) {
        this.description.add(value);
    }

    public void addField(String value, String type) {
	this.field.add(value);
        this.fieldType.add(type);
    }

    //@Override
    public Container getContainerDataSource() {
        return this.data;
    }

    //@Override
    public void setContainerDataSource(Container data) {
        this.data = data;
        /*if (this.itemId == null) {
            this.itemId = data.addItem().toString();
        }*/
        this.item = this.data.getItem(this.itemId);
        this.form.setItemDataSource(this.item);
    }
    
    
    @Override
    public void attach() {
        
        setModal(true);
        setWidth("500px");
        addComponent(this.content);
        
        this.content.setMargin(true);
        this.form.setWidth("250px");

         // Enable buffering so that commit() must be called for the form
         // before input is written to the data. (Form input is not written
         // immediately through to the underlying object.)
         this.form.setWriteThrough(true);
         this.form.setImmediate(true);
        
         if(this.data.size() == 0) {
            this.label.setCaption("No data available!");
            this.content.addComponent(label);
        }
        else {
            
            // Add fields to form
            for (int i = 0; i < this.description.size(); i++) {
                
                TextField textField = new TextField(this.description.get(i).toString());
                textField.setNullRepresentation(" ");
                this.form.addField(this.field.get(i).toString(), textField );
                
                //Property property = this.form.getItemProperty(this.field.get(i));  

                String colfieldType = this.fieldType.get(i).toString();
                if ("SomeCustomFieldStuffHereIfNeeded".equals(colfieldType)) {
                     //Some custom field stuff can be added here
                }
            }
            
            this.form.setVisibleItemProperties(new Object[] {});
        
            // Insert form into content
            this.content.addComponent(this.form);
            content.setComponentAlignment(this.form, Alignment.MIDDLE_CENTER);

            Button okButton = new Button("Ok", this, "okButtonClick");   
            this.content.addComponent(okButton);
            this.content.setComponentAlignment(okButton, Alignment.MIDDLE_RIGHT);

        }
    }
    
     
    public void okButtonClick () {
        this.form.commit();
        if(item instanceof Buffered) {
            ((Buffered)item).commit();
        }

        this.close();
    }
    
}