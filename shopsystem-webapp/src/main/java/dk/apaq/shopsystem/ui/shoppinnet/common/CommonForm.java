package dk.apaq.shopsystem.ui.shoppinnet.common;

import com.vaadin.data.Buffered;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Select;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import dk.apaq.shopsystem.service.OrganisationService;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates a form combined with common functionality for editing various data
 * 
 * @author Martin Christensen
 */

public class CommonForm extends Window {
    
    private OrganisationService orgService;
    private String factoryClass = "";
    private List<Container> data = new ArrayList<Container>();
    private Item item;
    private List<Form> formList = new ArrayList<Form>();
    private List itemId = new ArrayList();
    private String description = "";
    private List field = new ArrayList();
    private List<String> fieldName = new ArrayList();
    private List<String> fieldDescription = new ArrayList();
    private List<String> fieldType = new ArrayList(); // Auto if empty "" // "select"
    private List<Container> fieldData = new ArrayList();
    private VerticalLayout content = new VerticalLayout();
    private List<Component> tabContent = new ArrayList();
    private List<String> tabName = new ArrayList();
    private String headerText = "";
    private VerticalLayout formHolder = new VerticalLayout();
    private GridLayout formLayout = new GridLayout(2, 1);
    private TabSheet tabSheet = new TabSheet();

    private Integer fieldCount = -1;
    private Integer formCount = -1;
        
    
    public CommonForm(OrganisationService orgService) {
        this.orgService = orgService;
        //setCompositionRoot(this.content);
    }
        
    
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
    
    
    public void addField(String field, String fieldName, String fieldDescription, String fieldType, Container fieldData) {
	this.field.add(field);
        this.fieldName.add(fieldName);
        this.fieldDescription.add(fieldDescription);
        this.fieldType.add(fieldType);
        this.fieldData.add(fieldData);
    }
    
    
    public void setHeaderText(String value) {
        this.headerText = value;
    }

    
    public void addContainerDataSource(Container data) {
        this.data.add(data);
        this.item = this.data.get(this.formCount).getItem(this.itemId.get(this.formCount));
        this.form.setItemDataSource(this.item);
    }
    
    
    public void addTab(String name, Component component) {
        this.tabName.add(name);
        this.tabContent.add(component);
    }
    
    
    private Form form = new Form() {
   
        @Override
        public void attachField(Object propertyId, Field f) {
          
            if (field.contains(propertyId)) {
                fieldCount ++;
                
                // Reset curser in grid layout
                if (fieldCount == 0) {
                    formLayout.setCursorX(0);
                    formLayout.setCursorY(0);
                }
                
                Label lName = new Label(fieldName.get(fieldCount).toString());
                lName.addStyleName("margin");
                formLayout.addComponent(lName);
                
                // *** Build the needed field type
                String thisFieldType = fieldType.get(fieldCount).toString();

                // Select box
                if("select".equals(thisFieldType)) {
                    Select select = new Select();
                    select.setContainerDataSource(fieldData.get(fieldCount));
                    //select.setValue("1");
                    select.setItemCaptionPropertyId("name");
                    select.setNullSelectionAllowed(false);
                    super.attachField(propertyId, select);
                }
                
                // External selection. Field is visual, not bound to form
                else if("external_select".equals(thisFieldType)) {
                    ListSelect select = new ListSelect();
                    select.setContainerDataSource(fieldData.get(fieldCount));
                    select.setReadOnly(true);
                    select.setRows(3);
                    select.setWidth("150px");
                    select.setItemCaptionPropertyId("name");
                    select.setNullSelectionAllowed(false);
                    formLayout.addComponent(select);
                    }
                
                // Automated field type
                else {
                    super.attachField(propertyId, f);
                }
                
               
                
                String fDescription = fieldDescription.get(fieldCount).toString();
                if (!fDescription.equals("")) {
                    
                    // Add empty label in the 1st grid field, prior to adding description into 2nd grid field
                    formLayout.addComponent(new Label(""));
                    
                    Label lDescription = new Label(fDescription);
                    lDescription.setWidth("240px");
                    lDescription.addStyleName("description");
                    
                    Resource fieldDescriptionIconResource = new ThemeResource("icons/16/arrow-up.png");
                    Embedded fieldDescriptionIcon  = new Embedded(null, fieldDescriptionIconResource);
    
                    HorizontalLayout fieldDescriptionHolder = new HorizontalLayout();
                    fieldDescriptionHolder.addComponent(fieldDescriptionIcon);
                    fieldDescriptionHolder.addComponent(lDescription);
                    
                    formLayout.addComponent(fieldDescriptionHolder);
                }
                
                // Add spacing to the next field to the 2 column grid
                Label spacer1 = new Label("");
                spacer1.setHeight("10px");
                Label spacer2 = new Label("");
                spacer2.setHeight("10px");
                formLayout.addComponent(spacer1);
                formLayout.addComponent(spacer2);
            }
        }
        
    };
    
    
    
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
            //this.tabSheet.setHeight("500px");

            this.form.setLayout(this.formLayout);
            
            this.form.setFormFieldFactory(new CommonFieldFactory());

             // Enable buffering so that commit() must be called for the form
             // before input is written to the data. (Form input is not written
             // immediately through to the underlying object.)
             this.form.setWriteThrough(false);
             this.form.setImmediate(false);

             
             
             this.form.setVisibleItemProperties(this.field.toArray());
       
            // Insert overall description
             if (!this.description.equals("")) {
                 Label label = new Label(this.description);
                 label.addStyleName("underline");
                 this.content.addComponent(label);
             }
             
            // Insert form into content
            this.tabSheet.addTab(this.formHolder, "General", null);
            
            for (int i = 0; i < this.tabName.size(); i++) {
                this.tabSheet.addTab(this.tabContent.get(i), this.tabName.get(i), null);
            }
            
            
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
    
   
    private Button createButton(final String buttonText, final String buttonMethod, final String buttonTarget) {

        Button button = new Button(buttonText);
        button.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    try {
                        System.out.println("Creating instance: " + factoryClass);
                        Component newInstance = (Component) Class.forName(factoryClass).newInstance();
                        //dummy.addComponent(newInstance);

                        System.out.println("Finding methods...");
                        Method[] allMethods = newInstance.getClass().getMethods();

                        for (Method m : allMethods) {
                            System.out.println("Found: " + m.getName());
                            if (m.getName().equalsIgnoreCase(buttonMethod)) {
                                try {
                                    Object o = m.invoke(newInstance, orgService, itemId.get(formCount));

                                } catch (IllegalArgumentException ex) {
                                    Logger.getLogger(CommonGrid.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (InvocationTargetException ex) {
                                    Logger.getLogger(CommonGrid.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }

                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(CommonGrid.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    } catch (InstantiationException ex) {
                        Logger.getLogger(CommonGrid.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(CommonGrid.class.getName()).log(Level.SEVERE, null, ex);
                }

            
                       
            }
         
        });
                 
          
        return button;
        
    }
}
