package dk.apaq.shopsystem.site.form;

import com.google.gwt.i18n.client.CustomDateTimeFormat.Pattern;
import dk.apaq.shopsystem.entity.SystemUser;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 *
 * @author krog
 */
public class AccountFormBean {
    
    
    @NotNull @Size(min=3, max=40)
    private String displayName;
    

    @NotNull @Pattern("^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\\\-]+\\.)+[a-zA-Z]{2,4}$")
    private String email;
    
    @NotNull @Pattern("^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\\\-]+\\.)+[a-zA-Z]{2,4}$")
    private String email2;

    public AccountFormBean() {
    }

    public AccountFormBean(SystemUser user) {
        this.displayName = user.getDisplayName();
        this.email = user.getEmail();
        this.email2 = user.getEmail();
    }
    
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }


    
    
}
