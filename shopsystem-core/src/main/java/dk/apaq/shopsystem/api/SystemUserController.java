package dk.apaq.shopsystem.api;

import dk.apaq.crud.Crud;
import dk.apaq.filter.limit.Limit;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.service.SystemService;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SystemUserController {
    
    @Autowired
    private SystemService service;
    
    private Crud.Complete<String, SystemUser> crud;
    @PostConstruct
    public void init() {
        this.crud = service.getSystemUserCrud();
    }
    
    @RequestMapping(value="/users", method= RequestMethod.GET)
    public @ResponseBody List getUsers(@RequestParam(required=false) Integer limit,
                                    @RequestParam(required=false) String full) {
        Limit l = ControllerUtil.validateAndCreateLimit(limit);
        if("true".equalsIgnoreCase(full)) {
            return crud.list(l);
        } else {
            return crud.listIds(l);
        }
    }
    
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public @ResponseBody SystemUser getUser(@PathVariable String id) {
        SystemUser user = crud.read(id);
        if(user==null) throw new ResourceNotFoundException("User not found [id="+id+"]");
        return user;
    }
    
    
}
