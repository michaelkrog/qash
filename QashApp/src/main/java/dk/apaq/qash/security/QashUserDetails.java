package dk.apaq.qash.security;

import dk.apaq.qash.share.model.Account;
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
public class QashUserDetails implements UserDetails{

    private Account account;
    private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();


    public QashUserDetails(Account account) {
        assert account!=null;
        this.account = account;

        for(String role:account.getRoles()){
            authorities.add(new GrantedAuthorityImpl(role));
        }

        authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        
        return authorities;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !account.isLocked();
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
