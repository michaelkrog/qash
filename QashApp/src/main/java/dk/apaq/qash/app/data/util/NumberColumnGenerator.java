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
public class NumberColumnGenerator implements ColumnGenerator{

    private int minFractionDigits = 0;
    private int maxFractionDigits = 3;

    public NumberColumnGenerator() {
    }

    public NumberColumnGenerator(int minFractionDigits, int maxFractionDigits) {
        this.minFractionDigits = minFractionDigits;
        this.maxFractionDigits = maxFractionDigits;
    }


    public Component generateCell(Table source, Object itemId, Object columnId) {
        Locale locale = source.getLocale();
        if(locale == null) {
            locale = Locale.getDefault();
        }
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        nf.setMinimumFractionDigits(minFractionDigits);
        nf.setMaximumFractionDigits(maxFractionDigits);
        
        Property prop = source.getContainerProperty(itemId, columnId);
        Double value = (Double) prop.getValue();

        return new Label(nf.format(value));
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
