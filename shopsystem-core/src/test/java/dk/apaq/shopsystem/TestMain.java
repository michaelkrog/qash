package dk.apaq.shopsystem;

import com.google.gson.Gson;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.entity.Tax;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.util.List;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 */
public class TestMain {

    @Test
    public void dummy() {

    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("defaultSpringContext.xml");
        SystemService service = applicationContext.getBean(SystemService.class);
        Organisation org;
        /*SystemUser user = new SystemUser();
        user.setName("test");
        user.setPassword("test");
        
        org = new Organisation();
        org.setCompanyName("test");
        
        org = service.createOrganisation(user, org);*/
        org = service.getOrganisationCrud().read("8add913b34ec10760134ec1097380000");
        OrganisationService orgService = service.getOrganisationService(org);
        long startTime;
        //prepareing with an order that has data we can seaerch for
        /*Order order = new Order();
        order.setClerkName("Boysen");
        order.addOrderLine("dims", 1, 20, new Tax("Moms", 25));
        orgService.getOrders().create(order);
            
        startTime = System.currentTimeMillis();
        
        for(int i=0;i<100000;i++) {
            order = new Order();
            for(int e=0;e<10;e++) {
                order.addOrderLine("dims", 1, 20, new Tax("Moms", 25));
            }
            orgService.getOrders().create(order);
            
            if(i >0 &&i%1000 == 0) {
                long time = System.currentTimeMillis() - startTime;
                System.out.println("Count: " + i + " ("+((double)time/i)+"ms per order.)");
            }
        }*/
        
        System.out.println("All orders created. Searching through them");
        orgService.getOrders().listIds();
        startTime = System.currentTimeMillis();
        //List<Order> orderlist = orgService.getOrders().list(new CompareFilter("clerkName", "Boysen", CompareFilter.CompareType.Equals), null);
        Order order = orgService.getOrders().read("8add913b34ec10760134ec1098130003");
        long time = System.currentTimeMillis() - startTime;
        System.out.println("Search took " + time +" ms .)");
        //System.out.println("Search took " + time +" ms and found "+orderlist.size()+" orders.)");
    }
}
