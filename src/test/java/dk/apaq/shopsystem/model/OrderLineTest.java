/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.model;

import dk.apaq.shopsystem.entity.OrderLine;
import dk.apaq.shopsystem.entity.Tax;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author michaelzachariassenkrog
 */
public class OrderLineTest {

    public OrderLineTest() {
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

    @Test
    public void testTotals() {
        OrderLine line = new OrderLine();
        Tax tax = new Tax("Moms", 25.0);
        line.setPrice(80);
        line.setTax(tax);
        line.setQuantity(2);

        assertEquals(100, line.getPriceWithTax(),0);
        assertEquals(200, line.getTotalWithTax(),0);

        line.setDiscountPercentage(0.2);

        assertEquals(100, line.getPriceWithTax(),0);
        assertEquals(160, line.getTotalWithTax(),0);


    }

}