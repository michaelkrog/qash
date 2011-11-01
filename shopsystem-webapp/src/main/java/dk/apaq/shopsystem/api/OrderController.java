package dk.apaq.shopsystem.api;

import dk.apaq.shopsystem.entity.Order;
import dk.apaq.filter.limit.Limit;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class OrderController extends AbstractController { 


    @RequestMapping(value = "/{orgInfo}/orders", method = RequestMethod.GET)
    public @ResponseBody List<String> getOrderList(@PathVariable String orgInfo, @RequestParam(value = "limit", required=false) Integer limit) {
  
        orgService = GetOrgService(orgInfo);
        Limit l = new Limit(ValidateLimit(limit));
        return orgService.getOrders().listIds(l);
    }
    
    
    @RequestMapping(value = "/{orgInfo}/orders/{id}", method = RequestMethod.GET)
    public @ResponseBody Order getOrder(@PathVariable String orgInfo, @PathVariable String id) {
        
        orgService = GetOrgService(orgInfo);
        return orgService.getOrders().read(id);
    }

}
