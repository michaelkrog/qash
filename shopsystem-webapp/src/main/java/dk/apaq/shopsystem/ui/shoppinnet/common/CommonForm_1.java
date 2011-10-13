package dk.apaq.shopsystem.ui.shoppinnet.common;

import com.vaadin.data.Buffered;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
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

public class CommonForm_1 extends Window {
    
    private Container data;
    private Item item;
    private String itemId; 
    private List description = new ArrayList();
    private List field = new ArrayList();
    private List fieldDescription = new ArrayList();
    private List fieldType = new ArrayList();
    private VerticalLayout content = new VerticalLayout();
    private String headerText = "";
    
        
    private Form form = new Form();
    /*final private Form form = new Form() {
        HorizontalLayout hor = null;

        @Override
        protected void attachField(Object propertyId, Field field) {
            // Group consecutive check boxes on the same line
            if (field instanceof Label) {
                if (hor == null) {
                    hor = new HorizontalLayout();

                    // Add it to the FormLayout
                    getLayout().addComponent(hor);
                }
                hor.addComponent(field);
            } else { // Some other type of field
                hor = null;
                //super.attachField(propertyId, field);
            }
        }
    };*/
    
    
    
    public void setItemId(String value) {
        this.itemId = value;
    }
        
    
    public void addDescription(String value) {
        this.description.add(value);
    }

    
    public void addField(String field, String fieldDescription, String type) {
	this.field.add(field);
        this.fieldDescription.add(fieldDescription);
        this.fieldType.add(type);
    }
    
    
    public void setHeaderText(String value) {
        this.headerText = value;
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
        
        setCaption(this.headerText);
        setModal(true);
        setWidth("500px");
        addComponent(this.content);
        
        this.content.setMargin(true);
        this.form.setWidth("250px");

         // Enable buffering so that commit() must be called for the form
         // before input is written to the data. (Form input is not written
         // immediately through to the underlying object.)
         this.form.setWriteThrough(false);
         this.form.setImmediate(false);
        
         if(this.data.size() == 0) {
            Label label = new Label();
            label.setCaption("No data available!");
            this.content.addComponent(label);
        }
        else {
            
            // Add fields to form
            for (int i = 0; i < this.field.size(); i++) {
                
                TextField textField = new TextField();
                textField.setNullRepresentation("");
                
                if (!"".equals(this.fieldDescription.get(i).toString())) {
                    Label label = new Label(this.fieldDescription.get(i).toString());
                    label.setIcon(new ThemeResource("icons/16/attention.png"));
                    label.addStyleName("description");
                    this.form.getLayout().addComponent(label);
                
                }
               
                this.form.addField(this.field.get(i).toString(), textField);
               
        
                
                
                //Property property = this.form.getItemProperty(this.field.get(i));  

                String colfieldType = this.fieldType.get(i).toString();
                if ("SomeCustomFieldStuffHereIfNeeded".equals(colfieldType)) {
                     //Some custom field stuff can be added here
                }
            }
            
            this.form.setVisibleItemProperties(new Object[] {"name", "dateCreated"});
        
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