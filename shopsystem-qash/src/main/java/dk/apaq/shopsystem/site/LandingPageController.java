package dk.apaq.shopsystem.site;

import dk.apaq.shopsystem.site.form.QuoteFormBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author michael
 */
@Controller
public class LandingPageController {
    
    @Autowired
    MailSender mailSender;
    
    @Autowired
    SimpleMailMessage templateMessage;
    
    @RequestMapping("/index.htm")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        return new ModelAndView("landingpage");
    }
    
    @RequestMapping("/sendQuote.htm")
    public void sendQuote(@RequestParam String name, @RequestParam String email, @RequestParam String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        templateMessage.copyTo(mail);
        mail.setTo("contact@qashapp.com");
        mail.setSubject("Quote from Qash site [" + name + "]");
        mail.setText("From: " + name +"("+email+")\n\n" + mail);
        
        mailSender.send(mail); 
        
        //return "redirect:/index.htm";
    }
}
