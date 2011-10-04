package dk.apaq.shopsystem.ui.shoppinnet.common;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
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

public class CommonGrid extends CustomComponent implements Container.Filter { //Container.Viewer
    
    private Container data;
    //private IndexedContainer data;
    private String factoryClass = "";
    private String pageHeader = "";
    private Boolean edit = false;
    private Boolean search = false;
    private List header = new ArrayList();
    private List field = new ArrayList();
    private List fieldType = new ArrayList();
    private List button = new ArrayList();
    private List buttonMethod = new ArrayList();
    private List buttonTarget = new ArrayList();
    
    final private OrganisationService orgService;
    final private Table table = new Table();
    final private VerticalLayout content = new VerticalLayout();
    final private HorizontalLayout panel = new HorizontalLayout();
    final private HorizontalLayout buttonHolder = new HorizontalLayout();
    final private VerticalLayout dummy = new VerticalLayout();
    
    
    public void setFactoryClass (String factoryClass) {
        this.factoryClass = factoryClass;
    }
        
    public void setPageHeader (String pageHeader) {
        this.pageHeader = pageHeader;
    }
        
    public void setEdit (Boolean edit) {
        this.edit = edit;
    }
    
    public void setSearch (Boolean search) {
        this.search = search;
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
    

    /*@Override
    public Container getContainerDataSource() {
        return this.data;
    }*/

    
    //@Override
    public void setContainerDataSource(Container data) {
        this.data = data;
        //this.data = (IndexedContainer) data;
        table.setContainerDataSource(data);
    }
    
    
    @Override
    public void attach() {
        
        // Clear all
        this.content.removeAllComponents();
        this.panel.removeAllComponents();
        this.buttonHolder.removeAllComponents();
        
        // Create panel
        
        // Create search fields
        if(this.search == true) {
            TextField search = new TextField();
            search.setValue("Search...");
            this.panel.addComponent(search);
            this.panel.setComponentAlignment(search, Alignment.MIDDLE_LEFT);
        }
        
        // Create panel buttons
        for (int i = 0; i < this.button.size(); i++) {
            this.buttonHolder.addComponent(createButton(this.button.get(i).toString(),this.buttonMethod.get(i).toString(),this.buttonTarget.get(i).toString()));
        }
        
        // Create table
        this.table.setPageLength(20);
        
        /*if(this.data.size() == 0) {
            this.table.setContainerDataSource(null);
            this.table.addContainerProperty("", String.class,  null);
            this.table.addItem(new Object[] {"No data available!"}, new Integer(1));
        }
        else {*/
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
                //this.table.setMultiSelect(true);
                this.table.setColumnReorderingAllowed(true);
                this.table.setSortDisabled(false);
        //this.table.setRowHeaderMode(Table.ROW_HEADER_MODE_ICON_ONLY);

                this.table.setNullSelectionAllowed(false); 
                /*this.table.addListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        EditItem(table.getValue().toString());
                    }
                });*/
            }
     
            // Show only the needed columns, hide the rest
            this.table.setVisibleColumns(this.field.toArray());
       // }
        
        // Insert components into content
        this.buttonHolder.setSpacing(true);
        this.panel.addComponent(this.buttonHolder);
        this.panel.setComponentAlignment(this.buttonHolder, Alignment.MIDDLE_RIGHT);
        this.panel.setStyleName("v-table-panel");
        this.panel.setWidth("100%");
        this.panel.setSpacing(true);
        this.table.setWidth("100%");
        
        this.content.setSpacing(true);
        this.content.addComponent(new PageHeader(this.pageHeader));
        this.content.addComponent(this.panel);
        this.content.addComponent(this.table);
        this.content.addComponent(this.dummy);
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
                /*
                String tableId = "";
                if (table.getValue() == null) {
                    tableId = null;
                } 
                else {
                    tableId = table.getValue().toString();
                }
                                        
                Component newInstance = null;
                try {
                    try {
                        newInstance = (Component) Class.forName(factoryClass).newInstance();
                    } catch (InstantiationException ex) {
                        Logger.getLogger(CommonGrid.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(CommonGrid.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(CommonGrid.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    try {
                        
                        Method m = newInstance.getClass().getMethod(buttonMethod, orgService, tableId);
                        m.invoke(newInstance, orgService, tableId);
                       */ 
                               
                        
                        
                        
                        
                       
                            try {
                                try {
                                    System.out.println("Creating instance: " + factoryClass);
                                    Component newInstance = (Component) Class.forName(factoryClass).newInstance();
                                    dummy.addComponent(newInstance);

                                    System.out.println("Finding methods...");
                                    Method[] allMethods = newInstance.getClass().getMethods();

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

    @Override
    public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean appliesToProperty(Object propertyId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
