package dk.apaq.shopsystem.site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author michael
 */
@Controller
public class PolicyController {
    
    @RequestMapping("/policy.htm")
    public String handleRequest() {
        return "policy";
    }
}
