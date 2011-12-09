package dk.apaq.shopsystem.api;

import dk.apaq.shopsystem.entity.Tax;
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


@Controller
public class TaxController extends AbstractController {

    @Autowired
    public TaxController(SystemService service) {
        super(service);
    }


    
    @RequestMapping(value = "/organisations/{orgInfo}/taxes", method = RequestMethod.GET)
    public @ResponseBody List getTaxList(@PathVariable String orgInfo, @RequestParam(value = "limit", required=false) Integer limit, @RequestParam(value = "full", required=false) String full) {
  
        OrganisationService orgService = getOrganisationService(orgInfo);
        Limit l = new Limit(ControllerUtil.validateLimit(limit));
        
        if ("true".equals(full)) {
            return orgService.getTaxes().list(l);
        }
        else {
            return orgService.getTaxes().listIds(l);
        }
    }
    
    
    @RequestMapping(value = "/organisations/{orgInfo}/taxes/{id}", method = RequestMethod.GET)
    public @ResponseBody Tax getTax(@PathVariable String orgInfo, @PathVariable String id) {
        
        OrganisationService orgService = getOrganisationService(orgInfo);
        return orgService.getTaxes().read(id);
    }

}
