package dk.apaq.shopsystem.api;

import dk.apaq.filter.limit.Limit;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.security.SystemUserDetails;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class OrganisationController {
    
    @Autowired
    private SystemService service;
    
    private OrganisationCrud crud;
    @PostConstruct
    public void init() {
        this.crud = service.getOrganisationCrud();
    }
    
    @RequestMapping(value="/organisations", method= RequestMethod.GET)
    public @ResponseBody List getOrganisations(@RequestParam(value = "limit", required=false) Integer limit, 
                                                @RequestParam(value = "false", required=false) String forAll) {
        Limit l = ControllerUtil.validateAndCreateLimit(limit);
        if("true".equalsIgnoreCase(forAll)) {
            return crud.listIds(l);
        } else {
            SystemUserDetails details = (SystemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            SystemUser su = details.getUser();
            return crud.listIds(l, su);
        }
    }
    
    @RequestMapping(value = "/organisations/{id}", method = RequestMethod.GET)
    public @ResponseBody Organisation getOrganisation(@PathVariable String id) {
        Organisation org = crud.read(id);
        if(org==null) throw new ResourceNotFoundException("Organisation not found [id="+id+"]");
        return org;
    }
    
    @RequestMapping(value = "/organisations", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrganisation(@RequestBody Organisation organisation,
                                    HttpServletRequest request, HttpServletResponse response) {
        String id = service.getOrganisationCrud().create(organisation);
        String location = ControllerUtil.getLocationForChildResource(request, id);
        response.addHeader("Location", location);
    }
    
    @RequestMapping(value = "/organisations/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrganisation(@PathVariable String id, @RequestBody Organisation organisation,
                                    HttpServletRequest request, HttpServletResponse response) {
        //Should exist and be readable before updating
        getOrganisation(id);
        
        //force id upon organisation
        organisation.setId(id);
        
        //update
        service.getOrganisationCrud().update(organisation);
    }
}
