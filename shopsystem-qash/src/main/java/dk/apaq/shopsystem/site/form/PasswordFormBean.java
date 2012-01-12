package dk.apaq.shopsystem.site.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author krog
 */
public class PasswordFormBean {
    
    @NotNull
    private String oldPassword;
    
    @NotNull
    @Size(min = 6, max = 20)
    private String newPassword;
    
    @NotNull
    @Size(min = 6, max = 20)
    private String newPassword2;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    
    
}
