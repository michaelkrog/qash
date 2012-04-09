package dk.apaq.shopsystem.qash.data.util;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import dk.apaq.shopsystem.entity.PriceTag;
import dk.apaq.shopsystem.entity.Product;
import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author krog
 */
public class MoneyColumnGeneratorTest {
   
    /**
     * Test of generateCell method, of class MoneyColumnGenerator.
     */
    @Test
    public void testGenerateCell() {
        System.out.println("generateCell");
        Product product = new Product();
        product.setId("123");
        product.getPriceTags().add(new PriceTag("DKK", 99.95));
        
        Table source = new Table();
        source.setLocale(Locale.US);
        
        BeanContainer<String, Product> c = new BeanContainer(Product.class);
        c.setBeanIdProperty("id");
        c.addBean(product);
        
        source.setContainerDataSource(c);
        
        Object itemId = "123";
        Object columnId = "price";
        MoneyColumnGenerator instance = new MoneyColumnGenerator();
        String expResult = "99.95";
        //String result = (String) ((Label)instance.generateCell(source, itemId, columnId)).getValue();
        //assertEquals(expResult, result);
        
    }
}
