/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.qash.data.util;

import com.vaadin.data.Property;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author michaelzachariassenkrog
 */
public class PercentageFormatter extends AbstractNumberFormatter {

    public PercentageFormatter(Property property) {
        this(property, Locale.getDefault());
    }

    public PercentageFormatter(Property property, Locale locale) {
        super(property);
        NumberFormat nf = NumberFormat.getPercentInstance(locale);
        nf.setMaximumFractionDigits(3);
        nf.setMinimumFractionDigits(0);
        setNumberFormat(nf);
    }

    @Override
    public boolean isValid(Object value) {
        return super.isValid(fixValue(value));
    }

    @Override
    public Object parse(String formattedValue) throws Exception {
        return super.parse(fixValue(formattedValue));
    }

    @Override
    public void validate(Object value) throws InvalidValueException {
        super.validate(fixValue(value));
    }


    private String fixValue(Object value) {
        String str = (String) value;
        if(!str.endsWith("%")) {
            str = str + "%";
        }
        return str;
    }

}
