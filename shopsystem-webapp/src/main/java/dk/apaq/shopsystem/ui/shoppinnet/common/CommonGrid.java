package dk.apaq.shopsystem.ui.shoppinnet.common;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.service.OrganisationService;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates a grid combined with common functionality for listing various data
 * 
 * @author Martin Christensen
 */

public class CommonGrid extends CustomComponent implements Container.Viewer {
    
    private Container data;
    private String factoryClass = "";
    private Boolean edit = false;
    private String editCaption = "";
    private List header = new ArrayList();
    private List field = new ArrayList();
    private List fieldType = new ArrayList();
    private List button = new ArrayList();
    private List buttonMethod = new ArrayList();
    private List buttonTarget = new ArrayList();
    
    final private OrganisationService orgService;
    final private Table table = new Table();
    final VerticalLayout content = new VerticalLayout();
    final VerticalLayout dummy = new VerticalLayout();
    
    
    public void setFactoryClass (String factoryClass) {
        this.factoryClass = factoryClass;
    }
        
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
    
    public void addButton(String button, String buttonMethod, String buttonTarget) {
        
	this.button.add(button);
        this.buttonMethod.add(buttonMethod);
        this.buttonTarget.add(buttonTarget);
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
        
        this.content.addComponent(this.dummy);
        
        // Create panel
        for (int i = 0; i < this.button.size(); i++) {
            this.content.addComponent(createButton(this.button.get(i).toString(),this.buttonMethod.get(i).toString(),this.buttonTarget.get(i).toString()));
        }
        
        // Create table
        this.table.setWidth("100%");
        //this.table.setHeight("100%");
        this.table.setPageLength(20);
        
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
                this.table.setNullSelectionAllowed(false); 
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
    
    
    public CommonGrid(OrganisationService orgService) {
        
        this.orgService = orgService;
        setCompositionRoot(this.content);
    }
    
    
    public void EditItem(String itemId) {
        
        // Edit the item, using the common form
        //CommonDialog dialog = new CommonDialog(this.editCaption, new ConstructionForm(itemId));
        //getApplication().getMainWindow().addWindow(dialog);
    }
        
    
        
    private Button createButton(final String buttonText, final String buttonMethod, final String buttonTarget) {

        Button button = new Button(buttonText);
        //buttonComponent.get
        //button.setStyleName(Reindeer.BUTTON_LINK);
        //button.addStyleName("v-accordion-button");
        //button.setIcon(new ThemeResource("../shopsystem/icons/7/dot.png"));
        button.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                    
                if ("dialog".equals(buttonTarget)) {
                    // ?
                    //CommonDialog dialog = new CommonDialog(buttonText, buttonComponent(buttonTarget));
                    //getApplication().getMainWindow().addWindow(dialog);
                }
                if ("content".equals(buttonTarget)) {
                    // ?
                }
                if (!"dialog".equals(buttonTarget) && !"content".equals(buttonTarget)) {

                    try {
                        try {
                            System.out.println("Creating instance: " + factoryClass);
                            Component newInstance = (Component) Class.forName(factoryClass).newInstance();
                            dummy.addComponent(newInstance);

                            System.out.println("Finding methods...");
                            Method[] allMethods = newInstance.getClass().getDeclaredMethods();

                            for (Method m : allMethods) {
                                System.out.println("Found: " + m.getName());
                                if (m.getName().equalsIgnoreCase(buttonMethod)) {
                                    try {
                                       String tableId = "";
                                        if (table.getValue() == null) {
                                            tableId = null;
                                        } 
                                        else {
                                            tableId = table.getValue().toString();
                                        }
                                        
                                        //newInstance.getClass().getDeclaredMethod("setOrgService", orgService);
                                        Object o = m.invoke(newInstance, orgService, tableId); //new Locale(buttonMethod)
                                        //System.out.println(buttonMethod + ":" + o);

                                    } catch (IllegalArgumentException ex) {
                                        Logger.getLogger(CommonGrid.class.getName()).log(Level.SEVERE, null, ex);
                                        throw new UnsupportedOperationException("Cannot invoke method", ex);
                                    } catch (InvocationTargetException ex) {
                                        Logger.getLogger(CommonGrid.class.getName()).log(Level.SEVERE, null, ex);
                                        throw new UnsupportedOperationException("Cannot invoke method", ex);
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
   
            }
          
        });
        
        return button;
        
        
    }
}
