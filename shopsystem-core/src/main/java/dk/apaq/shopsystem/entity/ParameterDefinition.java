package dk.apaq.shopsystem.entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author michael
 */
public class ParameterDefinition {
    
    private final Object defaultValue;
    private final Class type;
    private final String optionalText;

    public ParameterDefinition(String type, Object defaultValue, String optionalText) {
        this.defaultValue = defaultValue;
        this.type = typeFromText(type);
        this.optionalText = optionalText;
    }

    
    public Object getDefaultValue() {
        return defaultValue;
    }

    public String getOptionalText() {
        return optionalText;
    }

    public Class getType() {
        return type;
    }
    
    private Class typeFromText(String text) {
        if("String".equalsIgnoreCase(text)) {
            return String.class;
        }
        
        if("Double".equalsIgnoreCase(text)) {
            return Double.class;
        }
        
        if("Integer".equalsIgnoreCase(text)) {
            return Integer.class;
        }
        
        if("Boolean".equalsIgnoreCase(text)) {
            return Boolean.class;
        }
        
        if("Date".equalsIgnoreCase(text)) {
            return Date.class;
        }
        
        throw new IllegalArgumentException("Unable to parse type. Only String, Double, Integer, Boolean and Date is supported.");
    }

}
