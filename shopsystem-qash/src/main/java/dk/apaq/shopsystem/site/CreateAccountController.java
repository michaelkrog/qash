package dk.apaq.shopsystem.site;

import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.site.form.AccountInfo;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author krog
 */
@Controller
@RequestMapping("/createAccount.htm")
public class CreateAccountController {

    @Autowired
    private SystemService service;
    
    @Autowired
    private Validator validator;
    
    @Resource(name="authenticationManager")
    private AuthenticationManager am;

    @InitBinder
    public void initBinder(WebDataBinder binder) {  
        binder.setValidator(validator);
    }
    
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView handleRequest() {
        return new ModelAndView("create_account", "accountInfo", new AccountInfo());
    }

    @RequestMapping(method=RequestMethod.POST)
    public String persistAccount(@ModelAttribute @Valid AccountInfo accountInfo, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        if (!accountInfo.getEmail2().equals(accountInfo.getEmail())) {
            result.rejectValue("email2", "1", "The two email fields does not match.");
        }

        if (!accountInfo.getPassword2().equals(accountInfo.getPassword())) {
            result.rejectValue("password2", "1", "The two password fields does not match.");
        }

        if (!result.hasErrors()) {
            Organisation org = new Organisation();
            org.setCompanyName(accountInfo.getCompanyName());

            SystemUser account = new SystemUser();
            account.setName(accountInfo.getUserName());
            account.setDisplayName(accountInfo.getDisplayName());
            account.setEmail(accountInfo.getEmail());
            account.setPassword(accountInfo.getEmail2());

            org = service.createOrganisation(account, org);

            authenticateUserAndSetSession(account, request);
            return "redirect:/dashboard.htm";
        } else {
            return "create_account";
        }

    }

    private void authenticateUserAndSetSession(SystemUser account, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(account.getName(), account.getPassword());

        // generate session if one doesn't exist
        request.getSession();

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = am.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }
}
