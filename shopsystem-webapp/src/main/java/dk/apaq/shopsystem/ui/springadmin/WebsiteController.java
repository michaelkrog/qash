package dk.apaq.shopsystem.ui.springadmin;

import dk.apaq.shopsystem.ui.springadmin.common.CommonGrid;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
//@RequestMapping(value = "/_admin")
public class WebsiteController extends AbstractController { 


    @RequestMapping(value = "/ajax/websites/list", method = RequestMethod.GET)
    public ModelAndView getWebsites() {
        
        //GetOrgService();
        //List list = orgService.getWebsites().list();
        
        //CommonGrid grid = new CommonGrid();
        
        ModelAndView modelAndView = new ModelAndView("admin/Grid");
        
        return modelAndView;
    }
   
}
