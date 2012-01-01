package dk.apaq.shopsystem.api;

import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;
import dk.apaq.shopsystem.service.SystemService;
import java.io.ByteArrayOutputStream;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.io.OutputStream;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;

/**
 *
 * @author michael
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
@Transactional
public class PaymentControllerTest {
    
    public PaymentControllerTest() {
        
    }

    @Autowired
    PaymentController controller;
    
    @Autowired
    private SystemService service;
    
    @PersistenceContext
    EntityManager em;
    
    @Test
    public void testPaymentViaWebhook() { 
    }
    
    /*
    @Test
    public void testPaymentViaWebhook() {
        OrganisationCrud crud = service.getOrganisationCrud();
        Organisation org = crud.read(crud.create());
        
        OrganisationService organisationService = service.getOrganisationService(org);
        String orderId = organisationService.getOrders().create();
        Order order = organisationService.getOrders().read(orderId);
        order.addOrderLine("Test", 1, 123, null);
        order.setStatus(OrderStatus.Processing);
        organisationService.getOrders().update(order);
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("orgid", org.getId());
        request.addParameter("orderid", order.getId());
        request.addParameter("amount", Double.toString(order.getTotalWithTax()));
        request.addParameter("currency", order.getCurrency());
        
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        controller.onGatewayEvent(request, response);
        order = organisationService.getOrders().read(orderId);
        
        assertTrue(order.isPaid());
        assertEquals(OrderStatus.Completed, order.getStatus());
    }
    
    @Test
    public void testPaymentViaWebhook_2payments() {
        OrganisationCrud crud = service.getOrganisationCrud();
        Organisation org = crud.read(crud.create());
        
        OrganisationService organisationService = service.getOrganisationService(org);
        String orderId = organisationService.getOrders().create();
        Order order = organisationService.getOrders().read(orderId);
        order.addOrderLine("Test", 1, 123, null);
        order.setStatus(OrderStatus.Processing);
        organisationService.getOrders().update(order);
        
        //First request
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("orgid", org.getId());
        request.addParameter("orderid", order.getId());
        request.addParameter("amount", Double.toString(order.getTotalWithTax() / 2));
        request.addParameter("currency", order.getCurrency());
        
        MockHttpServletResponse response = new MockHttpServletResponse();
        controller.onGatewayEvent(request, response);
        
        order = organisationService.getOrders().read(orderId);
        
        assertFalse(order.isPaid());
        assertEquals(OrderStatus.Accepted, order.getStatus());
        
        //Second request
        request = new MockHttpServletRequest();
        request.addParameter("orgid", org.getId());
        request.addParameter("orderid", order.getId());
        request.addParameter("amount", Double.toString(order.getTotalWithTax() / 2));
        request.addParameter("currency", order.getCurrency());
        
        response = new MockHttpServletResponse();
        controller.onGatewayEvent(request, response);
        
        order = organisationService.getOrders().read(orderId);
        
        assertTrue(order.isPaid());
        assertEquals(OrderStatus.Completed, order.getStatus());
    }*/
}
