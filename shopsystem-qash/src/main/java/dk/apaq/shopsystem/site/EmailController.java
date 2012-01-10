package dk.apaq.shopsystem.site;

import dk.apaq.shopsystem.api.ResourceNotFoundException;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.security.SystemUserDetails;
import dk.apaq.shopsystem.service.SystemService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author krog
 */
@Controller
public class EmailController {

    @Autowired
    private MailSender mailSender;
    @Autowired
    SimpleMailMessage templateMessage;
    @Autowired
    private SystemService service;

    @RequestMapping("/verify_email_send.htm")
    public ModelAndView validateEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SystemUserDetails sud = (SystemUserDetails) auth.getPrincipal();

        String email = sud.getUser().getEmail();

        SimpleMailMessage msg = this.templateMessage == null ? new SimpleMailMessage() : new SimpleMailMessage(this.templateMessage);
        msg.setSubject("Verify email");
        msg.setTo(email);
        msg.setText(
                "Dear " + sud.getUser().getDisplayName()
                + ", thank you for using our service. \n\nTo verify your email please go to the following link:\n"
                + "http://qashapp.com/verify_email.htm?userId=" + sud.getUser().getId() + "\n\n"
                + "Best Regards\n"
                + "The Qash team.");
        this.mailSender.send(msg);


        Map<String, String> model = new HashMap<String, String>();
        model.put("title", "Email sent");
        model.put("message", "An email has been sent to your email address (" + email + ").");
        return new ModelAndView("message", model);
    }

    @RequestMapping("/verify_email.htm")
    public ModelAndView validateEmail(@RequestParam(required = true) String userId) {
        SystemUser user = service.getSystemUserCrud().read(userId);

        if (user == null) {
            throw new ResourceNotFoundException("User not found [id=" + userId + "]");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SystemUserDetails sud = (SystemUserDetails) auth.getPrincipal();
        if (!sud.getUser().getName().equals(user.getName())) {
            return new ModelAndView("redirect:/verify_email_no_auth.htm");
        }

        user.setEmailVerified(true);
        service.getSystemUserCrud().update(user);

        Map<String, String> model = new HashMap<String, String>();
        model.put("title", "Email verified");
        model.put("message", "Your email address has been verified.");
        return new ModelAndView("message", model);
    }

    @RequestMapping("/verify_email_no_auth.htm")
    public String invalidAuth() {
        return "verify_email_no_auth";
    }
}
