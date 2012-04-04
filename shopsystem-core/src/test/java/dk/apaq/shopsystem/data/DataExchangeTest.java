package dk.apaq.shopsystem.data;

import dk.apaq.shopsystem.entity.Order;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
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
public class DataExchangeTest {
    
    public DataExchangeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of writePostings method, of class DataExchange.
     */
    @Test
    public void testWritePostings() {
        System.out.println("writePostings");
        
        List<Order> orderlist = new ArrayList<Order>();
        Order order = new Order();
        order.addOrderLine("Test", 1, 129995, null);
        orderlist.add(order);
        
        
        
        int account = 123;
        int offsetaccount = 321;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataExchange.writePostings(orderlist, account, offsetaccount, out);
        
        String text = new String(out.toByteArray());
        text = text.replaceAll("\r\n", "\n");
        assertEquals(text, "DATE;INVOICENUMBER;ITEMNO;DESCRIPTION;CURRENCY;TOTAL;TOTALINCVAT;TAXCODE;TAXRATE;ACCOUNT;OFFSETACCOUNT\n-;-1;;1.0XTest;USD;1,299.95;1,299.95;;0.0;123;321\n");

    }
}
