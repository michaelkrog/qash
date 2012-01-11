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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
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
        
        return new ModelAndView("landingpage", "quote", new QuoteFormBean());
    }
    
    @RequestMapping("/sendQuote.htm")
    public String sendQuote(@ModelAttribute QuoteFormBean quote) {
        SimpleMailMessage message = new SimpleMailMessage();
        templateMessage.copyTo(message);
        message.setText(quote.getMessage());
        message.setTo("contact@qashapp.com");
        message.setSubject("Quote from Qash site [" + quote.getFullName() + "]");
        message.setText("From: " + quote.getFullName() +"("+quote.getEmailAddress()+")\n\n" + quote.getMessage());
        
        mailSender.send(message);
        
        return "redirect:/index.htm";
    }
}
