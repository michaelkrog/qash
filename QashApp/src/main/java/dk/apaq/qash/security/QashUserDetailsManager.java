package dk.apaq.qash.security;

import dk.apaq.crud.Crud;
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.core.ContainsFilter;
import dk.apaq.filter.core.OrFilter;
import dk.apaq.qash.server.service.Service;
import dk.apaq.qash.share.model.Account;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 *
 * @author michael
 */
public class QashUserDetailsManager implements UserDetailsManager {

    private Service service = null;
    private Crud.Complete<String, Account> crud;
    
    public QashUserDetailsManager(Service service) {
        this.service = service;
        this.crud = service.getAccountCrud();
    }

    @Override
    public void createUser(UserDetails user) {
        updateUser(user);
    }

    @Override
    public void updateUser(UserDetails user) {
        assert user instanceof QashUserDetails;

        QashUserDetails qashUser = (QashUserDetails)user;
        crud.update(qashUser.getAccount());
    }

    @Override
    public void deleteUser(String username) {
        crud.delete(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean userExists(String username) {
        return loadUserByUsername(username) !=null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        try{
            Filter filterUsername = new CompareFilter("name", username, CompareFilter.CompareType.Equals);
            Filter filterIdentifier = new ContainsFilter("identifiers", username);
            Filter orFilter = new OrFilter(filterUsername, filterIdentifier);
            
            List<String> accounts = crud.listIds(orFilter, null);
            if(accounts.isEmpty()){
                throw new UsernameNotFoundException("User does not exist. [username=" + username + "]");
            }

            if(accounts.size() > 1){
                throw new UsernameNotFoundException("More than one user found. [username=" + username + "]");
            }

            Account account = crud.read(accounts.get(0));
            return new QashUserDetails(account);
        } catch (UsernameNotFoundException ex){
            throw ex;
        }  catch(Exception ex2){
            throw new DataAccessResourceFailureException("Unable to load user.",ex2);
        }
    }

}
