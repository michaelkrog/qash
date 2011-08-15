package dk.apaq.qash.security;

import java.util.Collection;

import dk.apaq.qash.share.model.Account;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.AuthenticationCancelledException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 *
 * @author michael
 */
public class QashAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsManager userDetailsManager;

    public QashAuthenticationProvider() {
    }

    public void setUserDetailsManager(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    public UserDetailsManager getUserDetailsManager() {
        return userDetailsManager;
    }


    /* (non-Javadoc)
     * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.Authentication)
     */
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

        if (!supports(authentication.getClass())) {
            return null;
        }

        //We only support requests with WebAuthenticationDetails
        if (!(authentication.getDetails() instanceof WebAuthenticationDetails)) {
            return null;
        }

        UserDetails userDetails = null;
        Authentication newAuthentication = null;
        String ambassadorFor = null;
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();

        
        DaoAuthenticationProvider d;

        //Is it formbased?
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken response =
                    (UsernamePasswordAuthenticationToken) authentication;

            userDetails =
                    userDetailsManager.loadUserByUsername(response.getName());
            if (!userDetails.getPassword().equals(response.getCredentials())) {
                throw new BadCredentialsException("Wrong password supplied.");
            }
        }

        //Is it openid?
        if (authentication instanceof OpenIDAuthenticationToken) {
            OpenIDAuthenticationToken response = (OpenIDAuthenticationToken) authentication;
            OpenIDAuthenticationStatus status = response.getStatus();

            // handle the various possibilities
            if (status == OpenIDAuthenticationStatus.SUCCESS) {
                // Lookup user details
                String identifier = response.getIdentityUrl();
                try {

                    userDetails = userDetailsManager.loadUserByUsername(
                            response.getIdentityUrl());
                } catch (UsernameNotFoundException ex) {
                    userDetails = userDetailsFromOpenIdResponse(response);
                    userDetailsManager.createUser(userDetails);
                }


            } else if (status == OpenIDAuthenticationStatus.CANCELLED) {
                throw new AuthenticationCancelledException("Log in cancelled");
            } else if (status == OpenIDAuthenticationStatus.ERROR) {
                throw new AuthenticationServiceException("Error message from server: " + response.getMessage());
            } else if (status == OpenIDAuthenticationStatus.FAILURE) {
                throw new BadCredentialsException("Log in failed - identity could not be verified");
            } else if (status == OpenIDAuthenticationStatus.SETUP_NEEDED) {
                throw new AuthenticationServiceException(
                        "The server responded setup was needed, which shouldn't happen");
            } else {
                throw new AuthenticationServiceException("Unrecognized return value " + status.toString());
            }
        }

        newAuthentication = createSuccessfulAuthentication(userDetails, authentication);

        return newAuthentication;
    }

    /**
     * Handles the creation of the final <tt>Authentication</tt> object which will be returned by the provider.
     * <p>
     * The default implementation just creates a new OpenIDAuthenticationToken from the original, but with the
     * UserDetails as the principal and including the authorities loaded by the UserDetailsService.
     *
     * @param userDetails the loaded UserDetails object
     * @param auth the token passed to the authenticate method, containing
     * @return the token which will represent the authenticated user.
     */
    protected Authentication createSuccessfulAuthentication(UserDetails userDetails, OpenIDAuthenticationToken auth) {
        return new OpenIDAuthenticationToken(userDetails, userDetails.getAuthorities(),
                auth.getIdentityUrl(), auth.getAttributes());
    }

    protected Authentication createSuccessfulAuthentication(UserDetails userDetails, UsernamePasswordAuthenticationToken auth) {
        UsernamePasswordAuthenticationToken successAuth =
                new UsernamePasswordAuthenticationToken(
                    userDetails, auth.getCredentials(), userDetails.getAuthorities());
        successAuth.setDetails(auth.getDetails());
        return successAuth;
    }

    protected Authentication createSuccessfulAuthentication(UserDetails userDetails, Authentication auth) {
        if(auth instanceof OpenIDAuthenticationToken)
            return createSuccessfulAuthentication(userDetails, (OpenIDAuthenticationToken)auth);
        if(auth instanceof UsernamePasswordAuthenticationToken)
            return createSuccessfulAuthentication(userDetails, (UsernamePasswordAuthenticationToken)auth);
        throw new IllegalArgumentException();
    }

    /*private Account createAccount(String identifier, String name, String username, String email) {
        Account account;

        account = new Account();
        account.setEmail(email);
        account.setDisplayname(name == null ? email : name);
        account.setName(username);
        account.getIdentifiers().add(identifier);
        account = shopDao.persistAccount(account, account);
        return account;
    }*/

    
    /* (non-Javadoc)
     * @see org.springframework.security.authentication.AuthenticationProvider#supports(java.lang.Class)
     */
    public boolean supports(Class<? extends Object> authentication) {
        return OpenIDAuthenticationToken.class.isAssignableFrom(authentication)
                || UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private boolean hasAuthority(Collection<GrantedAuthority> authlist, String role) {
        for (GrantedAuthority auth : authlist) {
            if (role.equals(auth.getAuthority())) {
                return true;
            }
        }

        return false;
    }

    private UserDetails userDetailsFromOpenIdResponse(OpenIDAuthenticationToken response) {
        String email = null;
        String name = null;
        String username = null;
        for (OpenIDAttribute attr : response.getAttributes()) {
            if ("email".equals(attr.getName())) {
                for (String value : attr.getValues()) {
                    if (value != null) {
                        email = value;
                        break;
                    }
                }
            }

            if ("name".equals(attr.getName())) {
                for (String value : attr.getValues()) {
                    if (value != null) {
                        name = value;
                        break;
                    }
                }
            }

            if ("username".equals(attr.getName())) {
                for (String value : attr.getValues()) {
                    if (value != null) {
                        username = value;
                        break;
                    }
                }
            }
        }

        if (username == null) {
            username = email;
        }

        Account account = new Account();
        account.setEmail(email);
        account.setDisplayname(name == null ? email : name);
        account.setName(username);
        account.getIdentifiers().add(response.getIdentityUrl());
        return new QashUserDetails(account);
    }
}
