package dk.apaq.shopsystem.ui.springadmin;

import dk.apaq.shopsystem.entity.Product;
import dk.apaq.filter.limit.Limit;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.ui.springadmin.common.CommonGrid;
import java.lang.annotation.Annotation;
import java.util.List;
import javax.servlet.jsp.PageContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class IndexController extends AbstractController { 

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public ModelAndView getIndex() {

        GetOrgService();
        ModelAndView modelAndView = new ModelAndView("LayoutView");
        modelAndView.addObject("organisationName", org.getName());
       
        // String[] args = { "Mr.", "X" };
        // E.g. message.code="Dear {0} {1}"
        // messageSource.getMessage("message.code", args, "en");

        
        //CommonGrid grid = new CommonGrid();

        return modelAndView;
    }
   
}
