package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.HasId;
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.AndFilter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.core.ContainsFilter;
import dk.apaq.filter.core.OrFilter;
import dk.apaq.filter.limit.Limit;
import dk.apaq.filter.sort.Sorter;
import dk.apaq.shopsystem.model.Organisation;
import dk.apaq.shopsystem.model.Store;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

/**
 *
 * @author michaelzachariassenkrog
 */
public class ShopCrudImpl extends AbstractContentCrud<String, Store> implements ShopCrud {

    private static final Logger LOG = LoggerFactory.getLogger(ShopCrudImpl.class);

    public ShopCrudImpl(Organisation organisation) {
        super(organisation, Store.class);

        LOG.debug("ShopCrud initialized");
    }

    @Override
    protected Store createInstance() {
        Authentication auth = getAuthentication();

        Store shop = new Store();
        shop.setOrganisation(organisation);
        LOG.debug("Shop instance created");

        return shop;
    }

    @Override
    public Store read(String id) {
        Store shop = super.read(id);
        return shop;
    }

    @Override
    public List<String> listIds(Filter filter, Sorter sorter, Limit limit) {
        Authentication auth = getAuthentication();
        return listIds(filter, sorter, limit, auth.getName());
    }

    public List<String> listIds(Limit limit, String forUser) {
        return listIds(null, null, limit, forUser);
    }

    public List<String> listIds(Filter filter, Sorter sorter, Limit limit, String forUser) {
        Filter wrapFilter;
        if (filter == null) {
            wrapFilter = generateUserFilter();
        } else {
            wrapFilter = new AndFilter(generateUserFilter(), filter);
        }
        return super.listIds(wrapFilter, sorter, limit);
    }

    private Filter generateUserFilter() {
        Authentication auth = getAuthentication();
        Filter createdByFilter = new CompareFilter("createdBy", auth.getName(), CompareFilter.CompareType.Equals);
        Filter inUserListFilter = new ContainsFilter("users", auth.getName());
        return new OrFilter(createdByFilter, inUserListFilter);
    }
}
