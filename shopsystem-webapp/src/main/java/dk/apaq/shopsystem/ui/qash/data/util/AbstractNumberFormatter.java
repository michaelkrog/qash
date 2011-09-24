/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.ui.qash.data.util;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.PropertyFormatter;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author michaelzachariassenkrog
 */
public abstract class AbstractNumberFormatter extends PropertyFormatter implements Validator {

    private NumberFormat nf;

    public AbstractNumberFormatter(Property propertyDataSource) {
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
        return nf.format(value);
    }

    @Override
    public boolean isValid(Object value) {
        try {
            nf.parse((String) value);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Object parse(String formattedValue) throws Exception {
        if (formattedValue == null) {
            return null;
        }
        return nf.parse(formattedValue);
    }

    @Override
    public void validate(Object value) throws InvalidValueException {
        if (!isValid(value)) {
            throw new InvalidValueException("Should be a valid number.");
        }
    }
    

}
