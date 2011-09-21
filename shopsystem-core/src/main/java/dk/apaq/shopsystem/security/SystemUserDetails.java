package dk.apaq.shopsystem.security;

import dk.apaq.shopsystem.entity.SystemUser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author michael
 */
public class SystemUserDetails implements UserDetails{

    private SystemUser user;
    private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();


    public SystemUserDetails(SystemUser user) {
        if(user == null) {
            throw new NullPointerException("User was null");
        }
        
        this.user = user;

        for(String role:user.getRoles()){
            authorities.add(new GrantedAuthorityImpl(role));
        }

        authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
    }

    public SystemUser getUser() {
        return user;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
