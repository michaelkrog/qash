package dk.apaq.shopsystem.api;

import dk.apaq.shopsystem.entity.Order;
import dk.apaq.filter.limit.Limit;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

/**
 * A controller for retrieveing Orders via the Rest API.
 */
@Controller
public class OrderController extends AbstractController { 


    /**
     * Retrieves an orderlist.
     */
    @RequestMapping(value = "/{orgInfo}/orders", method = RequestMethod.GET)
    public @ResponseBody List getOrderList(@PathVariable String orgInfo, @RequestParam(value = "limit", required=false) Integer limit, @RequestParam(value = "full", required=false) String full) {
  
        orgService = getOrganisationService(orgInfo);
        Limit l = new Limit(validateLimit(limit));
        
        if ("true".equals(full)) {
            return orgService.getProducts().list(l);
        }
        else {
            return orgService.getProducts().listIds(l);
        }
        
    }
    
    /**
     * Retrieves an specific order.
     */
    @RequestMapping(value = "/{orgInfo}/orders/{id}", method = RequestMethod.GET)
    public @ResponseBody Order getOrder(@PathVariable String orgInfo, @PathVariable String id) {
        
        orgService = getOrganisationService(orgInfo);
        return orgService.getOrders().read(id);
    }

}
