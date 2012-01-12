package dk.apaq.shopsystem.site;

import dk.apaq.shopsystem.api.ResourceNotFoundException;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.site.form.AccountFormBean;
import dk.apaq.shopsystem.site.form.PasswordFormBean;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
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

/**
 *
 * @author krog
 */
@Controller
@RequestMapping("/account_password.htm")
public class PasswordController {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordController.class);
    @Autowired
    private MailSender mailSender;
    @Autowired
    SimpleMailMessage templateMessage;
    @Autowired
    private SystemService service;
    @Autowired
    private Validator validator;
    @Resource(name = "authenticationManager")
    private AuthenticationManager am;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String handleRequest(Map model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SystemUser user = service.getSystemUser(username);
        
        if(user==null) {
            throw new ResourceNotFoundException("User not found.");
        }
        
        model.put("username", username);
        model.put("passwordInfo", new PasswordFormBean());
        return "password";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String persistAccount(@ModelAttribute("passwordInfo") @Valid PasswordFormBean passwordInfo, BindingResult result, Map model, HttpServletRequest request, HttpServletResponse response) {
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SystemUser orgUser = service.getSystemUser(username);
        if(orgUser==null) {
            throw new ResourceNotFoundException("Orginal user not found.");
        }
        
        if(passwordInfo.getOldPassword() == null || !passwordInfo.getOldPassword().equals(orgUser.getPassword())) {
            result.rejectValue("oldPassword", "account.input.passwordIncorrect", "Incorrect password");
        }
        
        if(orgUser.getPassword() == null || !orgUser.getPassword().equals(passwordInfo.getNewPassword())) {
            if (!passwordInfo.getNewPassword().equals(passwordInfo.getNewPassword2())) {
                result.rejectValue("newPassword2", "account.input.passwordsNotEqual", "The two password fields does not match.");
            }
        }

        
        if (!result.hasErrors()) {
            orgUser.setPassword(passwordInfo.getNewPassword());
            service.getSystemUserCrud().update(orgUser);
            
            authenticateUserAndSetSession(orgUser, request);
            return "redirect:/dashboard.htm";
        } else {
            model.put("username", orgUser.getName());
            return "password";
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
