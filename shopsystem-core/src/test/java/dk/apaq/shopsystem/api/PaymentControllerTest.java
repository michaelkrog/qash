package dk.apaq.shopsystem.api;

import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;
import dk.apaq.shopsystem.service.SystemService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
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
    public void testPaymentViaQuickPay() throws Exception {
        
        Organisation org = new Organisation();
        org.setMerchantId("12345");
        org.setMerchantSecret("54321");
        org = service.getOrganisationCrud().createAndRead(org);
        
        OrganisationService organisationService = service.getOrganisationService(org);
        String orderId = organisationService.getOrders().create();
        Order order = organisationService.getOrders().read(orderId);
        order.addOrderLine("Test", 1, new BigDecimal(123), null);
        order.setStatus(OrderStatus.Processing);
        order = organisationService.getOrders().update(order);
        
        String orderNumber = "000" + order.getNumber();
        String amount = Long.toString(order.getTotalWithTax().getAmountMinorLong());
        
        String[] keys = PaymentController.QUICKPAY_KEYS;
        String[] values = {"authorize", orderNumber, amount, order.getCurrency(), "120202134442", "9", "000", "OK", 
                        "000", "OK", org.getMerchantId(), "john@doe.dk", "qwerty", "VISA", "XXXX XXXX XXXX 1234", "1602",
                        "1", "low", "", "", "0"};
        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<keys.length;i++) {
            request.addParameter(keys[i], values[i]);
            builder.append(values[i]);
        }
       
        builder.append(org.getMerchantSecret());
        request.addParameter("md5check", DigestUtils.md5Hex(builder.toString()));
        
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        controller.onQuickpayEvent(request, org.getId());
        order = organisationService.getOrders().read(orderId);
        
        assertTrue(order.isPaid());
        assertEquals(OrderStatus.Completed, order.getStatus());
    }
    /*
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
