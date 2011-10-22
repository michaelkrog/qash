package dk.apaq.shopsystem.rendering.media;

import dk.apaq.filter.Filter;
import dk.apaq.filter.core.BeanFiltrationItem;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author michael
 */
public class FilterTranslatorForCssMediaTest {
    
    public FilterTranslatorForCssMediaTest() {
    }

    
    /**
     * Test of translate method, of class FilterTranslatorForCssMedia.
     */
    @Test
    public void testTranslate() {
        System.out.println("translate");
        String cssMediaString = "all and (min-device-width:400) and (max-device-width:800), all and (min-device-width:1000) and (max-device-width:1200) and (max-device-height:1200)";
        FilterTranslatorForCssMedia instance = new FilterTranslatorForCssMedia();
        Filter result = instance.translate(cssMediaString);
        
        DeviceInfo item = new DeviceInfo();
        item.set("device-width", 500);
        assertTrue(result.passesFilter(item));
        
        
        item.set("device-width", 900);
        assertFalse(result.passesFilter(item));
        
        item.set("device-width", 1000);
        item.set("device-height", 1000);
        
        assertTrue(result.passesFilter(item));
        
    }
}
