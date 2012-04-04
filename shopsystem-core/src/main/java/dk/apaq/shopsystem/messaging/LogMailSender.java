package dk.apaq.shopsystem.messaging;

import com.amazonaws.services.simpleemail.AWSJavaMailTransport;
import java.io.IOException;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * A simple mailsender for logging the mails send.
 * Practical for development purposes.
 * @author krog
 */
public class LogMailSender extends JavaMailSenderImpl {

    private static final Logger LOG = LoggerFactory.getLogger(LogMailSender.class);
    public static final String MAIL_TRANSPORT_PROTOCOL_KEY = "mail.transport.protocol";
    
    
    public class LogMailSenderTransport extends Transport {

        public LogMailSenderTransport(Session session, URLName urlname) {
            super(session, urlname);
        }

        @Override
        protected boolean protocolConnect(String host, int port, String user, String password) throws MessagingException {
            super.setConnected(true);
            return true;
        }

        
        @Override
        public void sendMessage(Message msg, Address[] adrss) throws MessagingException {
            for(Address adr : adrss) {
                try {
                    LOG.info("Mail send -------------------------\nRecipient: {}\nSender: {}\nText: {}\n-------------------", 
                            new Object[]{adr, msg.getFrom()[0], msg.getContent().toString()});
                } catch (IOException ex) {
                    LOG.error("Unable to send mail.", ex);
                } catch (MessagingException ex) {
                    LOG.error("Unable to send mail.", ex);
                }
            }
            
        }
        
    }

    @PostConstruct
    public void init() {
        Properties props = getJavaMailProperties();
        props.setProperty(MAIL_TRANSPORT_PROTOCOL_KEY, "log");
        setPort(-1);
    }
     
    @Override
    protected Transport getTransport(Session session) throws NoSuchProviderException {
        return new LogMailSenderTransport(session, null);
    }
    
}
