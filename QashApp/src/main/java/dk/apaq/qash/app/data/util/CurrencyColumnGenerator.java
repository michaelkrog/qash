/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.qash.app.data.util;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 *
 * @author michaelzachariassenkrog
 */
public class CurrencyColumnGenerator implements ColumnGenerator{

    private Currency currency = Currency.getInstance(Locale.getDefault());
    private Object currencyPropertyId = null;

    public Component generateCell(Table source, Object itemId, Object columnId) {
        Locale locale = source.getLocale();
        if(locale == null) {
            locale = Locale.getDefault();
        }
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

        if(currencyPropertyId!=null) {
            Property prop = source.getContainerProperty(itemId, currencyPropertyId);
            nf.setCurrency(Currency.getInstance((String)prop.getValue()));
        } else {
            nf.setCurrency(currency);
        }

        Property prop = source.getContainerProperty(itemId, columnId);
        Double value = (Double) prop.getValue();

        return new Label(nf.format(value));
    }

    public void setCurrency(String currency) {
        this.currency = Currency.getInstance(currency);
    }

    public String getCurrency() {
        return currency.getCurrencyCode();
    }

    public void setCurrencyPropertyId(Object currencyPropertyId) {
        this.currencyPropertyId = currencyPropertyId;
    }

    public Object getCurrencyPropertyId() {
        return currencyPropertyId;
    }




}
