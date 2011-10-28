package dk.apaq.shopsystem.api;

import dk.apaq.filter.limit.Limit;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Tax;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class TaxController { 

    @Autowired
    private SystemService service;
    
    @RequestMapping(value = "/{orgInfo}/taxes", method = RequestMethod.GET)
    public @ResponseBody List<String> getTaxList(@PathVariable String orgInfo) {
        //currently the orginfo is just the orgid - should be more secure
        Organisation org = service.getOrganisationCrud().read(orgInfo);
        if(org==null) {
            //how to 404?
            return null;
        }
        
        OrganisationService orgService = service.getOrganisationService(org);
        Limit limit = new Limit(100); //Limit should be build frm requestparameters
        return orgService.getTaxes().listIds(limit);
    }
    
    @RequestMapping(value = "/{orgInfo}/taxes/{id}", method = RequestMethod.GET)
    public @ResponseBody Tax getTax(@PathVariable String orgInfo,@PathVariable String id) {
        //currently the orginfo is just the orgid - should be more secure
        Organisation org = service.getOrganisationCrud().read(orgInfo);
        if(org==null) {
            //how to 404?
            return null;
        }
        
        OrganisationService orgService = service.getOrganisationService(org);
        return orgService.getTaxes().read(id);
    }

}
