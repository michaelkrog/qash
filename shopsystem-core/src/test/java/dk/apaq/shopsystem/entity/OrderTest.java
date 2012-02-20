package dk.apaq.shopsystem.entity;

import java.util.Date;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;

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
    
    @Test
    public void testBeanPattern() {
        Order order = new Order();
        order.setBuyer(new ContactInformation("Apaq", null, null, null, null, null, null, null, null, null));
        order.setClerkName("clerk");
        order.setClerkUserId("clerkUserId");
        order.setCurrency("currency");
        order.setDateChanged(new Date());
        order.setDateCreated(new Date());
        order.setDateInvoiced(new Date());
        order.setDateTimelyPayment(new Date());
        order.setId("id");
        order.setInvoiceNumber(1);
        order.setNumber(1);
        order.setOrganisation(new Organisation());
        order.setOutlet(new Store());
        order.setPaid(true);
        order.setRecipient(new ContactInformation("Apaq", null, null, null, null, null, null, null, null, null));
        order.setStatus(OrderStatus.New);
        
        assertNotNull(order.getBuyer());
        assertNotNull(order.getClerkName());
        assertNotNull(order.getClerkUserId());
        assertNotNull(order.getCurrency());
        assertNotNull(order.getDateChanged());
        assertNotNull(order.getDateCreated());
        assertNotNull(order.getDateInvoiced());
        assertNotNull(order.getDateTimelyPayment());
        assertNotNull(order.getId());
        assertEquals(1, order.getInvoiceNumber());
        assertNotNull(order.getName());
        assertEquals(1, order.getNumber());
        assertNotNull(order.getOrganisation());
        assertNotNull(order.getOutlet());
        assertNotNull(order.getRecipient());
        assertNotNull(order.getShortDescription());
        assertNotNull(order.getStatus());
        
        
        
    }

   

}
