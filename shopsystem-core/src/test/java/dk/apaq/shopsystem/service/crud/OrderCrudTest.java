package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.crud.CrudNotifier;
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.entity.ContactInformation;
import dk.apaq.shopsystem.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.PaymentType;
import dk.apaq.shopsystem.entity.Store;
import java.util.Date;
import java.util.List;
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
public class OrderCrudTest {

    public OrderCrudTest() {
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

        Crud.Editable<String, Organisation> orgcrud = service.getOrganisationCrud();
        Organisation organisation = orgcrud.read(orgcrud.create());
        Crud.Complete<String, Order> crud = service.getOrganisationService(organisation).getOrders();
        Order result = crud.read(crud.create());

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
    public void testCreate() {
        System.out.println("create");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation organisation = orgcrud.read(orgcrud.create());
        Crud.Complete<String, Order> crud = service.getOrganisationService(organisation).getOrders();
        Order item = crud.read(crud.create());

        assertNotNull(item);
    }

    @Test
    public void testUpdate() {
        System.out.println("update");
        Crud.Editable<String, Organisation> orgcrud = service.getOrganisationCrud();
        Organisation organisation = orgcrud.read(orgcrud.create());
        Crud.Complete<String, Order> crud = service.getOrganisationService(organisation).getOrders();
        Order result = crud.read(crud.create());

        ContactInformation ci = new ContactInformation();
        ci.setContactName("Kaj");
        result.setBuyer(ci);

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        Payment payment = new Payment();
        payment.setAmount(100);
        payment.setPaymentType(PaymentType.Cash);
        result.setCurrency("DKK");
        crud.update(result);

        result = crud.read(id);
        assertEquals(OrderStatus.Processing, result.getStatus());
        assertEquals("DKK", result.getCurrency());
        assertEquals("Kaj", result.getBuyer().getContactName());        

        result.setStatus(OrderStatus.Accepted);
        crud.update(result);

        result = crud.read(id);
        assertEquals(OrderStatus.Accepted, result.getStatus());

        result.setStatus(OrderStatus.New);
        try {
            crud.update(result);
            fail("Souhld not be able to change status back to new.");
        } catch(Exception ex) {}
    }

    @Test
    public void testDelete() {
        System.out.println("delete");
        Crud.Editable<String, Organisation> orgcrud = service.getOrganisationCrud();
        Organisation organisation = orgcrud.read(orgcrud.create());
        Crud.Complete<String, Order> crud = service.getOrganisationService(organisation).getOrders();
        Order result = crud.read(crud.create());

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
    public void testNotification() {
        System.out.println("delete");
        MockCrudListener<String, Organisation> listener = new MockCrudListener<String, Organisation>();
        Crud.Editable<String, Organisation> orgcrud = service.getOrganisationCrud();
        Organisation organisation = orgcrud.read(orgcrud.create());
        Crud.Complete<String, Order> crud = service.getOrganisationService(organisation).getOrders();
        ((CrudNotifier)crud).addListener(listener);
        Order result = crud.read(crud.create());

        assertTrue(listener.isCreateCalled());

        String id = result.getId();
        result = crud.read(id);
        assertTrue(listener.isReadCalled());

        result.setNumber(132);
        crud.update(result);
        assertTrue(listener.isUpdateCalled());

        crud.delete(id);
        assertTrue(listener.isDeleteCalled());
    }

    @Test
    public void testListIds() {
        System.out.println("listIds");
        Crud.Editable<String, Organisation> orgcrud = service.getOrganisationCrud();
        Organisation organisation = orgcrud.read(orgcrud.create());
        Crud.Editable<String, Order> crud = service.getOrganisationService(organisation).getOrders();

        for(int i=0;i<10;i++)
            crud.create();

        List<String> idlist = crud.listIds();
        assertTrue(idlist.size() >= 10);
    }

    @Test
    public void testListIdsFiltered() {
        System.out.println("listIds");
        Date longAgo = new Date(new Date().getTime()-1000000000);
        Filter dateFilter = new CompareFilter("dateCreated", longAgo, CompareFilter.CompareType.GreaterOrEqual);

        Crud.Editable<String, Organisation> orgcrud = service.getOrganisationCrud();
        Organisation organisation = orgcrud.read(orgcrud.create());
        Crud.Complete<String, Order> crud = service.getOrganisationService(organisation).getOrders();

        for(int i=0;i<10;i++)
            crud.create();

        List<String> idlist = crud.listIds(dateFilter, null);
        assertTrue(idlist.size() >= 10);
    }

    @Test
    public void testSecurity() {
        Crud.Editable<String, Organisation> orgcrud = service.getOrganisationCrud();
        Organisation org1 = orgcrud.read(orgcrud.create());
        Organisation org2 = orgcrud.read(orgcrud.create());

        Crud.Editable<String, Order> orderCrud1 = service.getOrganisationService(org1).getOrders();
        Crud.Editable<String, Order> orderCrud2 = service.getOrganisationService(org2).getOrders();

        Order order1 = orderCrud1.read(orderCrud1.create());
        Order order2 = orderCrud2.read(orderCrud2.create());

        //Allowed
        orderCrud1.read(order1.getId());
        orderCrud2.update(order2);

        try {
            orderCrud1.read(order2.getId());
            fail("Should not be allowed");
        } catch(SecurityException ex) { }


        try {
            orderCrud2.update(order1);
            fail("Should not be allowed");
        } catch(SecurityException ex) { }

    }

}
