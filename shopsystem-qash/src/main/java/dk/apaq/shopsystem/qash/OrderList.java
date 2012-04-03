package dk.apaq.shopsystem.qash;

import com.vaadin.data.Buffered;
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
import dk.apaq.filter.core.AndFilter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.sort.SortDirection;
import dk.apaq.filter.sort.Sorter;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterJob;
import dk.apaq.printing.core.PrinterManager;
import dk.apaq.shopsystem.annex.AnnexContext;
import dk.apaq.shopsystem.annex.AnnexService;
import dk.apaq.shopsystem.annex.AnnexType;
import dk.apaq.shopsystem.annex.AuditReportContent;
import dk.apaq.shopsystem.annex.Page;
import dk.apaq.shopsystem.annex.PageSize;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Outlet;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.qash.common.Spacer;
import dk.apaq.shopsystem.qash.data.util.CurrencyColumnGenerator;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.vaadin.addon.crudcontainer.FilterableContainer;
import java.awt.print.Printable;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class OrderList extends CustomComponent implements
        Property.ValueChangeNotifier, Container.Editor,
        ItemClickEvent.ItemClickNotifier, Property {
    
    private static final Logger LOG = LoggerFactory.getLogger(OrderList.class);
    private static final String[] COLUMNS_STORE =        {"delete", "number", "status", "dateChanged", "totalWithTax"};
    private static final String[] COLUMNCAPTIONS_STORE = {"", "Number", "Status", "Changed", "Total"};
    private static final String[] COLUMNS_NOSTORE = {"delete", "number", "outlet", "status", "dateChanged", "dateTimelyPayment", "total", "totalWithTax"};
    private static final String[] COLUMNCAPTIONS_NOSTORE = {"", "Number", "Store", "Status", "Changed", "Due", "Total", "Total(inc. tax)"};
    
    private static final String LABEL_INPROCESS = "In process";
    private static final String LABEL_ACCEPTED = "Accepted";
    private static final String LABEL_COMPLETED = "Completed";
    private static final String LABEL_UNPAID = "Unpaid";
    private final Label labelTitle = new Label("Orders");
    private final VerticalLayout topLayout = new VerticalLayout();
    private final VerticalLayout innerLayout = new VerticalLayout();
    private final HorizontalLayout filterLayout = new HorizontalLayout();
    private final Spacer filterSpacer = new Spacer();
    private final Button btnAdd = new Button("Add new order");
    private final Button btnPrintAuditLog = new Button("Print Report");
    private final ComboBox cmbStatus = new ComboBox("Status");
    private final ComboBox cmbClerk = new ComboBox("Clerk");
    private final Table table = new Table();
    private DateFormat dateFormat;
    private Container container;
    private boolean initialized;
    private final ThemeResource resourceDelete = new ThemeResource("img/clear.png");
    private final CurrencyColumnGenerator currencyColumnGenerator = new CurrencyColumnGenerator();
    private final Sorter sorter = new Sorter("number", SortDirection.Descending);
    private Outlet outlet;
    private String chosenStatus;
    private AnnexService annexService;
    private OrganisationService organisationService;
    
    private class DeleteColumnGenerator implements Table.ColumnGenerator {

        @Override
        public Component generateCell(Table source, final Object itemId, Object columnId) {
            Item item = source.getItem(itemId);
            OrderStatus status = (OrderStatus) item.getItemProperty("status").getValue();

            if (status == OrderStatus.New || status == OrderStatus.Processing) {
                Embedded embedded = new Embedded("Delete row", resourceDelete);
                embedded.setHeight(16, UNITS_PIXELS);
                embedded.addListener(new com.vaadin.event.MouseEvents.ClickListener() {

                    @Override
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

    private class OutletColumnGenerator implements Table.ColumnGenerator {

        @Override
        public Object generateCell(Table source, Object itemId, Object columnId) {
            Item item = source.getItem(itemId);
            Outlet outlet = (Outlet) item.getItemProperty("outlet").getValue();
            if (outlet == null) {
                return null;
            } else {
                return new Label(outlet.getName());
            }
        }
    }
    
    private class StatusColumnGenerator implements Table.ColumnGenerator {

        @Override
        public Object generateCell(Table source, Object itemId, Object columnId) {
            Item item = source.getItem(itemId);
            OrderStatus status = (OrderStatus) item.getItemProperty("status").getValue();
            Boolean paid = (Boolean) item.getItemProperty("paid").getValue();
            
            String text = status.name();
            if(!paid) {
                text+= " (Not paid)";
            }
            
            return new Label(text);
            
        }
    }

    private class FormattedDateGenerator implements Table.ColumnGenerator {

        @Override
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Property prop = source.getContainerProperty(itemId, columnId);
            Date date = (Date) prop.getValue();
            if(date==null) {
                return new Label("-");
            } else {
                return new Label(dateFormat.format(date));
            }
        }
    }

    @Override
    public void addListener(ItemClickListener listener) {
        table.addListener(listener);
    }

    @Override
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
        table.addGeneratedColumn("outlet", new OutletColumnGenerator());
        table.addGeneratedColumn("status", new StatusColumnGenerator());
        table.setColumnAlignment("totalWithTax", Table.ALIGN_RIGHT);

        currencyColumnGenerator.setCurrencyPropertyId("currency");

        filterLayout.addComponent(cmbStatus);
        //filterLayout.addComponent(cmbClerk);
        filterLayout.addComponent(filterSpacer);
        filterLayout.addComponent(btnPrintAuditLog);
        filterLayout.addComponent(btnAdd);
        filterLayout.setExpandRatio(filterSpacer, 1.0F);
        filterLayout.setSpacing(true);
        filterLayout.setComponentAlignment(btnPrintAuditLog, Alignment.BOTTOM_RIGHT);
        filterLayout.setComponentAlignment(btnAdd, Alignment.BOTTOM_RIGHT);
        filterLayout.setWidth(100, Component.UNITS_PERCENTAGE);

        cmbStatus.addItem("All");
        cmbStatus.addItem(LABEL_INPROCESS);
        cmbStatus.addItem(LABEL_ACCEPTED);
        cmbStatus.addItem(LABEL_COMPLETED);
        cmbStatus.addItem(LABEL_UNPAID);
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
                chosenStatus = (String) event.getProperty().getValue();
                updateFilter();
            }
        });
        
        btnPrintAuditLog.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(annexService==null) {
                   LOG.error("AnnexService missing.");
                }
            
                PrinterManager printerManager = PrintFacade.getManager(getApplication());
                Printer printer = printerManager.getDefaultPrinter();
                
                Date from = new Date();
                from.setHours(0);
                from.setMinutes(0);
                from.setSeconds(0);
                Date to = new Date();
                Page page = new Page(PageSize.A4, 2, 2, 2, 2);
                
                Filter orderFilter = new AndFilter(new CompareFilter("dateInvoiced", from, CompareFilter.CompareType.GreaterOrEqual),
                                            new CompareFilter("dateInvoiced", to, CompareFilter.CompareType.LessOrEqual));
                List<Order> orders = organisationService.getOrders().list(orderFilter, null);
                
                Filter paymentFilter = new AndFilter(new CompareFilter("dateCreated", from, CompareFilter.CompareType.GreaterOrEqual),
                                            new CompareFilter("dateCreated", to, CompareFilter.CompareType.LessOrEqual));
                List<Payment> payments = organisationService.getPayments().list(paymentFilter, null);
                
                AuditReportContent auditReportContent = new AuditReportContent(organisationService.readOrganisation(), from, to, orders, payments);
                AnnexContext<AuditReportContent, Void> annexContext = new AnnexContext<AuditReportContent, Void>(auditReportContent, null, page, getLocale());
                Printable printable = annexService.generateAuditReportPrintable(annexContext, AnnexType.Receipt);
                
                PrinterJob printerJob =  PrinterJob.getBuilder(printer, printable).build();
                printerManager.print(printerJob);
            }
        });

        btnAdd.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Object id = container.addItem();
                Item item = table.getItem(id);

                //Set the outlet for the order
                item.getItemProperty("outlet").setValue(outlet);
                
                //Set default currency
                item.getItemProperty("currency").setValue(organisationService.readOrganisation().getCurrency());
                
                if(item instanceof Buffered) {
                    ((Buffered)item).commit();
                }
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
        
        updateColumns();
        
        if (newDataSource instanceof FilterableContainer) {
            ((FilterableContainer) newDataSource).setSorter(sorter);
        }
    }

    public Container getContainerDataSource() {
        return container;
    }

    @Override
    public void attach() {
        super.attach();

        if (!initialized) {
            dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, getLocale());
            table.addGeneratedColumn("dateChanged", new FormattedDateGenerator());
            table.addGeneratedColumn("dateTimelyPayment", new FormattedDateGenerator());
            table.addGeneratedColumn("total", currencyColumnGenerator);
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

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
        updateColumns();
        updateFilter();
       
    }

    public void setOrganisationService(OrganisationService organisationService) {
        this.organisationService = organisationService;
    }
    
    public void setAnnexService(AnnexService annexService) {
        this.annexService = annexService;
    }
    
    private void updateFilter() {
        if (container instanceof FilterableContainer) {
            Filter filter = null;
            String value = chosenStatus;
            if (LABEL_INPROCESS.equals(value)) {
                filter = new CompareFilter("status", OrderStatus.Processing, CompareFilter.CompareType.Equals);
            }
            if (LABEL_ACCEPTED.equals(value)) {
                filter = new CompareFilter("status", OrderStatus.Accepted, CompareFilter.CompareType.Equals);
            }
            
            if (LABEL_COMPLETED.equals(value)) {
                filter = new CompareFilter("status", OrderStatus.Completed, CompareFilter.CompareType.Equals);
            }
            
            if (LABEL_UNPAID.equals(value)) {
                filter = new CompareFilter("paid", false, CompareFilter.CompareType.Equals);
            }
            
            
            if(outlet!=null) {
                Filter outletFilter = new CompareFilter("outlet", outlet, CompareFilter.CompareType.Equals);
                filter = filter == null ? outletFilter : new AndFilter(filter, outletFilter);
            }
            
            ((FilterableContainer) container).setFilter(filter);
        }

    }
    
    private void updateColumns() {
        if(container!=null) {
            if(outlet==null) {
                table.setVisibleColumns(COLUMNS_NOSTORE);
                table.setColumnHeaders(COLUMNCAPTIONS_NOSTORE);
            } else {
                table.setVisibleColumns(COLUMNS_STORE);
                table.setColumnHeaders(COLUMNCAPTIONS_STORE);
            }
        }
    }
}
