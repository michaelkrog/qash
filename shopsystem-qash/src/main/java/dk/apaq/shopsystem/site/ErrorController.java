package dk.apaq.shopsystem.site;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author krog
 */
@Controller
public class ErrorController {
    
    @RequestMapping("/error.htm")
    public ModelAndView handleRequest(){
        Map<String, String> model = new HashMap<String, String>();
        model.put("title", "Page not found");
        model.put("message", "Hmm... The page could not be found.");
        return new ModelAndView("message", model);
    }
}
