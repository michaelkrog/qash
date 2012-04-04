package dk.apaq.shopsystem.qash.data.util;

import com.vaadin.data.util.BeanItem;
import dk.apaq.shopsystem.entity.Product;
import java.util.Locale;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author krog
 */
public class CurrencyAmountFormatterTest {
    
    

    @Test
    public void testFormat() {
        Product p = new Product();
        p.setPrice(9995);
        BeanItem<Product> bi = new BeanItem<Product>(p);
        
        CurrencyAmountFormatter amountFormatter = new CurrencyAmountFormatter(bi.getItemProperty("price"), Locale.US, "USD");
        String result = (String) amountFormatter.getValue();
        
        assertEquals(/* $ */"99.95", result);
    }
}
