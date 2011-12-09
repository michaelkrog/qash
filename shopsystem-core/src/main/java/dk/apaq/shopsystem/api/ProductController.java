package dk.apaq.shopsystem.api;

import dk.apaq.shopsystem.entity.Product;
import dk.apaq.filter.limit.Limit;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

//extends AbstractController
@Controller
public class ProductController extends AbstractController {

    
    @Autowired 
    public ProductController(SystemService service) {
        super(service);
    }

    

    @RequestMapping(value = "/organisations/{orgInfo}/products", method = RequestMethod.GET)
    public @ResponseBody List getProductList(@PathVariable String orgInfo, @RequestParam(value = "limit", required=false) Integer limit, @RequestParam(value = "full", required=false) String full) {
  
        OrganisationService orgService = getOrganisationService(orgInfo);
        Limit l = new Limit(ControllerUtil.validateLimit(limit));
        
        if ("true".equals(full)) {
            return orgService.getProducts().list(l);
        }
        else {
            return orgService.getProducts().listIds(l);
        }
    }
    
    
    @RequestMapping(value = "/organisations/{orgInfo}/products/{id}", method = RequestMethod.GET)
    public @ResponseBody Product getProduct(@PathVariable String orgInfo, @PathVariable String id) {
        
        OrganisationService orgService = getOrganisationService(orgInfo);
        return orgService.getProducts().read(id);
    }
    
}
