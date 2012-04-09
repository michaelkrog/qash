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
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;

/**
 *
 * @author michael
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
@Transactional
public class DaybookControllerTest {
    
    public DaybookControllerTest() {
        
    }

    @Autowired
    DaybookController controller;
    
    @Autowired
    private SystemService service;
    
    @PersistenceContext
    EntityManager em;
    
    /**
     * Test of getPaymentList method, of class DaybookController.
     */
    @Test
    public void testGetPaymentList() {
        System.out.println("getPaymentList");
        
        OrganisationCrud crud = service.getOrganisationCrud();
        Organisation org = crud.read(crud.create());
        
        OrganisationService organisationService = service.getOrganisationService(org);
        String orderId = organisationService.getOrders().create();
        Order order = organisationService.getOrders().read(orderId);
        order.addOrderLine("Test", 1, new BigDecimal(123), null);
        order.setStatus(OrderStatus.Processing);
        organisationService.getOrders().update(order);
        
        em.clear();
        
        order = organisationService.getOrders().read(orderId);
        order.setStatus(OrderStatus.Accepted);
        organisationService.getOrders().update(order);
        
        String orgInfo = org.getId();
        Date from = new Date();
        from.setMonth(from.getMonth()-1);
        Date to = new Date();
        
        int account = 1;
        int offsetAccount = 2;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        controller.getDaybook(orgInfo, from, to, account, offsetAccount, out);
        
        String result = new String(out.toByteArray());
        assertTrue(result.contains(order.getOrderLine(0).getTitle()));
        
    }
}
