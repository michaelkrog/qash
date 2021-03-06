package dk.apaq.shopsystem.site.form;

import com.google.gwt.i18n.client.CustomDateTimeFormat.Pattern;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author krog
 */
public class CreateAccountFormBean extends AccountFormBean {

    @NotNull
    @Size(min = 2, max = 30)
    private String companyName;
    @NotNull
    @Size(min = 6, max = 40)
    private String userName;
    @NotNull
    @Size(min = 6, max = 20)
    private String password;
    @NotNull
    @Size(min = 6, max = 20)
    private String password2;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
