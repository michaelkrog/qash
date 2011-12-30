package dk.apaq.shopsystem.site;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author krog
 */
@Controller
@RequestMapping("/login.htm")
public class LoginController {
    
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView handleRequest(@RequestParam(required=false) String redirect, HttpServletRequest request) throws IOException {
        
        if(redirect==null) {
            redirect = "/dashboard.htm";
        }
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        if(auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return new ModelAndView("redirect:" + redirect);
        }
        
        String spec = "/j_spring_rpx_security_check?redirect=" + URLEncoder.encode(redirect, "utf-8");
        URL tokenUrl = new URL(new URL(request.getRequestURL().toString()),spec);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("rxpReturnUrl", tokenUrl.toString());

        return new ModelAndView("login", model);
    }
}
