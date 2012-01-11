package dk.apaq.shopsystem.site.form;

/**
 *
 * @author krog
 */
public class QuoteFormBean {
    private String fullName;
    private String emailAddress;
    private String message;

    public QuoteFormBean() {
    }

    public QuoteFormBean(String fullName, String emailAddress, String message) {
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.message = message;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getFullName() {
        return fullName;
    }

    public String getMessage() {
        return message;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
