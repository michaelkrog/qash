package dk.apaq.shopsystem.ui.qash;

import dk.apaq.shopsystem.ui.common.SearchField;
import dk.apaq.shopsystem.ui.common.CommonDialog;
import dk.apaq.shopsystem.ui.qash.print.PrintDocGenerator;
import com.google.gwt.event.dom.client.KeyCodes;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Item.PropertySetChangeEvent;
import com.vaadin.event.MouseEvents;
import com.vaadin.data.Buffered;
import com.vaadin.ui.Field;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.event.ShortcutListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractField.FocusShortcut;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout.MarginInfo;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.crud.Crud;
import dk.apaq.crud.Crud.Filterable;
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.sort.Sorter;


import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderLineTax;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.PaymentType;
import dk.apaq.shopsystem.entity.Product;
import dk.apaq.shopsystem.ui.ShopSystemTheme;
import dk.apaq.shopsystem.ui.qash.data.ContainerFormattingWrapper;
import dk.apaq.shopsystem.ui.qash.data.OrderLineContainer;
import dk.apaq.shopsystem.ui.qash.data.util.CurrencyAmountFormatter;
import dk.apaq.shopsystem.ui.qash.data.util.CurrencyAmountValidator;
import dk.apaq.shopsystem.ui.qash.data.util.NumberFormatter;
import dk.apaq.shopsystem.ui.qash.data.util.NumberValidator;
import dk.apaq.shopsystem.ui.qash.data.util.PercentageFormatter;
import dk.apaq.shopsystem.ui.qash.data.util.PercentageValidator;
import dk.apaq.shopsystem.ui.common.ProductFilterGenerator;
import dk.apaq.shopsystem.ui.common.SystemSettings;
import dk.apaq.vaadin.addon.crudcontainer.FilterableContainer;
import dk.apaq.vaadin.addon.crudcontainer.HasBean;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.print.PrintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 */
public class OrderEditor extends CustomComponent implements
        com.vaadin.data.Item.Editor, Property.ValueChangeListener {

    private static final Logger LOG = LoggerFactory.getLogger(OrderEditor.class);
    private com.vaadin.data.Item dataSource = null;
    private final OrderHeader header = new OrderHeader();
    private final OrderFooter footer = new OrderFooter();
    private final VerticalLayout layout = new VerticalLayout();
    private final Table table = new Table();
    private PrintDocGenerator printDocGenerator;
    private OrderLineContainer orderlineContainer = new OrderLineContainer();
    private ContainerFormattingWrapper containerFormattingWrapper = new ContainerFormattingWrapper.WithIndexed(orderlineContainer);
    private Container productContainer;
    private Container paymentContainer;
    private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
    private NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
    private Container taxContainer;
    private Boolean editable = null;
    private boolean hasChanges = false;
    private PropertyWrapper propertyWrapper;
    private CurrencyAmountValidator amountValidator;
    private PercentageValidator percentageValidator;
    private NumberValidator numberValidator;
    private EditorFieldFactory fieldFactory = new EditorFieldFactory();
    private String[] shownTableProperties = {"delete", "title", "price", "priceWithTax",
        "discountPercentage", "quantity", "totalWithTax"};
    private String[] shownTableHeaders = {"", "title", "Price w/o tax", "Price w/ tax",
        "Discount", "Qty.", "total"};
    private String[] uneditableProperties = {"title", "totalWithTax"};
    private ThemeResource resourceDelete = new ThemeResource("img/clear.png");
    private Crud.Filterable<String, Product> productCrud;
    private boolean initialized = false;
    private boolean hasEditableColumns = false;
    private OrderLineChangeListener orderLineChangeListener = new OrderLineChangeListener();
    private Sorter productSorter = new Sorter("name");
    private Sorter paymentSorter = new Sorter("dateChanged");
    private SystemSettings settings;
    private final PaymentChangeListener paymentChangeListener = new PaymentChangeListener();
    private final OrderChangeListener orderChangeListener = new OrderChangeListener();

    private class OrderChangeListener implements Item.PropertySetChangeListener {

        public void itemPropertySetChange(PropertySetChangeEvent event) {
            update();
        }

    }

    private class PaymentChangeListener implements Container.ItemSetChangeListener {

        public void containerItemSetChange(ItemSetChangeEvent event) {
            update();
        }
    }

    private class OrderLineChangeListener implements Property.ValueChangeListener {

        public void valueChange(ValueChangeEvent event) {
            hasChanges = true;
        }
    }

    private class DeleteColumnGenerator implements Table.ColumnGenerator {

        public Component generateCell(final Table source, final Object itemId, final Object columnId) {
            if (!editable) {
                return new Label();
            }

            Embedded embedded = new Embedded("Delete row", resourceDelete);
            embedded.setHeight(16, UNITS_PIXELS);
            embedded.addListener(new com.vaadin.event.MouseEvents.ClickListener() {

                public void click(MouseEvents.ClickEvent event) {
                    table.getContainerDataSource().removeItem(itemId);
                    hasChanges = true;
                }
            });
            return embedded;
        }
    }

    private class PropertyWrapper implements ContainerFormattingWrapper.PropertyWrapper {

        private final Locale locale;

        public PropertyWrapper(Locale locale) {
            this.locale = locale;
        }

        public Property createWrappedProperty(Object propertyId, Property wrapped) {
            if ("price".equals(propertyId)
                    || "priceWithTax".equals(propertyId)
                    || "totalWithTax".equals(propertyId)) {
                return new CurrencyAmountFormatter(wrapped, locale);
            }

            if ("discountPercentage".equals(propertyId)) {
                return new PercentageFormatter(wrapped, locale);
            }

            if ("quantity".equals(propertyId)) {
                return new NumberFormatter(wrapped, locale);
            }
            return null;
        }
    }

    private class EditorFieldFactory implements TableFieldFactory {

        private TableFieldFactory factory = DefaultFieldFactory.get();
        private TextField lastCreatedPriceWithTaxField = null;
        private TextField lastCreatedQuantityField = null;

        public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
            for (String current : uneditableProperties) {
                if (current.equals(propertyId)) {
                    return null;
                }
            }

            Field field = factory.createField(container, itemId, propertyId, uiContext);


            if (field instanceof TextField) {

                field.addListener(OrderEditor.this);
                ((TextField) field).setImmediate(true);
                field.setStyleName(ShopSystemTheme.TEXTALIGN_RIGHT);

                if ("price".equals(propertyId)) {
                    field.addValidator(amountValidator);
                }

                if ("priceWithTax".equals(propertyId)) {
                    field.addValidator(amountValidator);
                    lastCreatedPriceWithTaxField = (TextField) field;
                }

                if ("quantity".equals(propertyId)) {
                    field.addValidator(numberValidator);
                    lastCreatedQuantityField = (TextField) field;
                    ((TextField) field).addShortcutListener(new ShortcutListener("Listen for enter", KeyCodes.KEY_ENTER, null) {

                        @Override
                        public void handleAction(Object sender, Object target) {
                            header.getSearchField().focus();
                        }
                    });
                }

                if ("discountPercentage".equals(propertyId)) {
                    field.addValidator(percentageValidator);
                }

                field.setWidth(100, UNITS_PERCENTAGE);
                field.setInvalidAllowed(false);
            }
            return field;
        }

        public TextField getLastCreatedPriceWithTaxField() {
            return lastCreatedPriceWithTaxField;
        }

        public TextField getLastCreatedQuantityFieild() {
            return lastCreatedQuantityField;
        }
    }

    
    public class OrderHeader extends CustomComponent {

        private final VerticalLayout outerLayout = new VerticalLayout();
        private final HorizontalLayout topLayout = new HorizontalLayout();
        private final VerticalLayout leftLayout = new VerticalLayout();
        private final VerticalLayout rightLayout = new VerticalLayout();
        private final HorizontalLayout bottomLayout = new HorizontalLayout();
        private final Label lbl_orderno = new Label("#12");
        private final Label lbl_invoiceno = new Label("Invoiceno: #12");
        private final Label lbl_dateCreated = new Label("Created: 1/2 2011");
        private final Label lbl_dateDelivered = new Label("Delivered: 1/2 2011");
        private final Label lbl_customer = new Label("Anonymous Customer");
        private final Label lbl_clerk = new Label("Clerk: Hannah Krog");
        private final Label lbl_due = new Label("Due: 0,00");
        private final Label lbl_status = new Label("New");
        private final TextField txt_barcode = new TextField();
        private final Button btn_addLine = new Button("Add non-stock item (F8)");
        private final Button btn_print = new Button("Print (F10)");
        private final Button btn_advance = new Button("Invoice");
        private final SearchField field_search = new SearchField();
        private final ProductFilterGenerator filterGenerator = new ProductFilterGenerator();
        private HorizontalLayout orderlineLayout = new HorizontalLayout();
        private Property property;
        private boolean enabled = true;

        public OrderHeader() {

            field_search.setFilterGenerator(filterGenerator);

            lbl_orderno.setStyleName(Reindeer.LABEL_H1);
            lbl_customer.setStyleName(Reindeer.LABEL_H1);
            lbl_status.setStyleName(Reindeer.LABEL_H2);

            field_search.setInputPrompt("Search Item (F4)");
            field_search.setImmediate(true);
            field_search.setItemCaptionMode(Select.ITEM_CAPTION_MODE_ITEM);
            field_search.setItemCaptionPropertyId("name");
            //field_search.setNullSelectionAllowed(false);

            txt_barcode.setInputPrompt("Barcode (F6)");
            txt_barcode.setImmediate(true);
            txt_barcode.setStyleName(ShopSystemTheme.TEXTFIELD_BARCODE);

            lbl_customer.setSizeUndefined();
            lbl_clerk.setSizeUndefined();
            lbl_status.setSizeUndefined();
            lbl_due.setSizeUndefined();

            outerLayout.addComponent(topLayout);
            outerLayout.addComponent(bottomLayout);
            outerLayout.addComponent(orderlineLayout);
            outerLayout.setComponentAlignment(orderlineLayout, Alignment.BOTTOM_RIGHT);
            outerLayout.setSizeFull();

            bottomLayout.setMargin(true, true, false, true);
            bottomLayout.addComponent(leftLayout);
            bottomLayout.addComponent(rightLayout);
            bottomLayout.setSizeFull();


            topLayout.setMargin(true, true, true, true);
            topLayout.addComponent(lbl_orderno);
            //topLayout.addComponent(lbl_customer);
            //topLayout.setComponentAlignment(lbl_customer, Alignment.TOP_RIGHT);
            topLayout.setWidth(100, Component.UNITS_PERCENTAGE);

            leftLayout.setMargin(false);
            leftLayout.addComponent(lbl_invoiceno);
            leftLayout.addComponent(lbl_dateCreated);
            leftLayout.addComponent(lbl_dateDelivered);
            leftLayout.setSizeFull();

            rightLayout.setMargin(false);
            rightLayout.addComponent(lbl_status);
            rightLayout.setComponentAlignment(lbl_status, Alignment.TOP_RIGHT);
            rightLayout.addComponent(lbl_due);
            rightLayout.setComponentAlignment(lbl_due, Alignment.TOP_RIGHT);
            //rightLayout.addComponent(lbl_clerk);
            //rightLayout.setComponentAlignment(lbl_clerk, Alignment.TOP_RIGHT);
            rightLayout.setSizeFull();

            orderlineLayout.setMargin(false, true, false, true);
            orderlineLayout.setSpacing(true);
            orderlineLayout.addComponent(field_search);
            orderlineLayout.addComponent(txt_barcode);
            orderlineLayout.addComponent(btn_addLine);
            orderlineLayout.addComponent(btn_print);
            orderlineLayout.addComponent(btn_advance);

            btn_advance.setStyleName(Reindeer.BUTTON_DEFAULT);

            setCompositionRoot(outerLayout);

            setStyleName("v-ordereditor-header");
            topLayout.setStyleName("v-ordereditor-orderheader-top");
        }

        public Label getLabelDateCreated() {
            return lbl_dateCreated;
        }

        public Label getLabelDateDelivered() {
            return lbl_dateDelivered;
        }

        public Label getLabelInvoiceno() {
            return lbl_invoiceno;
        }

        public Label getLabelOrderno() {
            return lbl_orderno;
        }

        public Label getLabelStatus() {
            return lbl_status;
        }

        public Label getLabelDue() {
            return lbl_due;
        }

        public Button getButtonAdvanceOrder() {
            return btn_advance;
        }

        public Button getButtonAddLine() {
            return btn_addLine;
        }

        public Button getButtonPrint() {
            return btn_print;
        }

        public TextField getBarcodeField() {
            return txt_barcode;
        }

        public SearchField getSearchField() {
            return field_search;
        }
    }

    public class OrderFooter extends CustomComponent implements com.vaadin.data.Item.Viewer {

        private final HorizontalLayout outerLayout = new HorizontalLayout();
        private final HorizontalLayout leftLayout = new HorizontalLayout();
        private final Label lbl_total = new Label();
        private final Label lbl_shortcutdescription = new Label("Shortcuts using Ctrl + Shift. S=Search, B=Barcode, A=Add item, P=Print and Enter=Complete order");
        private final Button btn_vat = new Button();
        private final Button btn_freight = new Button();
        private final Button btn_discount = new Button();
        private final Select printerList = new Select();
        //private RemotePrintServiceManager printServiceManager;
        private final BeanContainer<String, PrintService> printServiceContainer = new BeanContainer<String, PrintService>(PrintService.class);
        private com.vaadin.data.Item dataSource;

        public OrderFooter() {
            outerLayout.setStyleName(Reindeer.LAYOUT_BLACK);
            outerLayout.addComponent(leftLayout);
            outerLayout.addComponent(btn_vat);
            outerLayout.addComponent(lbl_total);

            lbl_total.setSizeUndefined();
            lbl_total.setStyleName(Reindeer.LABEL_H2);

            leftLayout.setSpacing(true);
            leftLayout.addComponent(printerList);
            leftLayout.setComponentAlignment(printerList, Alignment.MIDDLE_LEFT);
            //leftLayout.addComponent(lbl_shortcutdescription);
            //leftLayout.setComponentAlignment(lbl_shortcutdescription, Alignment.MIDDLE_LEFT);
            leftLayout.setSizeFull();

            outerLayout.setComponentAlignment(lbl_total, Alignment.MIDDLE_RIGHT);
            outerLayout.setComponentAlignment(btn_vat, Alignment.MIDDLE_RIGHT);
            outerLayout.setExpandRatio(leftLayout, 1F);
            outerLayout.setSpacing(true);

            outerLayout.setSizeFull();
            outerLayout.setMargin(new MarginInfo(false, true, false, true));

            printerList.setNewItemsAllowed(false);
            printerList.setNullSelectionAllowed(false);
            printerList.setContainerDataSource(printServiceContainer);
            printerList.setItemCaptionPropertyId("name");

            printServiceContainer.setBeanIdProperty("name");

            setCompositionRoot(outerLayout);
        }

        public void setItemDataSource(com.vaadin.data.Item newDataSource) {
            this.dataSource = newDataSource;

            update();

        }

        @Override
        public void attach() {
            super.attach();

            /*if (printServiceManager == null) {
                //printServiceManager = RemotePrintServiceManager.getInstance(getApplication());
                //printServiceManager = RemotePrintServiceHelper.getPrintServiceManager(getApplication());
                /*printServiceManager.addListener(new PrintServiceListChangedListener() {

                    public void onPrintServiceListChanged(PrintServiceListChangedEvent event) {
                        updatePrinterList();
                    }
                });
                updatePrinterList();
            }*/
            update();
        }

        /*private void updatePrinterList() {
            List<? extends PrintService> list = printServiceManager.getPrintServices(null, null);
            printServiceContainer.removeAllItems();
            for (PrintService service : list) {
                printServiceContainer.addBean(service);
            }

            PrintService defPs = printServiceManager.getDefaultPrintService();
            if (defPs != null) {
                printerList.setValue(defPs.getName());
            }
        }*/

        @Override
        public com.vaadin.data.Item getItemDataSource() {
            return this.dataSource;
        }

        public PrintService getSelectedPrintService() {
            Object id = printerList.getValue();
            if (id == null) {
                return null;
            }
            BeanItem<PrintService> item = printServiceContainer.getItem(id);
            return item.getBean();
        }

        public void update() {
            double total = 0;
            double tax = 0;

            if (dataSource != null) {
                Property propTotal = dataSource.getItemProperty("total");
                Property propTotalTax = dataSource.getItemProperty("totalTax");
                tax = (Double) propTotalTax.getValue();
                total = ((Double) propTotal.getValue()) + tax;
            }

            lbl_total.setCaption(numberFormat.format(total));
            btn_vat.setCaption("VAT: " + numberFormat.format(tax));
        }
    }

    public OrderEditor() {
        setStyleName("v-ordereditor");

        layout.setStyleName(Reindeer.LAYOUT_WHITE);
        layout.setMargin(false);

        layout.addComponent(header);
        layout.addComponent(table);
        layout.addComponent(footer);

        table.addGeneratedColumn("delete", new DeleteColumnGenerator());
        table.setSizeFull();
        table.setTableFieldFactory(fieldFactory);

        this.orderlineContainer.addListener(new Container.ItemSetChangeListener() {

            public void containerItemSetChange(ItemSetChangeEvent event) {
                for (Object id : orderlineContainer.getItemIds()) {
                    Item item = orderlineContainer.getItem(id);
                    for (Object propId : item.getItemPropertyIds()) {
                        Property prop = item.getItemProperty(propId);
                        if (!prop.isReadOnly() && (prop instanceof Property.ValueChangeNotifier)) {
                            ((Property.ValueChangeNotifier) prop).addListener(orderLineChangeListener);
                        }
                    }
                }
            }
        });

        header.getSearchField().addShortcutListener(new FocusShortcut(header.getSearchField(), KeyCode.F4, ModifierKey.META));
        header.getBarcodeField().addShortcutListener(new FocusShortcut(header.getBarcodeField(), KeyCode.F6, ModifierKey.META));
        header.getButtonAddLine().setClickShortcut(KeyCode.F8, ModifierKey.META);
        header.getButtonPrint().setClickShortcut(KeyCode.F10, ModifierKey.META);
        header.getButtonAdvanceOrder().setClickShortcut(KeyCode.F12, ModifierKey.META);

        header.setWidth("100%");
        footer.setWidth("100%");
        footer.setHeight("27px");
        layout.setExpandRatio(table, 1.0F);

        layout.setSizeFull();

        setCompositionRoot(layout);


        header.getButtonPrint().addListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                doPrint();

            }
        });

        header.getButtonAdvanceOrder().addListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                Property propStatus = dataSource.getItemProperty("status");
                OrderStatus status = (OrderStatus) propStatus.getValue();

                if (!status.isConfirmedState()) {
                    doComplete(true);
                } else {
                    doPay();
                }


            }
        });

        header.getButtonAddLine().addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                final NonStockProductCreatePanel createPanel = new NonStockProductCreatePanel(OrderEditor.this.taxContainer);
                final CommonDialog dialog = new CommonDialog("Add non-stock item", createPanel);
                dialog.setWidth(350, UNITS_PIXELS);
                dialog.setHeight(170, UNITS_PIXELS);
                dialog.setModal(true);
                getApplication().getMainWindow().addWindow(dialog);

                dialog.addListener(new Window.CloseListener() {

                    public void windowClose(CloseEvent e) {
                        if (dialog.getResult() == CommonDialog.ButtonType.Ok) {
                            Object itemid = orderlineContainer.addItem();
                            Item item = orderlineContainer.getItem(itemid);
                            item.getItemProperty("title").setValue(createPanel.getTitle());
                            item.getItemProperty("quantity").setValue(1);

                            if (createPanel.getTax() != null) {
                                OrderLineTax tax = new OrderLineTax(createPanel.getTax());
                                item.getItemProperty("tax").setValue(tax);
                            }
                            hasChanges = true;
                        }
                    }
                });


            }
        });

        header.getSearchField().addListener(new Property.ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                if (header.getSearchField().getValue() == null) {
                    return;
                }

                String itemId = (String) header.getSearchField().getValue();
                com.vaadin.data.Item item = OrderEditor.this.productContainer.getItem(itemId);

                if (item instanceof HasBean) {
                    HasBean<Product> bi = (HasBean) item;
                    int index = (Integer) orderlineContainer.addItem(bi.getBean());
                    header.getSearchField().setValue(null);
                    hasChanges = true;

                    TextField f = bi.getBean().getPrice() > 0 ? fieldFactory.lastCreatedQuantityField : fieldFactory.getLastCreatedPriceWithTaxField();
                    if (f != null) {
                        f.focus();
                        f.selectAll();
                    }
                }


            }
        });

        header.getBarcodeField().addListener(new Property.ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                String barcode = (String) event.getProperty().getValue();

                if ("".equals(barcode)) {
                    return;
                }

                if (productCrud != null) {
                    Filter filter = new CompareFilter("barcode", barcode, CompareFilter.CompareType.Equals);
                    List<String> idlist = productCrud.listIds(filter, null);
                    if (idlist.isEmpty()) {
                        getApplication().getMainWindow().showNotification("Product not found", "No product could be found with given barcode.", Window.Notification.TYPE_WARNING_MESSAGE);
                    } else {
                        Product product = productCrud.read(idlist.get(0));
                        int index = (Integer) orderlineContainer.addItem(product);
                        hasChanges = true;
                    }
                    header.getBarcodeField().setValue("");
                }

            }
        });

    }

    public void setPaymentDatasource(Container container) {
        if (this.paymentContainer != null && this.paymentContainer instanceof Container.ItemSetChangeNotifier) {
            ((Container.ItemSetChangeNotifier) this.paymentContainer).removeListener(paymentChangeListener);
        }

        this.paymentContainer = container;

        if (this.paymentContainer != null && this.paymentContainer instanceof Container.ItemSetChangeNotifier) {
            ((Container.ItemSetChangeNotifier) this.paymentContainer).addListener(paymentChangeListener);
        }


        if (this.paymentContainer instanceof FilterableContainer) {
            ((FilterableContainer) this.paymentContainer).setSorter(paymentSorter);
        }



        //this.header.getSearchField().setContainerDataSource(container);
    }

    public void setProductDatasource(Container container) {
        this.productContainer = container;
        if (this.productContainer instanceof FilterableContainer) {
            ((FilterableContainer) this.productContainer).setSorter(productSorter);
        }
        this.header.getSearchField().setContainerDataSource(container);
    }

    public void setTaxDataSource(Container container) {
        this.taxContainer = container;
    }

    public void setItemDataSource(com.vaadin.data.Item newDataSource) {

        if(this.dataSource!=null && (this.dataSource instanceof Item.PropertySetChangeNotifier)) {
            ((Item.PropertySetChangeNotifier)this.dataSource).removeListener(orderChangeListener);
        }

        this.dataSource = newDataSource;

        if (dataSource != null) {
            Order order = getOrderFromDatasource();
            Currency currency = Currency.getInstance(order.getCurrency());
            numberFormat.setCurrency(currency);
        }

        if(this.dataSource!=null && (this.dataSource instanceof Item.PropertySetChangeNotifier)) {
            ((Item.PropertySetChangeNotifier)this.dataSource).addListener(orderChangeListener);
        }

        Order order = ((HasBean<Order>) this.dataSource).getBean();
        Filter paymentFilter = new CompareFilter("orderId", order.getId(), CompareFilter.CompareType.Equals);
        ((FilterableContainer) paymentContainer).setFilter(paymentFilter);
        this.orderlineContainer.setDatasource(newDataSource);
        footer.setItemDataSource(this.dataSource);
        this.update();
    }

    public void setPrintDocGenerator(PrintDocGenerator printDocGenerator) {
        this.printDocGenerator = printDocGenerator;
    }

    public com.vaadin.data.Item getItemDataSource() {
        return this.dataSource;
    }

    private void update() {


        String numberString = "";
        String invoiceNumberString = "";
        String createdString = "";
        String invoicedString = "";
        String statusString = "";
        String changeString = "";
        boolean completed = false;
        boolean paid = false;


        if (dataSource != null) {
            long number = (Long) dataSource.getItemProperty("number").getValue();
            long invoiceNumber = (Long) dataSource.getItemProperty("invoiceNumber").getValue();
            Date created = (Date) dataSource.getItemProperty("dateCreated").getValue();
            Date invoiced = (Date) dataSource.getItemProperty("dateInvoiced").getValue();

            OrderStatus status = (OrderStatus) dataSource.getItemProperty("status").getValue();

            numberString = number < 0 ? "New Order" : "#" + number;
            invoiceNumberString = "InvoiceNo.: " + (invoiceNumber > 0 ? invoiceNumber : "-");
            createdString = "Created: " + dateFormat.format(created);
            invoicedString = "Invoiced: " + (invoiced == null ? "-" : dateFormat.format(invoiced));
            statusString = getStatusString(status);

            double due = getDue();
            double change = getChange();
            if (status.isConfirmedState()) {
                if (due > 0) {
                    statusString = "Due: " + numberFormat.format(due);
                } else {
                    changeString = "Change: " + numberFormat.format(change);
                }
            }
            completed = status.isConfirmedState();
            paid = status == OrderStatus.Completed;

        }

        header.getLabelDue().setValue(changeString);
        header.getLabelOrderno().setValue(numberString);
        header.getLabelInvoiceno().setValue(invoiceNumberString);
        header.getLabelDateCreated().setValue(createdString);
        header.getLabelDateDelivered().setValue(invoicedString);
        header.getLabelStatus().setValue(statusString);

        if (!completed) {
            header.getButtonAdvanceOrder().setCaption("Conclude (F12)");
            header.getButtonAdvanceOrder().setEnabled(true);
        } else if (completed && !paid) {
            header.getButtonAdvanceOrder().setCaption("Pay (F12)");
            header.getButtonAdvanceOrder().setEnabled(true);
        } else {
            header.getButtonAdvanceOrder().setCaption("Pay (F12)");
            header.getButtonAdvanceOrder().setEnabled(false);
        }

        setEditable(!completed);
        footer.update();
    }

    @Override
    public void attach() {
        super.attach();
        this.settings = SystemSettings.getInstance(getApplication());

        if (!initialized) {
            if (getLocale() != null) {
                System.out.println("Locale: " + getLocale());
                numberFormat = NumberFormat.getCurrencyInstance(getLocale());
                Order order = getOrderFromDatasource();
                if (order != null) {
                    Currency currency = Currency.getInstance(order.getCurrency());
                    numberFormat.setCurrency(currency);
                }
            }

            dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, getLocale());
            amountValidator = new CurrencyAmountValidator(getLocale());
            percentageValidator = new PercentageValidator(getLocale());
            numberValidator = new NumberValidator(getLocale());

            //We have to wait until attached do get the correct locale
            propertyWrapper = new PropertyWrapper(getLocale());
            containerFormattingWrapper.addWrapper("price", propertyWrapper);
            containerFormattingWrapper.addWrapper("priceWithTax", propertyWrapper);
            containerFormattingWrapper.addWrapper("discountPercentage", propertyWrapper);
            containerFormattingWrapper.addWrapper("quantity", propertyWrapper);
            containerFormattingWrapper.addWrapper("totalWithTax", propertyWrapper);
            table.setContainerDataSource(containerFormattingWrapper);

            table.setContainerDataSource(this.containerFormattingWrapper);

            table.setVisibleColumns(shownTableProperties);
            table.setColumnHeaders(shownTableHeaders);
            table.setColumnCollapsingAllowed(true);
            table.setColumnCollapsed("price", true);

            table.setColumnAlignment("quantity", Table.ALIGN_RIGHT);
            table.setColumnAlignment("price", Table.ALIGN_RIGHT);
            table.setColumnAlignment("priceWithTax", Table.ALIGN_RIGHT);
            table.setColumnAlignment("discountPercentage", Table.ALIGN_RIGHT);
            table.setColumnAlignment("totalWithTax", Table.ALIGN_RIGHT);

            table.setColumnWidth("delete", 24);
            table.setColumnWidth("price", 90);
            table.setColumnWidth("priceWithTax", 90);
            table.setColumnWidth("discountPercentage", 55);
            table.setColumnWidth("quantity", 80);
            table.setColumnWidth("totalWithTax", 120);

            //setEditable(true);
            initialized = true;
        }

    }

    private boolean isEditable() {
        return editable;
    }

    private void setEditable(boolean editable) {
        if (this.editable != null && this.editable == editable) {
            return;
        }

        this.editable = editable;

        header.getSearchField().setEnabled(editable);
        header.getBarcodeField().setEnabled(editable);
        header.getButtonAddLine().setEnabled(editable);
        header.getButtonPrint().setEnabled(!editable);

        /*
        if (editable) {
        if (!hasEditableColumns) {
        table.addGeneratedColumn("price", editableColumnGenerator);
        table.addGeneratedColumn("priceWithTax", editableColumnGenerator);
        table.addGeneratedColumn("discountPercentage", editableColumnGenerator);
        table.addGeneratedColumn("quantity", editableColumnGenerator);
        //table.addGeneratedColumn("totalWithTax", updateableTotalGenerator);
        hasEditableColumns = true;
        }
        } else {
        table.removeGeneratedColumn("price");
        table.removeGeneratedColumn("priceWithTax");
        table.removeGeneratedColumn("quantity");
        table.removeGeneratedColumn("discountPercentage");
        //table.removeGeneratedColumn("totalWithTax");
        hasEditableColumns = false;
        }*/

        table.setEditable(editable);

    }

    public void setProductCrud(Filterable<String, Product> productCrud) {
        this.productCrud = productCrud;
    }

    public void commit() {
        if (hasChanges) {

            this.dataSource.getItemProperty("dateChanged").setValue(new Date());
            Property prop = this.dataSource.getItemProperty("status");
            if (OrderStatus.New.equals(prop.getValue())) {
                prop.setValue(OrderStatus.Processing);
            }

            if (this.dataSource instanceof Buffered) {
                ((Buffered) this.dataSource).commit();
            }

            hasChanges = false;
        }
    }

    @Override
    public void detach() {
        super.detach();
        commit();
    }

    private String getStatusString(OrderStatus status) {
        if (status.isConfirmedState()) {
            return status.name();
        }

        return "Processing";

    }

    public void valueChange(ValueChangeEvent event) {
        update();
    }

    private void doPrint() {
        if (printDocGenerator == null) {
            getApplication().getMainWindow().showNotification("PrintDocGenerator not available.", Window.Notification.TYPE_ERROR_MESSAGE);
            return;
        }

        try {

            PrintService ps = footer.getSelectedPrintService();
            if (ps == null) {
                getApplication().getMainWindow().showNotification("No printer available.", Window.Notification.TYPE_TRAY_NOTIFICATION);
                return;
            }
            
            /*

            String printTypeSettingKey = PrinterUtil.generatePrintTypeSettingKey(ps);
            String strPrintType = settings.get(printTypeSettingKey);
            PrintType printType = strPrintType == null ? PrintType.Receipt : PrintType.valueOf(strPrintType);

            Order order = (Order) ((HasBean<Order>) dataSource).getBean();

            switch (printType) {
                case Invoice:
                    printDocGenerator.printInvoice(order, ps);
                    break;
                default:
                    //doc = printDocGenerator.generateReceipt(order, PageSize.Receipt);
                    break;
            }
            */
            /*
            Doc doc;

            switch (printType) {
                case Invoice:
                    doc = printDocGenerator.generateInvoice(order, PageSize.A4);
                    break;
                default:
                    doc = printDocGenerator.generateReceipt(order, PageSize.Receipt);
                    break;
            }

            ps.createPrintJob().print(doc, null);
            */
        } catch (Exception ex) {
            LOG.error("Error printing invoice or receipt.", ex);
            getApplication().getMainWindow().showNotification(ex.getMessage(), Window.Notification.TYPE_ERROR_MESSAGE);
        }
    }

    private void doComplete(boolean openPayDialog) {

        Property propStatus = dataSource.getItemProperty("status");
        propStatus.setValue(OrderStatus.Accepted);
        hasChanges = true;
        commit();
        update();

        if(openPayDialog) {
            doPay();
        }

    }

    private void doPay() {
        final PaymentPanel panel = new PaymentPanel();
        double due = getDue();
        panel.setDue(due);
        panel.setCurrency((String) dataSource.getItemProperty("currency").getValue());
        panel.setValue(getDue());

        final CommonDialog dialog = new CommonDialog("Payment", panel);
        dialog.setButtonCaption(CommonDialog.ButtonType.Ok, "Pay");
        dialog.setDefaultButtonType(CommonDialog.ButtonType.Ok);
        dialog.setWidth(430, UNITS_PIXELS);
        dialog.setHeight(220, UNITS_PIXELS);
        dialog.center();
        getApplication().getMainWindow().addWindow(dialog);
        dialog.addListener(new Window.CloseListener() {

            public void windowClose(CloseEvent e) {
                if (dialog.getResult() == CommonDialog.ButtonType.Ok) {
                    PaymentType pt = panel.getPaymentType();
                    double value = (Double) panel.getValue();

                    addPayment(pt, value);

                    OrderStatus status = (OrderStatus) dataSource.getItemProperty("status").getValue();
                    if (OrderStatus.Completed.equals(status)) {
                        double due = getDue();
                        if (due < 0) {
                            addPayment(PaymentType.Change, due);
                        }

                        /*String cookieVal = settings.get(PrinterUtil.PRINT_ON_COMPLETE_SETTING);
                        boolean print = "true".equalsIgnoreCase(cookieVal);
                        if (print) {
                            doPrint();
                        }*/
                    }
                }
            }
        });
    }

    private double getPaidValue() {
        if (paymentContainer == null) {
            return 0;
        }
        double value = 0;
        for (Object id : paymentContainer.getItemIds()) {
            Item item = paymentContainer.getItem(id);
            value += (Double) item.getItemProperty("amount").getValue();
        }
        return value;
    }

    private double getDue() {
        if (dataSource == null) {
            return 0;
        }
        double orderTotal = (Double) dataSource.getItemProperty("totalWithTax").getValue();
        return orderTotal - getPaidValue();
    }

    private double getChange() {
        if (paymentContainer == null) {
            return 0;
        }
        double value = 0;
        for (Object id : paymentContainer.getItemIds()) {
            Item item = paymentContainer.getItem(id);
            if (PaymentType.Change.equals(item.getItemProperty("paymentType").getValue())) {
                value -= (Double) item.getItemProperty("amount").getValue();
            }
        }
        return value < -0.00 || value > 0.00 ? value : 0;
    }

    private void addPayment(PaymentType paymentType, double value) {
        Order order = getOrderFromDatasource();
        Object id = paymentContainer.addItem();
        Item item = paymentContainer.getItem(id);
        item.getItemProperty("amount").setValue(value);
        item.getItemProperty("paymentType").setValue(paymentType);
        item.getItemProperty("orderId").setValue(order.getId());

        if (item instanceof Buffered) {
            ((Buffered) item).commit();
        }

    }

    private Order getOrderFromDatasource() {
        if (dataSource != null && dataSource instanceof HasBean) {
            return ((HasBean<Order>) dataSource).getBean();
        }
        return null;
    }
}
