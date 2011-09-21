/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.model;

import dk.apaq.shopsystem.entity.Product;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderLineTax;
import dk.apaq.shopsystem.entity.PaymentType;
import dk.apaq.shopsystem.entity.Tax;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author michael
 */
public class OrderTest extends TestCase {
    
    public OrderTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAddSameItemTwice(){
        Order order = new Order();
        Tax tax = new Tax();
        tax.setId("tax");
        tax.setName("Moms");
        tax.setRate(25.0);

        Product item = new Product();
        item.setName("Dims");
        item.setPrice(20.0);
        item.setId("ID");
        item.setItemNo("DIMS");
        item.setTax(tax);

        order.addOrderLine(item);
        order.addOrderLine(item);
        
        assertEquals(1, order.getOrderLineCount());
        assertEquals(40.0, order.getTotal());
        
    }

    public void testTax() {
        Order order = new Order();
        Tax tax = new Tax();
        tax.setId("tax");
        tax.setName("Moms");
        tax.setRate(25.0);
        order.addOrderLine("Dims", 1, 100, tax);

        List<OrderLineTax> taxlist = order.getTaxList();
        assertEquals(1, taxlist.size());
        assertEquals(25.0, order.getTotalTax(taxlist.get(0)));
    }


    public void testPaidAmount(){
        Order order = new Order();
        //order.addPayment(PaymentType.Cash, 35.0);
        //assertEquals(35.0, order.getPaidAmount());

    }

   

}
