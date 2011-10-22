package dk.apaq.shopsystem.rendering.media;

import dk.apaq.filter.FiltrationItem;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author michael
 */
public class DeviceInfo implements FiltrationItem {

    private final Map<String, Object> data = new HashMap<String, Object>();

    @Override
    public Object getFiltrationProperty(String propertyId) {
        return data.get(propertyId);
    }
    
    public Object get(String infoType) {
        return data.get(infoType);
    }
    
    public void set(String infoType, Object value) {
        if("device-width".equals(infoType) || "device-height".equals(infoType)) {
            Integer convertedValue = convertToInt(value);
            data.put(infoType, convertedValue);
        }
    }
    
    public int getInfoCount() {
        return data.size();
    }
    
    public Integer convertToInt(Object value) {
        if(value instanceof Integer) {
            return (Integer) value;
        }
        
        return Integer.parseInt((String)value);
    }
    
    
}
