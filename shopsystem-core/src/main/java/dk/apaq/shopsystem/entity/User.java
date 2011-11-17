package dk.apaq.shopsystem.entity;

import dk.apaq.crud.HasId;
import java.security.Principal;
import java.util.Date;
import java.util.List;

/**
 *
 */
public interface User extends Principal, HasId<String> {

    public List<String> getRoles();
    public String getDisplayName();

    public String getEmail();

    public String getPassword();

    public boolean isEmailVerified() ;

    public boolean isLocked();
    
    public String getTelephone();

}
