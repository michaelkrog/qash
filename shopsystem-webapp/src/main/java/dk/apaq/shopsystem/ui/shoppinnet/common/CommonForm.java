package dk.apaq.shopsystem.ui.shoppinnet.common;

import com.vaadin.data.Buffered;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
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
    
    private List<Container> data = new ArrayList<Container>();
    private Item item;
    private List<Form> formList = new ArrayList<Form>();
    private List itemId = new ArrayList();
    private String description = "";
    private List field = new ArrayList();
    private List fieldName = new ArrayList();
    private List fieldDescription = new ArrayList();
    private List fieldType = new ArrayList();
    private VerticalLayout content = new VerticalLayout();
    private String headerText = "";
    private VerticalLayout formHolder = new VerticalLayout();
    private GridLayout formLayout = new GridLayout(2, 1);
    private TabSheet tabSheet = new TabSheet();

    private Integer fieldCount = -1;
    private Integer formCount = -1;
        
    //private Form form = new Form();
    
    //private List<Form> form = new ArrayList<Form>() {
    //this.formList.add(new form());
    private Form form = new Form() {
   
        @Override
        protected void attachField(Object propertyId, Field f) {
          
            if (field.contains(propertyId)) {
                fieldCount ++;
                
                Label lName = new Label(fieldName.get(fieldCount).toString());
                lName.addStyleName("margin");
                formLayout.addComponent(lName);
                super.attachField(propertyId, f);
                
                String fDescription = fieldDescription.get(fieldCount).toString();
                if (!fDescription.equals("")) {
                    // Add empty label in the field caption area, prior to adding description into 2nd grid field
                    formLayout.addComponent(new Label());
                    
                    Label lDescription = new Label(fDescription);
                    lDescription.setWidth("250px");
                    lDescription.addStyleName("description");
                    
                    Resource fieldDescriptionIconResource = new ThemeResource("icons/16/arrow-up.png");
                    Embedded fieldDescriptionIcon  = new Embedded(null, fieldDescriptionIconResource);
    
                    HorizontalLayout fieldDescriptionHolder = new HorizontalLayout();
                    fieldDescriptionHolder.addComponent(fieldDescriptionIcon);
                    fieldDescriptionHolder.addComponent(lDescription);
                    
                    formLayout.addComponent(fieldDescriptionHolder);
                }
                
                // Add spacing to the next field to the 2 column grid
                Label spacer1 = new Label();
                spacer1.setHeight("10px");
                Label spacer2 = new Label();
                spacer2.setHeight("10px");
                formLayout.addComponent(spacer1);
                formLayout.addComponent(spacer2);
            }
        }
        
    };
    
    
    
    public void addItemId(String value) {
        this.itemId.add(value);
    }
        
    
    public void addDescription(String description) {
        this.description = description;
    }

    
    public void addForm(String form) {
        this.formList.add(new Form());
        this.formCount ++;
    }
    
    
    public void addField(String field, String fieldName, String fieldDescription, String type) {
	this.field.add(field);
        this.fieldName.add(fieldName);
        this.fieldDescription.add(fieldDescription);
        this.fieldType.add(type);
    }
    
    
    public void setHeaderText(String value) {
        this.headerText = value;
    }

    
    //public Container getContainerDataSource() {
    //    return this.data;
    //}

    
    public void addContainerDataSource(Container data) {
        this.data.add(data);
        this.item = this.data.get(this.formCount).getItem(this.itemId.get(this.formCount));
        this.form.setItemDataSource(this.item);
    }
    
    
    @Override
    public void attach() {
        
        setCaption(this.headerText);
        setModal(true);
        setWidth("500px");
        addComponent(this.content);

        this.tabSheet.addStyleName("minimal");
               
        if(this.data.isEmpty()) {
            Label label = new Label();
            label.setCaption("No data available!");
            this.content.addComponent(label);
        }
        else {

            //this.formArray[0] = new Form();

            this.form.setWidth("250px");

            this.form.setLayout(this.formLayout);
            this.form.setFormFieldFactory(new CommonFieldFactory());

             // Enable buffering so that commit() must be called for the form
             // before input is written to the data. (Form input is not written
             // immediately through to the underlying object.)
             this.form.setWriteThrough(false);
             this.form.setImmediate(false);

             
             
             this.form.setVisibleItemProperties(new Object[] {"name", "dateCreated"});
        
            // Insert overall description
             if (!this.description.equals("")) {
                 Label label = new Label(this.description);
                 label.addStyleName("underline");
                 this.content.addComponent(label);
             }
             
            // Insert form into content
            this.tabSheet.addTab(this.formHolder, "General", null);
            this.formHolder.addComponent(this.form);
            this.formHolder.setComponentAlignment(this.form, Alignment.MIDDLE_CENTER);
            this.formLayout.setMargin(true);
            this.content.addComponent(this.tabSheet);

            
            
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
