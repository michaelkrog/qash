package dk.apaq.shopsystem.entity;

import java.io.Serializable;
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
@Entity
public class ComponentParameter implements Serializable {
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Logger LOG = LoggerFactory.getLogger(ComponentParameter.class);
    
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
    protected String id;
    
    private String type;
    private String value;

    public String getId() {
        return id;
    }
    
    public Class getType() {
        if(type==null) {
            return null;
        }
        
        if("String".equals(type)) {
            return String.class;
        }
        
        if("Double".equals(type)) {
            return Double.class;
        }
        
        if("Integer".equals(type)) {
            return Integer.class;
        }
        
        if("Date".equals(type)) {
            return Date.class;
        }
        
        if("Boolean".equals(type)) {
            return Boolean.class;
        }
    
        return null;
    }
    
    public Date getDate() {
        if(value==null) {
            return null;
        }
        try {
            return df.parse(value);
        } catch (ParseException ex) {
            LOG.debug("Unable to parse date.", ex);
            return null;
        }
    }
    
    public String getString() {
        return value;
    }
    
    public Integer getInteger() {
        if(value==null) {
            return null;
        }
        return Integer.parseInt(value);
    }
    
    public Double getDouble() {
        if(value==null) {
            return null;
        }
        return Double.parseDouble(value);
    }
    
    public Boolean getBoolean() {
        if(value==null) {
            return null;
        }
        return Boolean.parseBoolean(value);
    }
    
    public void setDate(Date date) {
        value = df.format(date);
        type = "Date";
    }
    
    public void setString(String value) {
        this.value =value;
        this.type = "String";
    }
    
    public void setInteger(Integer value) {
        this.value = value.toString();
        this.type = "Integer";
    }
    
    public void setDouble(Double value) {
        this.value = value.toString();
        this.type = "Double";
    }
    
    public void setBoolean(Boolean value) {
        this.value = value.toString();
        this.type = "Boolean";
    }
}
