package dk.apaq.shopsystem.site;

import dk.apaq.shopsystem.api.ResourceNotFoundException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author krog
 */
@Controller
public class RedirectController {

    @RequestMapping("/login.jsp")
    public View handleLoginPageRedirect(HttpServletRequest request) {
        return getRedirectViewFor("login.htm");
    }
    
    @RequestMapping("/policy.jsp")
    public View handlePolicyPageRedirect() {
        return getRedirectViewFor("policy.htm");
    }
    
    @RequestMapping("/terms.jsp")
    public View handleTermsPageRedirect() {
        return getRedirectViewFor("terms.htm");
    }
    
    @RequestMapping("/shoplist.jsp")
    public View handleDashboardPageRedirect() {
        return getRedirectViewFor("dashboard.htm");
    }
    
    @RequestMapping("/tour.jsp")
    public View handleTourPageRedirect() {
        return getRedirectViewFor("tour.htm");
    }
    
    @RequestMapping("/account.jsp")
    public View handleAccountPageRedirect() {
        return getRedirectViewFor("account.htm");
    }
    
    @RequestMapping("/contact.jsp")
    public View handleContactPageRedirect() {
        return getRedirectViewFor("contact.htm");
    }
    
    private RedirectView getRedirectViewFor(String path) {
        RedirectView view = new RedirectView(path);
        view.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        return view;
    }
}
