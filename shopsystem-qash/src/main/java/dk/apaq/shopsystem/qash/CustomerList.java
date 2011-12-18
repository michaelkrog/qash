package dk.apaq.shopsystem.qash;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import dk.apaq.crud.Crud;
import dk.apaq.filter.FilterGenerator;
import dk.apaq.filter.sort.Sorter;
import dk.apaq.shopsystem.entity.Customer;
import dk.apaq.shopsystem.qash.common.CustomerFilterGenerator;
import dk.apaq.shopsystem.qash.common.Spacer;
import dk.apaq.shopsystem.qash.listeners.RemoveSelectedOnClickListener;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import dk.apaq.vaadin.addon.crudcontainer.FilterableContainer;
import java.util.Collection;

/**
 * A List for products including filtering and editing.
 */
public class CustomerList extends CustomComponent implements Property, Property.Editor, Container, Container.Editor {

    private static final String[] COLUMNS = {"companyName", "contactName", "email"};
    private final Action editAction = new Action("Edit");
    private final Action deleteAction = new Action("Delete");
    private Label titleLabel = new Label("Customers");
    private VerticalLayout layout = new VerticalLayout();
    private VerticalLayout topVLayout = new VerticalLayout();
    private HorizontalLayout topHLayout = new HorizontalLayout();
    private Button addButton = new Button("Add new customer");
    private Button removeButton = new Button("Remove customer");
    private Button openButton = new Button("Edit customer");
    private TextField searchField = new TextField();
    private Spacer spacer = new Spacer();
    private Table table = new Table();
    private Crud<String, Customer> customerCrud;
    private CrudContainer customerContainer;
    private Sorter sorter = new Sorter("contactName");
    private final FilterGenerator filterGenerator = new CustomerFilterGenerator();

    private class SearchFieldHandler implements TextChangeListener {

        public void textChange(TextChangeEvent event) {
            if (customerContainer instanceof FilterableContainer) {
                ((FilterableContainer) customerContainer).setFilter(filterGenerator.generateFilter(event.getText()));
            }
        }
    }

    public CustomerList() {

        setStyleName("v-itemlist");
        setCompositionRoot(layout);
        layout.addComponent(topVLayout);
        layout.addComponent(table);

        titleLabel.setStyleName(Reindeer.LABEL_H1);

        searchField.setInputPrompt("Search, fx. 'John'");
        searchField.setImmediate(true);
        searchField.addListener(new SearchFieldHandler());


        topVLayout.addComponent(titleLabel);
        topVLayout.addComponent(topHLayout);
        topVLayout.setMargin(true);

        topHLayout.setSpacing(true);
        topHLayout.setMargin(true, false, false, false);
        topHLayout.addComponent(searchField);
        topHLayout.addComponent(spacer);
        topHLayout.addComponent(addButton);
        topHLayout.addComponent(openButton);
        topHLayout.addComponent(removeButton);
        //topHLayout.addComponent(adjustButton);
        topHLayout.setComponentAlignment(searchField, Alignment.BOTTOM_RIGHT);
        topHLayout.setExpandRatio(spacer, 1.0F);
        topHLayout.setWidth(100, Component.UNITS_PERCENTAGE);

        table.setSelectable(true);
        table.addActionHandler(new Handler() {

            public Action[] getActions(Object target, Object sender) {
                return new Action[]{editAction, deleteAction};
            }

            public void handleAction(Action action, Object sender, Object target) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });


        table.setSizeFull();

        addButton.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                Object id = table.addItem();
                Item item = table.getItem(id);
                item.getItemProperty("companyName").setValue("Unnamed customer");
                editItem(item);
            }
        });

        openButton.setStyleName(Reindeer.BUTTON_DEFAULT);
        openButton.setClickShortcut(KeyCode.ENTER, null);
        openButton.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                if (table.getValue() == null) {
                    return;
                }
                String productId = (String) table.getValue();
                Item item = table.getItem(productId);

                editItem(item);

            }
        });

        removeButton.addListener(new RemoveSelectedOnClickListener(table));

        layout.setMargin(false);
        layout.setExpandRatio(table, 1.0F);
        layout.setSizeFull();
    }

    public void setCustomerCrud(Crud<String, Customer> customerCrud) {
        this.customerCrud = customerCrud;
        refreshCustomerContainer();
    }

    @Override
    public Object getValue() {
        return table.getValue();
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
        table.setValue(newValue);
    }

    @Override
    public Class<?> getType() {
        return table.getType();
    }
    
    @Override
    public Item getItem(Object itemId) {
        return table.getItem(itemId);
    }

    @Override
    public Collection<?> getContainerPropertyIds() {
        return table.getContainerPropertyIds();
    }

    @Override
    public Collection<?> getItemIds() {
        return table.getItemIds();
    }

    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        return table.getContainerProperty(itemId, propertyId);
    }

    @Override
    public Class<?> getType(Object propertyId) {
        return table.getType(propertyId);
    }

    @Override
    public int size() {
        return table.size();
    }

    @Override
    public boolean containsId(Object itemId) {
        return table.containsId(itemId);
    }

    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object addItem() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeItem(Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void setPropertyDataSource(Property newDataSource) {
        table.setPropertyDataSource(newDataSource);
    }

    @Override
    public Property getPropertyDataSource() {
        return table.getPropertyDataSource();
    }

    @Override
    public void setContainerDataSource(Container newDataSource) {
        throw new UnsupportedOperationException("Not supported. Use setCustomerCrud.");
    }

    @Override
    public Container getContainerDataSource() {
        return table.getContainerDataSource();
    }
    
    @Override
    public void attach() {
        super.attach();
        refreshCustomerContainer();
    }

    private void editItem(final Item item) {
        final CustomerForm form = new CustomerForm();
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        Window dialog = new Window("Edit Customer", layout);
        dialog.addComponent(form);
        dialog.setWidth(445, Component.UNITS_PIXELS);
        dialog.setHeight(370, Component.UNITS_PIXELS);
        dialog.center();
        
        getApplication().getMainWindow().addWindow(dialog);

        
        form.setItemDataSource(item);
        
        
    }

    private void refreshCustomerContainer() {
        if (customerCrud != null) {
            this.customerContainer = new CrudContainer(customerCrud, Customer.class);
            this.customerContainer.setSorter(sorter);
            table.setContainerDataSource(this.customerContainer);
            table.setVisibleColumns(COLUMNS);
        }
    }

    
    
    
}
