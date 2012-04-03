package dk.apaq.shopsystem.security;

/**
 *
 * @author michaelzachariassenkrog
 */
public enum SecurityRoles {
    /**
     * Role for a system administrator
     */
    ROLE_ADMIN, 
    
    /**
     * Role for an organisation administrator
     */
    ROLE_ORGADMIN, 
    
    /**
     * Role for the on that should recieve payment information for an organisation.
     */
    ROLE_ORGPAYER
}
