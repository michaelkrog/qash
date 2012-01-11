package dk.apaq.shopsystem.site;

import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.core.LikeFilter;
import dk.apaq.shopsystem.api.ResourceNotFoundException;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.security.SystemUserDetails;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.site.form.AccountFormBean;
import dk.apaq.shopsystem.site.form.CreateAccountFormBean;
import java.util.HashMap;
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
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author krog
 */
@Controller
@RequestMapping("/account.htm")
public class AccountController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);
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
    public ModelAndView handleRequest() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SystemUser user = service.getSystemUser(username);
        
        if(user==null) {
            throw new ResourceNotFoundException("User not found.");
        }
        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("username", username);
        model.put("accountInfo", new AccountFormBean(user));
        return new ModelAndView("account", model);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView persistAccount(@ModelAttribute @Valid AccountFormBean accountInfo, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SystemUser orgUser = service.getSystemUser(username);
        if(orgUser==null) {
            throw new ResourceNotFoundException("Orginal user not found.");
        }
        
        if(orgUser.getEmail() == null || !orgUser.getEmail().equals(accountInfo.getEmail())) {
            if (!accountInfo.getEmail2().equals(accountInfo.getEmail())) {
                result.rejectValue("email2", "1", "The two email fields does not match.");
            }
        }

        
        if (!result.hasErrors()) {
            orgUser.setDisplayName(accountInfo.getDisplayName());
            orgUser.setEmail(accountInfo.getEmail());
            service.getSystemUserCrud().update(orgUser);
            
            if (mailSender != null) {
                SimpleMailMessage msg = this.templateMessage == null ? new SimpleMailMessage() : new SimpleMailMessage(this.templateMessage);
                msg.setSubject("New account");
                msg.setTo(accountInfo.getEmail());
                msg.setText(
                        "Dear " + accountInfo.getDisplayName()
                        + "\n\nYour account information has been changed.\n\n"
                        + "Best Regards\n"
                        + "The Qash team.");
                try {
                    this.mailSender.send(msg);
                } catch (MailException ex) {
                    // simply log it and go on...
                    LOG.error("Unable to send mail.", ex);
                }
            }

            authenticateUserAndSetSession(orgUser, request);
            return new ModelAndView("redirect:/dashboard.htm");
        } else {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("username", username);
            model.put("accountInfo", accountInfo);
            return new ModelAndView("account", model);
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
