package dk.apaq.shopsystem.entity;

import java.security.Principal;
import java.util.List;

/**
 *
 */
public interface User extends Principal {

    public List<String> getRoles();
    public String getDisplayname();

    public String getEmail();

    public String getPassword();

    public boolean isEmailVerified() ;

    public boolean isLocked();
}
