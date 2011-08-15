package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.filter.Filter;
import dk.apaq.filter.jpa.FiltrationJpaTranslator;
import dk.apaq.filter.limit.Limit;
import dk.apaq.filter.sort.Sorter;
import dk.apaq.shopsystem.model.Account;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author michaelzachariassenkrog
 */
public class AccountCrudImpl extends AbstractCrud<String, Account> implements Crud.Complete<String, Account> {

    public AccountCrudImpl() {
        super(Account.class);
    }


    @Override
    protected Account createEntityInstance() {
        return new Account();
    }

    public List<String> listIds(Limit limit) {
        return listIds(null, null, limit);
    }

    /*
    private void checkRead(Account account) {
        Authentication auth = getAuthentication();
        if (account.getName()!=null && !isAdministrator(auth) &&
                !auth.getName().equals(account.getName())) {
            throw new SecurityException("User is not authorized to retrieve account.[Account=" + account.getName() + "; User=" + auth.getName() + "]");
        }
    }

    private void checkEdit(Account account) {
        Authentication auth = getAuthentication();
        if (account.getName()!=null && !isAdministrator(auth)
                    && !auth.getName().equals(account.getName())) {
                throw new SecurityException("User is not administrator or owner of account. Cannot edit account.");
            }
    }*/

    public List<String> listIds(Filter filter, Sorter sorter) {
        return listIds(filter, sorter, null);
    }

    public List<String> listIds(Filter filter, Sorter sorter, Limit limit) {
        Query q = FiltrationJpaTranslator.translate(em, new String[]{"id"}, Account.class, filter, sorter, limit);
        return q.getResultList();
        /*List<String> idlist = new ArrayList<String>();

        StringBuilder sb = new StringBuilder();
        sb.append(JavaPersistenceQueryTranslator.getTranslator().translate(Account.class, filter));
        
        if(sorter!=null) {
            int count = 0;
            for(SorterEntry entry : sorter.getSorterEntries()) {
                sb.append(count == 0 ? " ORDER BY " : ", ");
                sb.append(entry.getPropertyId());
                sb.append(" ");
                sb.append(entry.getDirection() == SortDirection.Ascending ? "ASC" : "DESC");
                count++;
            }
        }

        String query = sb.toString();
        Query q = em.createQuery(query);
        if(limit!=null) {
            q.setFirstResult(limit.getOffset());
            q.setMaxResults(limit.getCount());
        }

        List<Account> modellist = (List<Account>) q.getResultList();

        for (Account model : modellist) {
            idlist.add(model.getId());
        }
        return idlist;*/
    }
    
}
