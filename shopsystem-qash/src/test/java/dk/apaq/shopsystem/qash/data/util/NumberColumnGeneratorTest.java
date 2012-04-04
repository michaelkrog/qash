package dk.apaq.shopsystem.qash.data.util;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Label;
import dk.apaq.shopsystem.entity.Product;
import java.util.Locale;
import com.vaadin.ui.Table;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author krog
 */
public class NumberColumnGeneratorTest {
    /**
     * Test of generateCell method, of class NumberColumnGenerator.
     */
    @Test
    public void testGenerateCell() {
        System.out.println("generateCell");
        Product product = new Product();
        product.setId("123");
        product.setQuantityInStock(1.2345);
        
        Table source = new Table();
        source.setLocale(Locale.US);
        
        BeanContainer<String, Product> c = new BeanContainer(Product.class);
        c.setBeanIdProperty("id");
        c.addBean(product);
        
        source.setContainerDataSource(c);
        
        Object itemId = "123";
        Object columnId = "quantityInStock";
        NumberColumnGenerator instance = new NumberColumnGenerator(1, 3);
        String expResult = "1.234";
        String result = (String) ((Label)instance.generateCell(source, itemId, columnId)).getValue();
        assertEquals(expResult, result);
    }

    
}
