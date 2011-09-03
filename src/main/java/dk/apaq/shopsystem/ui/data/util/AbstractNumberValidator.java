/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.ui.data.util;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.PropertyFormatter;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author michaelzachariassenkrog
 */
public abstract class AbstractNumberValidator  implements Validator {

    private final NumberFormat nf;

    public AbstractNumberValidator(NumberFormat nf) {
        this.nf = nf;
    }

    public NumberFormat getNumberFormat() {
        return nf;
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
    public void validate(Object value) throws InvalidValueException {
        if (!isValid(value)) {
            throw new InvalidValueException("Should be a valid number.");
        }
    }

}