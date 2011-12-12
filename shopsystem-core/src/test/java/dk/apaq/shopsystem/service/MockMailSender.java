package dk.apaq.shopsystem.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 *
 * @author michael
 */
public class MockMailSender implements MailSender {

    boolean mailSend;
    
    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        mailSend = true;
    }

    @Override
    public void send(SimpleMailMessage[] simpleMessages) throws MailException {
        mailSend = true;
    }

    public boolean isMailSend() {
        return mailSend;
    }
    
    public void reset() {
        mailSend = false;
    }
    
}
