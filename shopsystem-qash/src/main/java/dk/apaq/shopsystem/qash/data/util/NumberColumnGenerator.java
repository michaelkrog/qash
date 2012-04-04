package dk.apaq.shopsystem.qash.data.util;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Generates a number formatted label for a column. If number is double it is formattede directly.
 * If number is an integer or a long 
 * @author michaelzachariassenkrog
 */
public class NumberColumnGenerator implements ColumnGenerator{

    private int minFractionDigits = 0;
    private int maxFractionDigits = 3;
    private final Map<Locale, NumberFormat> formatMap = new HashMap<Locale, NumberFormat>(); 
    
    public NumberColumnGenerator() {
    }

    public NumberColumnGenerator(int minFractionDigits, int maxFractionDigits) {
        this.minFractionDigits = minFractionDigits;
        this.maxFractionDigits = maxFractionDigits;
    }


    public Component generateCell(Table source, Object itemId, Object columnId) {
        Property prop = source.getContainerProperty(itemId, columnId);
        if(prop == null) {
            throw new NullPointerException("Property not found. [itemId=["+itemId+"]; columnId="+columnId+"]");
        }
        
        if(prop.getType() != Double.class) {
            throw new IllegalArgumentException("Property not of type Double.class. [itemId=["+itemId+"]; columnId="+columnId+"]");
        }
        
        Double value = (Double) prop.getValue();
        return new Label(formatValue(source, value));
    }
    
    
    protected String formatValue(Table source, Double value) {
        Locale locale = source.getLocale();
        if(locale == null) {
            locale = Locale.getDefault();
        }
        
        NumberFormat nf = formatMap.get(locale);
        if(nf==null) {
            nf = NumberFormat.getNumberInstance(locale);
            formatMap.put(locale, nf);
        }
        nf.setMinimumFractionDigits(minFractionDigits);
        nf.setMaximumFractionDigits(maxFractionDigits);
        return nf.format(value);
    }

    public void setMaxFractionDigits(int maxFractionDigits) {
        this.maxFractionDigits = maxFractionDigits;
    }

    public int getMaxFractionDigits() {
        return maxFractionDigits;
    }

    public void setMinFractionDigits(int minFractionDigits) {
        this.minFractionDigits = minFractionDigits;
    }

    public int getMinFractionDigits() {
        return minFractionDigits;
    }

    
}
