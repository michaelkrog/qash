package dk.apaq.shopsystem.ui;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ConversionException;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import dk.apaq.shopsystem.entity.PaymentType;
import dk.apaq.shopsystem.ui.data.util.CurrencyAmountFormatter;
import dk.apaq.shopsystem.ui.common.SwitchButtonGroup;
import java.text.NumberFormat;

/**
 *
 * @author krog
 */
public class PaymentPanel extends CustomComponent implements Property, Property.Editor {

    private static final PaymentType[] PAYMENTTYPES = {PaymentType.Cash, PaymentType.CreditCard, PaymentType.Transfer};
    private static final String[] PAYMENTTYPEICONS = {"img/cash_16.png", "img/creditcard_16.png", "img/transfer_16.png"};
    private String currency = "USD";
    private ObjectProperty changeProperty = new ObjectProperty(new Double(0.0));
    private Property property = new ObjectProperty(new Double(0.0));
    private Property dueProperty = new ObjectProperty(new Double(0.0));
    private ChangeHandler changeHandler = new ChangeHandler();
    private SwitchButtonGroup buttonGroup = new SwitchButtonGroup();
    private VerticalLayout layout = new VerticalLayout();
    private HorizontalLayout changeLayout = new HorizontalLayout();
    private TextField textAmount = new TextField();
    private Label lblChange = new Label("0,-");
    private Label lblChangeTitle = new Label("Change:");
    private NumberFormat numberFormat;
    private PaymentType paymentType = PaymentType.Cash;

    private class ChangeHandler implements Property.ValueChangeListener, TextChangeListener,
            Button.ClickListener {

        public void valueChange(ValueChangeEvent event) {
            update((Double) event.getProperty().getValue());
        }

        public void textChange(TextChangeEvent event) {
            double value;
            String text = event.getText();

            try {
                value = numberFormat.parse(text).doubleValue();
            } catch (Exception e) {
                value = 0;
            }

            update(value);
        }

        private void update(double value) {
            Double due = (Double) dueProperty.getValue();
            Double newChange = (due - value) * -1;
            newChange = (newChange < -0.00 || newChange > 0.00) ? newChange : 0;
            changeProperty.setValue(newChange);
        }

        public void buttonClick(ClickEvent event) {
            paymentType = (PaymentType) event.getButton().getData();
            textAmount.focus();
            textAmount.selectAll();
        }
    }

    public PaymentPanel() {
        layout.addComponent(buttonGroup);
        layout.addComponent(textAmount);
        layout.addComponent(changeLayout);
        layout.setStyleName(Reindeer.LAYOUT_WHITE);
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setComponentAlignment(textAmount, Alignment.TOP_RIGHT);
        layout.setComponentAlignment(changeLayout, Alignment.TOP_RIGHT);

        changeLayout.addComponent(lblChangeTitle);
        changeLayout.addComponent(lblChange);

        buttonGroup.setWidth(100, UNITS_PERCENTAGE);
        lblChangeTitle.setStyleName(Reindeer.LABEL_SMALL);
        lblChangeTitle.setSizeUndefined();
        lblChange.setStyleName(Reindeer.LABEL_SMALL);
        textAmount.setStyleName(ShopSystemTheme.TEXTFIELD_XLARGE);
        textAmount.addStyleName(ShopSystemTheme.TEXTALIGN_RIGHT);
        textAmount.setImmediate(true);

        for (PaymentType p : PAYMENTTYPES) {
            Button button = buttonGroup.addButton(p.name());
            button.setData(p);
            button.addListener((Button.ClickListener) changeHandler);
        }
        setCompositionRoot(layout);

        ((Property.ValueChangeNotifier) property).addListener(changeHandler);
        textAmount.addListener((TextChangeListener) changeHandler);
        textAmount.setTextChangeTimeout(500);

        bindProperties();
    }

    public void setPropertyDataSource(Property newDataSource) {
        if (newDataSource.getType() != Double.class) {
            throw new IllegalArgumentException("Property must be of type Double");
        }
        this.property = newDataSource;
    }

    public Property getPropertyDataSource() {
        return property;
    }

    public void setDue(double value) {
        this.dueProperty.setValue(value);
    }

    public void setCurrency(String value) {
        this.currency = value;
        bindProperties();
    }

    private void bindProperties() {
        Property formattedAmount = buildFormatter(property);
        Property formattedChange = buildFormatter(changeProperty);

        textAmount.setPropertyDataSource(formattedAmount);
        lblChange.setPropertyDataSource(formattedChange);

    }

    private CurrencyAmountFormatter buildFormatter(Property prop) {
        return new CurrencyAmountFormatter(prop, getLocale(), currency);
    }

    public Object getValue() {
        return property.getValue();
    }

    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
        property.setValue(newValue);
    }

    public Class<?> getType() {
        return Double.class;
    }

    @Override
    public void attach() {
        this.numberFormat = NumberFormat.getNumberInstance(getLocale());
        this.numberFormat.setMinimumFractionDigits(2);
        this.numberFormat.setMaximumFractionDigits(2);
        textAmount.focus();
        textAmount.selectAll();
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }
}
