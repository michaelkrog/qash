package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import dk.apaq.shopsystem.model.Store;
import dk.apaq.shopsystem.service.Service;
import dk.apaq.shopsystem.model.Order;
import dk.apaq.shopsystem.model.OrderStatus;
import dk.apaq.shopsystem.model.Payment;
import dk.apaq.shopsystem.model.PaymentType;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;

/**
 *
 * @author michaelzachariassenkrog
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
public class PaymentCrudTest {

    public PaymentCrudTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("John", "Doe"));
    }

    @After
    public void tearDown() {
    }

    @Autowired
    private Service service;

    /*
    @Test
    public void testRead() {
        System.out.println("read");

        Crud.Editable<String, Store> shopcrud = service.getShopCrud();
        Store shop = shopcrud.read(shopcrud.create());
        Crud.Complete<String, Payment> crud = service.getPaymentCrud(shop);
        Payment result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result = crud.read(id);
        assertNotNull(result);

        crud.delete(id);

        result = crud.read(id);
        assertNull(result);
    }

    @Test
    public void testListIds() {
        System.out.println("listIds");
        Crud.Editable<String, Store> shopcrud = service.getShopCrud();
        Store shop = shopcrud.read(shopcrud.create());
        Crud.Complete<String, Payment> crud = service.getPaymentCrud(shop);

        for(int i=0;i<10;i++)
            crud.create();

        List<String> idlist = crud.listIds();
        assertTrue(idlist.size() >= 10);
    }

    @Test
    public void testCreate() {
        System.out.println("create");
        Crud.Editable<String, Store> shopcrud = service.getShopCrud();
        Store shop = shopcrud.read(shopcrud.create());
        Crud.Complete<String, Payment> crud = service.getPaymentCrud(shop);
        Payment result = crud.read(crud.create());
        assertNotNull(result);
    }

    @Test
    public void testUpdate() {
        System.out.println("update");
        Crud.Editable<String, Store> shopcrud = service.getShopCrud();
        Store shop = shopcrud.read(shopcrud.create());

        Crud.Complete<String, Order> orderCrud = service.getOrderCrud(shop);
        Order order = orderCrud.read(orderCrud.create());
        order.setCurrency("DKK");
        order.addOrderLine("Test", 1, 194.95, null);
        orderCrud.update(order);

        Crud.Complete<String, Payment> crud = service.getPaymentCrud(shop);
        Payment result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        Date now = new Date();
        result.setAmount(200);
        result.setPaymentType(PaymentType.Cash);
        
        try {
            crud.update(result);
            fail("Should fail because of missing order");
        } catch(Exception ex) {}

        result.setOrderId(order.getId());

        try {
            crud.update(result);
            fail("Should fail because order not accepted");
        } catch(Exception ex) {}

        order.setStatus(OrderStatus.Accepted);
        orderCrud.update(order);
        
        crud.update(result);

        result = crud.read(id);
        assertEquals(200, result.getAmount(), 0.03);
        assertEquals(PaymentType.Cash, result.getPaymentType());

        Payment change = crud.read(crud.create());
        change.setOrderId(order.getId());
        change.setAmount(-5.95);
        change.setPaymentType(PaymentType.Change);
        crud.update(change);
    }

    @Test
    public void testDelete() {
        System.out.println("delete");
        Crud.Editable<String, Store> shopcrud = service.getShopCrud();
        Store shop = shopcrud.read(shopcrud.create());
        Crud.Complete<String, Payment> crud = service.getPaymentCrud(shop);
        Payment result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result = crud.read(id);
        assertNotNull(result);

        crud.delete(id);

        result = crud.read(id);
        assertNull(result);
    }

    @Test
    public void testSecurity() {
        Crud.Editable<String, Store> shopcrud = service.getShopCrud();
        Store shop1 = shopcrud.read(shopcrud.create());
        Store shop2 = shopcrud.read(shopcrud.create());

        Crud.Complete<String, Order> orderCrud = service.getOrderCrud(shop1);
        Order order = orderCrud.read(orderCrud.create());
        order.setCurrency("DKK");
        order.addOrderLine("Test", 1, 194.95, null);
        order.setStatus(OrderStatus.Accepted);
        orderCrud.update(order);

        Crud.Complete<String, Order> orderCrud2 = service.getOrderCrud(shop2);
        Order order2 = orderCrud2.read(orderCrud2.create());
        order2.setCurrency("DKK");
        order2.addOrderLine("Test", 1, 194.95, null);
        order2.setStatus(OrderStatus.Accepted);
        orderCrud2.update(order2);

        Crud.Complete<String, Payment> crud1 = service.getPaymentCrud(shop1);
        Payment result1 = crud1.read(crud1.create());
        result1.setOrderId(order.getId());

        Crud.Complete<String, Payment> crud2 = service.getPaymentCrud(shop2);
        Payment result2 = crud2.read(crud2.create());
        result2.setOrderId(order2.getId());


        //Allowed
        crud1.read(result1.getId());
        crud2.update(result2);

        try {
            crud1.read(result2.getId());
            fail("Should not be allowed");
        } catch(SecurityException ex) { }


        try {
            crud2.update(result1);
            fail("Should not be allowed");
        } catch(SecurityException ex) { }

    }*/


}