package dk.apaq.shopsystem.qash.data.util;

import com.vaadin.data.Property;
import com.vaadin.data.util.PropertyFormatter;
import java.text.NumberFormat;

/**
 *
 * @author krog
 */
public class AbstractMoneyFormatter extends PropertyFormatter {

    private NumberFormat nf;

    public AbstractMoneyFormatter(Property propertyDataSource) {
        super(propertyDataSource);
    }

    public void setNumberFormat(NumberFormat numberFormat) {
        this.nf = numberFormat;
    }

    public NumberFormat getNumberFormat() {
        return nf;
    }
    
    @Override
    public String format(Object value) {
        if (value == null || nf == null) {
            return null;
        }
        
        
        Number number = (Number) value;
        
        return nf.format(number.doubleValue() / 100);
    }

    @Override
    public Object parse(String formattedValue) throws Exception {
        if (formattedValue == null) {
            return null;
        }
        
        Number number = nf.parse(formattedValue);
        Double dValue = number.doubleValue() * 100;
        return dValue.longValue();
        
    }
    
}
