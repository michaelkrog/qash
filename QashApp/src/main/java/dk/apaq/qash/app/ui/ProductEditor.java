package dk.apaq.qash.app.ui;

import com.vaadin.data.Buffered;
import com.vaadin.data.Container;
import com.vaadin.data.Validator.InvalidValueException;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import dk.apaq.qash.app.QashTheme;
import dk.apaq.qash.app.data.util.CurrencyAmountFormatter;
import dk.apaq.qash.app.data.util.CurrencyAmountValidator;
import dk.apaq.qash.app.ui.common.FieldEnabler;
import dk.apaq.qash.share.model.Tax;
import dk.apaq.vaadin.addon.crudcontainer.HasBean;

/**
 *
 * @author michaelzachariassenkrog
 */
public class ProductEditor extends CustomComponent implements
        com.vaadin.data.Item.Editor, Buffered {

    private final TabSheet tabSheet = new TabSheet();
    private final GeneralPanel generalPanel = new GeneralPanel();
    private final PricePanel pricePanel = new PricePanel();
    private final StockPanel stockPanel = new StockPanel();
    private VerticalLayout layout = new VerticalLayout();
    private Item dataSource;
    private CurrencyAmountValidator amountValidator;
    private boolean writeThrough;

    private Map<String, Property.Editor> propertyMap = new HashMap<String, Property.Editor>();

    private class TaxWrapper implements Property {

        private final Property wrapped;
        private final Container taxDataSource;

        public TaxWrapper(Property wrapped, Container datasource) {
            this.wrapped = wrapped;
            this.taxDataSource = datasource;
        }

        public Object getValue() {
            Tax tax = (Tax)wrapped.getValue();
            if(tax==null) {
                return null;
            }
            return tax.getId();
        }

        public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
            String id = (String)newValue;
            if(id==null || "".equals(id)) {
                wrapped.setValue(null);
                return;
            }

            Item item = taxDataSource.getItem(id);
            Tax tax = ((HasBean<Tax>)item).getBean();
            wrapped.setValue(tax);
        }

        public Class<?> getType() {
            return String.class;
        }

        public boolean isReadOnly() {
            return wrapped.isReadOnly();
        }

        public void setReadOnly(boolean newStatus) {
            wrapped.setReadOnly(newStatus);
        }



    }


    private class GeneralPanel extends CustomComponent {
        private final GridLayout layout = new GridLayout(2, 3);
        private final Label lblTitle = new Label("Title:");
        private final Label lblItemNo = new Label("Itemno.:");
        private final Label lblBarcode = new Label("Barcode:");
        private final TextField txtTitle = new TextField();
        private final TextField txtItemnNo = new TextField();
        private final TextField txtBarcode = new TextField();


        public GeneralPanel() {

            lblTitle.setSizeUndefined();
            lblTitle.setStyleName("bold");

            lblItemNo.setSizeUndefined();
            lblItemNo.setStyleName("bold");

            lblBarcode.setSizeUndefined();
            lblBarcode.setStyleName("bold");

            layout.addComponent(lblTitle);
            layout.addComponent(txtTitle);
            layout.addComponent(lblItemNo);
            layout.addComponent(txtItemnNo);
            layout.addComponent(lblBarcode);
            layout.addComponent(txtBarcode);


            layout.setSpacing(true);
            layout.setMargin(true);
            
            layout.setComponentAlignment(lblTitle, Alignment.MIDDLE_RIGHT);
            layout.setComponentAlignment(lblItemNo, Alignment.MIDDLE_RIGHT);
            layout.setComponentAlignment(lblBarcode, Alignment.MIDDLE_RIGHT);

            setCompositionRoot(layout);
        }

        public TextField getTxtBarcode() {
            return txtBarcode;
        }

        public TextField getTxtItemnNo() {
            return txtItemnNo;
        }

        public TextField getTxtTitle() {
            return txtTitle;
        }



    }

    private class PricePanel extends CustomComponent {
        private final GridLayout layout = new GridLayout(3, 3);
        private final HorizontalLayout priceLayout = new HorizontalLayout();
        private final Label lblPrice = new Label("Price:");
        private final Label lblTax = new Label("Tax:");
        private final CheckBox chkFixedPrice = new CheckBox("Use fixed price");
        private final Label lblDiscount = new Label("Discount(%):");
        private final TextField txtPriceExTax = new TextField("Excluding taxes");
        private final TextField txtPriceIncTax = new TextField("Including taxes");
        private final ComboBox cmbTax = new ComboBox();
        private final TextField txtDiscount = new TextField();

        public PricePanel() {

            lblPrice.setSizeUndefined();
            lblPrice.setStyleName("bold");

            lblTax.setSizeUndefined();
            lblTax.setStyleName("bold");
            
            layout.addComponent(chkFixedPrice, 1, 0);
            layout.addComponent(lblPrice, 0 ,1);
            layout.addComponent(priceLayout, 1, 1);
            layout.addComponent(lblTax, 0, 2);
            layout.addComponent(cmbTax, 1, 2);

            layout.setSpacing(true);
            layout.setMargin(true);

            priceLayout.addComponent(txtPriceExTax);
            priceLayout.addComponent(txtPriceIncTax);
            priceLayout.setSpacing(true);
            
            layout.setComponentAlignment(lblPrice, Alignment.BOTTOM_RIGHT);
            layout.setComponentAlignment(lblTax, Alignment.MIDDLE_RIGHT);

            chkFixedPrice.setImmediate(true);

            cmbTax.setItemCaptionPropertyId("name");
            FieldEnabler fieldEnabler = new FieldEnabler(chkFixedPrice, txtPriceExTax, txtPriceIncTax);

            setCompositionRoot(layout);
        }

        public CheckBox getChkFixedPrice() {
            return chkFixedPrice;
        }


        public ComboBox getCmbTax() {
            return cmbTax;
        }

        public TextField getTxtDiscount() {
            return txtDiscount;
        }

        public TextField getTxtPriceExTax() {
            return txtPriceExTax;
        }

        public TextField getTxtPriceIncTax() {
            return txtPriceIncTax;
        }


    }

    private class StockPanel extends CustomComponent {
        private final GridLayout layout = new GridLayout(2, 2);
        private final Label lblStock = new Label("");
        private final Label lblAmount = new Label("Amount:");
        private final CheckBox chkStock = new CheckBox("Inventory management");
        private final TextField txtAmount = new TextField();

        public StockPanel() {

            lblStock.setSizeUndefined();
            lblStock.setStyleName(QashTheme.LABEL_BOLD);

            lblAmount.setSizeUndefined();
            lblAmount.setStyleName("bold");

            layout.addComponent(lblStock);
            layout.addComponent(chkStock);
            layout.addComponent(lblAmount);
            layout.addComponent(txtAmount);

            layout.setSpacing(true);
            layout.setMargin(true);

            layout.setComponentAlignment(lblStock, Alignment.MIDDLE_RIGHT);
            layout.setComponentAlignment(lblAmount, Alignment.MIDDLE_RIGHT);

            chkStock.setImmediate(true);

            FieldEnabler fieldEnabler = new FieldEnabler(chkStock, txtAmount);

            setCompositionRoot(layout);
        }

        public TextField getTxtAmount() {
            return txtAmount;
        }

        public CheckBox getChkStock() {
            return chkStock;
        }


    }

    public ProductEditor() {

        propertyMap.put("name", generalPanel.getTxtTitle());
        propertyMap.put("itemNo", generalPanel.getTxtItemnNo());
        propertyMap.put("barcode", generalPanel.getTxtBarcode());
        propertyMap.put("price", pricePanel.getTxtPriceExTax());
        propertyMap.put("priceWithTax", pricePanel.getTxtPriceIncTax());
        propertyMap.put("stockProduct", stockPanel.getChkStock());
        propertyMap.put("quantityInStock", stockPanel.getTxtAmount());
        propertyMap.put("tax", pricePanel.getCmbTax());

        tabSheet.addTab(generalPanel, "General", null);
        tabSheet.addTab(pricePanel, "Prices", null);
        tabSheet.addTab(stockPanel, "Stock", null);
        tabSheet.setSizeFull();

        layout.addComponent(tabSheet);
        layout.setSizeFull();
        
        setStyleName("v-producteditor");
        setCompositionRoot(layout);
    }

    public Container getTaxContainerDatasource() {
        return this.pricePanel.getCmbTax().getContainerDataSource();
    }

    public void setTaxContainerDatasource(Container container) {
        this.pricePanel.getCmbTax().setContainerDataSource(container);
    }

    public void setItemDataSource(com.vaadin.data.Item newDataSource) {
            this.pricePanel.getChkFixedPrice().setValue(true);
            this.stockPanel.getChkStock().setValue(true);
            this.dataSource = newDataSource;

            for(Map.Entry entry : propertyMap.entrySet()) {
                String propertyId = (String) entry.getKey();
                Property property = this.dataSource.getItemProperty(propertyId);

                if(property==null) {
                    throw new IllegalArgumentException("Item does not have a property with propertid '"+propertyId+"'");
                }

                property = wrapFormatIfNeccesary(propertyId, property);

                Property.Editor editor = (Property.Editor) entry.getValue();

                if(property.getValue()==null && property.getType()==String.class) {
                    property.setValue("");
                }

                editor.setPropertyDataSource(property);
            }

            Double price = (Double) this.dataSource.getItemProperty("price").getValue();
            if(price > 0.001) {
                this.pricePanel.getChkFixedPrice().setValue(true);
            } else {
                this.pricePanel.getChkFixedPrice().setValue(false);
            }
        
        }

    public Item getItemDataSource() {
        return this.dataSource;
    }

    public void commit() throws SourceException, InvalidValueException {
        if(!this.pricePanel.getChkFixedPrice().booleanValue()) {
            this.pricePanel.getTxtPriceExTax().setValue("0");
        }

        for(Property.Editor e : propertyMap.values()) {
            if(e instanceof Buffered) {
                ((Buffered)e).commit();
            }
        }
    }

    public void discard() throws SourceException {
        for(Property.Editor e : propertyMap.values()) {
            if(e instanceof Buffered) {
                ((Buffered)e).discard();
            }
        }
    }

    public boolean isWriteThrough() {
        return writeThrough;
    }

    public void setWriteThrough(boolean writeThrough) throws SourceException, InvalidValueException {
        this.writeThrough = writeThrough;
        for(Property.Editor e : propertyMap.values()) {
            if(e instanceof Buffered) {
                ((Buffered)e).setWriteThrough(writeThrough);
            }
        }
    }

    public boolean isReadThrough() {
        return true;
    }

    public void setReadThrough(boolean readThrough) throws SourceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isModified() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Property wrapFormatIfNeccesary(String name, Property property) {
        if("price".equals(name)) {
            return new CurrencyAmountFormatter(property, getLocale());
        }

        if("priceWithTax".equals(name)) {
            return new CurrencyAmountFormatter(property, getLocale());
        }

        if("tax".equals(name)) {
            return new TaxWrapper(property, this.pricePanel.cmbTax.getContainerDataSource());
        }

        return property;
    }

    @Override
    public void attach() {
        super.attach();

        this.amountValidator = new CurrencyAmountValidator(getLocale());

        this.pricePanel.getTxtPriceExTax().addValidator(amountValidator);
        this.pricePanel.getTxtPriceExTax().setInvalidAllowed(false);
        this.pricePanel.getTxtPriceExTax().setImmediate(true);
        this.pricePanel.getTxtPriceIncTax().addValidator(amountValidator);
        this.pricePanel.getTxtPriceIncTax().setInvalidAllowed(false);
        this.pricePanel.getTxtPriceIncTax().setImmediate(true);
        this.pricePanel.getCmbTax().setImmediate(true);


    }




}
