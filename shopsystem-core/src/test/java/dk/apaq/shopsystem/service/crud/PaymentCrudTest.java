package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import dk.apaq.shopsystem.entity.Store;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.PaymentType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
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
    private SystemService service;

    
    @Test
    public void testRead() {
        System.out.println("read");

        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Complete<String, Payment> crud = service.getOrganisationService(org).getPayments();
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
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Complete<String, Payment> crud = service.getOrganisationService(org).getPayments();

        for(int i=0;i<10;i++)
            crud.create();

        List<String> idlist = crud.listIds();
        assertTrue(idlist.size() >= 10);
    }

    @Test
    public void testCreate() {
        System.out.println("create");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Complete<String, Payment> crud = service.getOrganisationService(org).getPayments();
        Payment result = crud.read(crud.create());
        assertNotNull(result);
    }

    @Test
    public void testUpdate() {
        System.out.println("update");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());

        Crud.Complete<String, Order> orderCrud = service.getOrganisationService(org).getOrders();
        Order order = orderCrud.read(orderCrud.create());
        order.setCurrency("DKK");
        order.addOrderLine("Test", 1, new BigDecimal(19495), null);
        orderCrud.update(order);

        Crud.Complete<String, Payment> crud = service.getOrganisationService(org).getPayments();
        Payment result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        Date now = new Date();
        result.setAmount(Money.of(CurrencyUnit.of("DKK"), 200));
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
        assertEquals(20000, result.getAmount().getAmountMinorLong());
        assertEquals(PaymentType.Cash, result.getPaymentType());

        /*Payment change = crud.read(crud.create());
        change.setOrderId(order.getId());
        change.setAmount(Money.of(CurrencyUnit.of("DKK"), -5.95));
        change.setPaymentType(PaymentType.Change);
        crud.update(change);*/
    }

    @Test
    public void testDelete() {
        System.out.println("delete");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Complete<String, Payment> crud = service.getOrganisationService(org).getPayments();
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
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org1 = orgcrud.read(orgcrud.create());
        Organisation org2 = orgcrud.read(orgcrud.create());

        Crud.Complete<String, Order> orderCrud = service.getOrganisationService(org1).getOrders();
        Order order = orderCrud.read(orderCrud.create());
        order.setCurrency("DKK");
        order.addOrderLine("Test", 1, new BigDecimal(19495), null);
        order.setStatus(OrderStatus.Accepted);
        orderCrud.update(order);

        Crud.Complete<String, Order> orderCrud2 = service.getOrganisationService(org2).getOrders();
        Order order2 = orderCrud2.read(orderCrud2.create());
        order2.setCurrency("DKK");
        order2.addOrderLine("Test", 1, new BigDecimal(19495), null);
        order2.setStatus(OrderStatus.Accepted);
        orderCrud2.update(order2);

        Crud.Complete<String, Payment> crud1 = service.getOrganisationService(org1).getPayments();
        Payment result1 = crud1.createAndRead(new Payment("DKK", 0));
        result1.setOrderId(order.getId());

        Crud.Complete<String, Payment> crud2 = service.getOrganisationService(org2).getPayments();
        Payment result2 = crud2.createAndRead(new Payment("DKK", 0));
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

    }


}