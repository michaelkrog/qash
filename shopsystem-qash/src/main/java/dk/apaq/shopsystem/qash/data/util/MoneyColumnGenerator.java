package dk.apaq.shopsystem.qash.data.util;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

/**
 *
 * @author michael
 */
public class MoneyColumnGenerator extends NumberColumnGenerator {

    public MoneyColumnGenerator() {
        super(2, 2);
    }

    @Override
    public Component generateCell(Table source, Object itemId, Object columnId) {
        Property prop = source.getContainerProperty(itemId, columnId);
        Long value = (Long) prop.getValue();
        Double dValue =  value.doubleValue() / 100;
        
        return new Label(formatValue(source, dValue));
    }
    
    
}
