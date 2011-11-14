/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.apaq.shopsystem.qash.data.util;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.PropertyFormatter;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 *
 * @author michaelzachariassenkrog
 */
public class CurrencyAmountFormatter extends AbstractNumberFormatter {

    public CurrencyAmountFormatter(Property property) {
        this(property, null);
    }

    public CurrencyAmountFormatter(Property property, Locale locale) {
        this(property, locale, null);
    }

    public CurrencyAmountFormatter(Property property, Locale locale, String currency) {
        super(property);
        
        //If locale is null, then use default locale.
        locale = locale == null ? Locale.getDefault() : locale;
        
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        setNumberFormat(nf);
        
        if(currency!=null) {
            nf.setCurrency(Currency.getInstance(currency));
        }

    }
}
