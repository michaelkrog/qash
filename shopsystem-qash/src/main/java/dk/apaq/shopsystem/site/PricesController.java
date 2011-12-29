package dk.apaq.shopsystem.site;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author michael
 */
@Controller
public class PricesController {
    
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        return new ModelAndView("landingpage");
    }
}
