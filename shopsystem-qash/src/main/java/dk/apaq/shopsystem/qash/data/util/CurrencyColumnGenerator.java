/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.qash.data.util;

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
            
            if(prop == null) {
                throw new NullPointerException("'currencyPropertyId' specified, but column was not found.");
            }
            
            if(prop.getType() != String.class) {
                throw new IllegalArgumentException("Property for currency not of type String.class. [itemId=["+itemId+"]; currencyPropertyId="+currencyPropertyId+"]");
            }
            
            nf.setCurrency(Currency.getInstance((String)prop.getValue()));
        } else {
            nf.setCurrency(currency);
        }

        Property prop = source.getContainerProperty(itemId, columnId);
        
        if(prop == null) {
            throw new NullPointerException("Property not found. [itemId=["+itemId+"]; columnId="+columnId+"]");
        }
        
        if(prop.getType() != Long.class) {
            throw new IllegalArgumentException("Property not of type Long.class. [itemId=["+itemId+"]; columnId="+columnId+"]");
        }
        
        Long value = (Long) prop.getValue();
        Double dValue =  value.doubleValue() / 100;
        return new Label(nf.format(dValue));
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
