package dk.apaq.shopsystem.ui.springadmin;

import dk.apaq.shopsystem.entity.Product;
import dk.apaq.filter.limit.Limit;
import java.lang.annotation.Annotation;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class IndexController extends AbstractController { 


    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView getIndex() {

        ModelAndView modelAndView = new ModelAndView("LayoutView");
        modelAndView.addObject("settings", "Settings");
        
        return modelAndView;
    }
   
}
