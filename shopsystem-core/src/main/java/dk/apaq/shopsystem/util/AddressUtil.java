package dk.apaq.shopsystem.util;

import dk.apaq.shopsystem.entity.HasAddress;
import java.util.Locale;

/**
 *
 * @author michael
 */
public class AddressUtil {
    
    public static String formatAddress(HasAddress address, Locale locale, String lineSeperator) {
        StringBuilder sb = new StringBuilder();
        
        if(address.getCity() == null) {
            return sb.toString();
        }
        sb.insert(0, lineSeperator);
        sb.insert(0, address.getCity());
        
        
        if(address.getPostalCode() == null) {
            return sb.toString();
        }
        sb.insert(0, " ");
        sb.insert(0, address.getPostalCode());
        
        
        if(address.getStreet() == null) {
            return sb.toString();
        }
        sb.insert(0, lineSeperator);
        sb.insert(0, address.getStreet());
        
        return sb.toString();
    }
    
   
}
