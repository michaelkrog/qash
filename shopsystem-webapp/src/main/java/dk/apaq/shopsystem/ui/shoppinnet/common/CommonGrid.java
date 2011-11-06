package dk.apaq.shopsystem.ui.shoppinnet.common;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
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

public class CommonGrid extends CustomComponent { //Container.Viewer
    
    //private Container data;
    //private IndexedContainer data;
    private String factoryClass = "";
    private String pageHeader = "";
    private List description = new ArrayList();
    private List descriptionCaption = new ArrayList();
    private Boolean editAble = false;
    private Boolean search = false;
    private List header = new ArrayList();
    private List field = new ArrayList();
    private List fieldType = new ArrayList();
    private List button = new ArrayList();
    private List buttonMethod = new ArrayList();
    private List buttonTarget = new ArrayList();
    private String selector = "";
    private String selectorName = "";
    private String selectorDefault = "";
    private Container selectorData;
    
    final private OrganisationService orgService;
    final private Table table = new Table();
    final private VerticalLayout content = new VerticalLayout();
    final private HorizontalLayout panel = new HorizontalLayout();
    final private HorizontalLayout buttonHolder = new HorizontalLayout();
    final private GridLayout filterHolder = new GridLayout(2,1);
    final private Panel descriptionPanel = new Panel();
    final private VerticalLayout descriptionPanelContent = new VerticalLayout();
    final private VerticalLayout dummy = new VerticalLayout();
    
    
    public void setFactoryClass (String factoryClass) {
        this.factoryClass = factoryClass;
    }
        
    public void setPageHeader (String pageHeader) {
        this.pageHeader = pageHeader;
    }
    
    public void addDescription (String descriptionCaption, String description) {
        this.description.add(description);
        this.descriptionCaption.add(descriptionCaption);
    }
        
    public void setEditAble (Boolean editAble) {
        this.editAble = editAble;
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
    
    public void setSelector(String selector, String selectorName, String selectorDefault, Container selectorData) {
	this.selector = selector;
        this.selectorName = selectorName;
        this.selectorDefault = selectorDefault;
        this.selectorData = selectorData;
    }
    

    /*@Override
    public Container getContainerDataSource() {
        return this.data;
    }*/

    
    //@Override
    public void setContainerDataSource(Container data) {
        //this.data = data;
        //this.data = (IndexedContainer) data;
        this.table.setContainerDataSource(data);
    }
    
    
    @Override
    public void attach() {
        
        // Clear all
        this.content.removeAllComponents();
        this.panel.removeAllComponents();
        this.buttonHolder.removeAllComponents();
        this.filterHolder.removeAllComponents();
        this.descriptionPanel.removeAllComponents();
        this.descriptionPanelContent.removeAllComponents();
        
        // Create description
        this.descriptionPanel.setCaption("Information");
        this.descriptionPanel.setIcon(new ThemeResource("icons/16/attention.png"));
        this.descriptionPanelContent.setSpacing(true);
        
        for (int i = 0; i < this.description.size(); i++) {
            Label label = new Label(this.description.get(i).toString());
            if (!"".equals(this.descriptionCaption.get(i).toString())) { 
                label.setCaption(this.descriptionCaption.get(i).toString());
            }
            label.addStyleName("information");
            this.descriptionPanelContent.addComponent(label);      
        }
        this.descriptionPanel.addComponent(descriptionPanelContent); 
        
        
        // *** Create panel ***
        
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
        
        // Create panel selector
        if (!"".equals(this.selector)) {
            final Select select = new Select();
            select.setContainerDataSource(this.selectorData);
            select.setValue(this.selectorDefault);
            select.setItemCaptionPropertyId(this.selector);
            select.setNullSelectionAllowed(false);
            select.setImmediate(true);
            
            select.addListener(new ValueChangeListener() {
                public void valueChange(ValueChangeEvent event) {
                    try {
                        try {
                            Component newInstance = (Component) Class.forName(factoryClass).newInstance();
                            Method[] allMethods = newInstance.getClass().getMethods();
                            for (Method m : allMethods) {
                                System.out.println("Found: " + m.getName());
                                if (m.getName().equalsIgnoreCase("getSelectorContainer")) {
                                    try {
                                        try {
                                            selectorDefault = select.getValue().toString();
                                            Object o = m.invoke(newInstance, orgService, select.getValue().toString());
                                            setContainerDataSource((Container) o);
                                            
                                        } catch (InvocationTargetException ex) {
                                            Logger.getLogger(CommonGrid.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    } catch (IllegalArgumentException ex) {
                                        Logger.getLogger(CommonGrid.class.getName()).log(Level.SEVERE, null, ex);
                                    } 
                                }
                            }

                        } catch (InstantiationException ex) {
                            Logger.getLogger(CommonGrid.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(CommonGrid.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(CommonGrid.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    // Recreate grid
                    attach();         
                }
            });
            
            this.filterHolder.addComponent(select);
        }
        // *** Panel end ***
        
        
        // Create table
        this.table.setPageLength(20);

        for (int i = 0; i < this.header.size(); i++) {

            this.table.setColumnHeader(this.field.get(i), this.header.get(i).toString());

            String colfieldType = this.fieldType.get(i).toString();
            if ("SomeCustomFieldStuffHereIfNeeded".equals(colfieldType)) {
                 //Some custom field stuff can be added here
            }
        }

        // Handle table selection change, if enabled
        if (this.editAble == true) {
            this.table.setSelectable(true);
            this.table.setColumnReorderingAllowed(true);
            this.table.setSortDisabled(false);
            this.table.setNullSelectionAllowed(false); 
        }

        // Show only the needed columns, hide the rest
        this.table.setVisibleColumns(this.field.toArray());
       
        
        // Insert components into content'        
        this.filterHolder.setSpacing(true);
        this.panel.addComponent(this.filterHolder);
        this.panel.setComponentAlignment(this.filterHolder, Alignment.TOP_LEFT);
        
        this.buttonHolder.setSpacing(true);
        this.panel.addComponent(this.buttonHolder);
        this.panel.setComponentAlignment(this.buttonHolder, Alignment.TOP_RIGHT);
        
        this.panel.setStyleName("v-table-panel");
        this.panel.setWidth("100%");
        this.panel.setSpacing(true);
        this.table.setWidth("100%");
        
        this.content.setSpacing(true);
        this.content.addComponent(new PageHeader(this.pageHeader));
        
        if (this.description.size() != 0) {
            this.content.addComponent(this.descriptionPanel);
        }
        this.content.addComponent(this.panel);
        this.content.addComponent(this.table);
        this.content.addComponent(this.dummy);
    }
    
    
    public CommonGrid(OrganisationService orgService) {
        
        this.orgService = orgService;
        setCompositionRoot(this.content);
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

}
