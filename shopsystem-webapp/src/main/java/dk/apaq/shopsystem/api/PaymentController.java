package dk.apaq.shopsystem.api;

import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.filter.limit.Limit;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class PaymentController extends AbstractController { 


    @RequestMapping(value = "/{orgInfo}/payments", method = RequestMethod.GET)
    public @ResponseBody List getPaymentList(@PathVariable String orgInfo, @RequestParam(value = "limit", required=false) Integer limit, @RequestParam(value = "full", required=false) String full) {
  
        orgService = GetOrgService(orgInfo);
        Limit l = new Limit(ValidateLimit(limit));
        
        if ("true".equals(full)) {
            return orgService.getProducts().list(l);
        }
        else {
            return orgService.getProducts().listIds(l);
        }
    }
    
    
    @RequestMapping(value = "/{orgInfo}/payments/{id}", method = RequestMethod.GET)
    public @ResponseBody Payment getPayment(@PathVariable String orgInfo, @PathVariable String id) {
        
        orgService = GetOrgService(orgInfo);
        return orgService.getPayments().read(id);
    }

}