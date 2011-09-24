package dk.apaq.shopsystem.ui.qash;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.MouseEvents;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.core.OrFilter;
import dk.apaq.filter.sort.SortDirection;
import dk.apaq.filter.sort.Sorter;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.ui.qash.data.util.CurrencyColumnGenerator;
import dk.apaq.shopsystem.ui.common.Spacer;
import dk.apaq.vaadin.addon.crudcontainer.FilterableContainer;
import java.text.DateFormat;
import java.util.Date;

/**
 *
 */
public class OrderList extends CustomComponent implements
        Property.ValueChangeNotifier, Container.Editor, 
        ItemClickEvent.ItemClickNotifier, Property {

    private static final String LABEL_INPROCESS = "In process";
    private static final String LABEL_COMPLETED= "Completed";


    private final Label labelTitle = new Label("Order Overview");
    private final VerticalLayout topLayout = new VerticalLayout();
    private final VerticalLayout innerLayout = new VerticalLayout();
    private final HorizontalLayout filterLayout = new HorizontalLayout();
    private final Spacer filterSpacer = new Spacer();
    private final Button btnAdd = new Button("Add new order");
    private final ComboBox cmbStatus = new ComboBox("Status");
    private final ComboBox cmbClerk = new ComboBox("Clerk");
    private final Table table = new Table();
    private DateFormat dateFormat;
    private Container container;
    private boolean initialized;
    private final ThemeResource resourceDelete = new ThemeResource("img/clear.png");
    private final CurrencyColumnGenerator currencyColumnGenerator = new CurrencyColumnGenerator();
    private final Sorter sorter = new Sorter("number", SortDirection.Descending);


    private class DeleteColumnGenerator implements Table.ColumnGenerator {

        public Component generateCell(Table source, final Object itemId, Object columnId) {
            Item item = source.getItem(itemId);
            OrderStatus status = (OrderStatus)item.getItemProperty("status").getValue();

            if(status == OrderStatus.New || status == OrderStatus.Processing) {
                Embedded embedded = new Embedded("Delete row", resourceDelete);
                embedded.setHeight(16, UNITS_PIXELS);
                embedded.addListener(new com.vaadin.event.MouseEvents.ClickListener() {

                    public void click(MouseEvents.ClickEvent event) {
                        table.removeItem(itemId);
                    }
                });
                return embedded;
            } else {
                return null;
            }
        }

    }

    private class FormattedDateGenerator implements Table.ColumnGenerator {

        public Component generateCell(Table source, Object itemId, Object columnId) {
            Property prop = source.getContainerProperty(itemId, columnId);
            Date date = (Date) prop.getValue();
            return new Label(dateFormat.format(date));
        }

    }

    public void addListener(ItemClickListener listener) {
        table.addListener(listener);
    }

    public void removeListener(ItemClickListener listener) {
        table.removeListener(listener);
    }

    public OrderList() {
        setStyleName("v-orderlist");

        labelTitle.setStyleName(Reindeer.LABEL_H1);

        topLayout.addComponent(labelTitle);
        topLayout.addComponent(filterLayout);
        topLayout.setMargin(true);
        topLayout.setSpacing(true);

        innerLayout.addComponent(topLayout);
        innerLayout.addComponent(table);
        innerLayout.setSpacing(true);
        innerLayout.setSizeFull();
        innerLayout.setExpandRatio(table, 1.0F);

        table.setWidth(100, Component.UNITS_PERCENTAGE);
        table.setHeight(100, Component.UNITS_PERCENTAGE);
        table.setStyleName(Reindeer.TABLE_STRONG);
        table.setImmediate(true);
        table.addGeneratedColumn("delete", new DeleteColumnGenerator());
        table.setColumnAlignment("totalWithTax", Table.ALIGN_RIGHT);

        currencyColumnGenerator.setCurrencyPropertyId("currency");
        
        filterLayout.addComponent(cmbStatus);
        //filterLayout.addComponent(cmbClerk);
        filterLayout.addComponent(filterSpacer);
        filterLayout.addComponent(btnAdd);
        filterLayout.setExpandRatio(filterSpacer, 1.0F);
        filterLayout.setSpacing(true);
        filterLayout.setComponentAlignment(btnAdd, Alignment.BOTTOM_RIGHT);
        filterLayout.setWidth(100, Component.UNITS_PERCENTAGE);

        cmbStatus.addItem("All");
        cmbStatus.addItem("In process");
        cmbStatus.addItem("Completed");
        cmbStatus.setNullSelectionAllowed(false);
        cmbStatus.setNewItemsAllowed(false);
        cmbStatus.setValue("All");
        cmbStatus.setImmediate(true);

        cmbClerk.addItem("All");
        cmbClerk.addItem("Hannah Krog");
        cmbClerk.setNullSelectionAllowed(false);
        cmbClerk.setNewItemsAllowed(false);

        setCompositionRoot(innerLayout);

        cmbStatus.addListener(new ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                if(container instanceof FilterableContainer) {
                    Filter filter = null;
                    String value = (String) event.getProperty().getValue();
                    if(LABEL_INPROCESS.equals(value)) {
                        filter = new CompareFilter("status", OrderStatus.Processing, CompareFilter.CompareType.Equals);
                    }
                    if(LABEL_COMPLETED.equals(value)) {
                        filter = new OrFilter(
                                new CompareFilter("status", OrderStatus.Accepted, CompareFilter.CompareType.Equals),
                                new CompareFilter("status", OrderStatus.Completed, CompareFilter.CompareType.Equals));
                    }
                    ((FilterableContainer)container).setFilter(filter);
                }

            }
        });

        btnAdd.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                Object id = container.addItem();
                table.select(id);
            }
        });


    }

    public Button getButtonAdd() {
        return btnAdd;
    }

    @Override
    public void addListener(ValueChangeListener listener) {
        table.addListener(listener);
    }

    @Override
    public void removeListener(ValueChangeListener listener) {
        table.removeListener(listener);
    }

    @Override
    public void setContainerDataSource(Container newDataSource) {
        this.container = newDataSource;
        table.setContainerDataSource(newDataSource);
        table.setVisibleColumns(new Object[]{"delete", "number", "status", "dateChanged", "totalWithTax"});
        table.setColumnHeaders(new String[]{"", "Number", "Status", "Changed", "Total"});

        if(newDataSource instanceof FilterableContainer) {
            ((FilterableContainer)newDataSource).setSorter(sorter);
        }
    }

    public Container getContainerDataSource() {
        return container;
    }

    @Override
    public void attach() {
        super.attach();

        if(!initialized) {
            dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, getLocale());
            table.addGeneratedColumn("dateChanged", new FormattedDateGenerator());
            table.addGeneratedColumn("totalWithTax", currencyColumnGenerator);
            initialized = true;
        }

    }

    public Object getValue() {
        return table.getValue();
    }

    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
        table.setValue(newValue);
    }

    public Class<?> getType() {
        return table.getType();
    }

}
