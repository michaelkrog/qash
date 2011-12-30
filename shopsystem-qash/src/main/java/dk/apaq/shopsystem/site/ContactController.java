package dk.apaq.shopsystem.site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author krog
 */
@Controller
public class ContactController {
    
    @RequestMapping("/contact.htm")
    public String handleRequest(){
        
        return "contact";
    }
}
