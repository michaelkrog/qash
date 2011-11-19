package dk.apaq.shopsystem.ui.springadmin;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class UserController extends AbstractController { 


    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List getUsers() {

        List list = orgService.getUsers().list();

        //ModelAndView modelAndView = new ModelAndView("LayoutView");
        
        return list;
    }
   
}
