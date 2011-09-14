package dk.apaq.shopsystem.service.crud;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.apaq.filter.Filter;
import dk.apaq.filter.core.AndFilter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.limit.Limit;
import dk.apaq.filter.sort.Sorter;
import dk.apaq.shopsystem.entity.BaseUser;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.entity.SystemUserReference;
import javax.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author michaelzachariassenkrog
 */
public class UserCrudImpl extends EntityManagerCrudForSpring<String, BaseUser> implements UserCrud {

    private static final Logger LOG = LoggerFactory.getLogger(UserCrudImpl.class);
    protected final Organisation organisation;
    private final Filter orgFilter;
    
    public UserCrudImpl(EntityManager em, Organisation organisation) {
        super(em, BaseUser.class);
        this.organisation = organisation;
        this.orgFilter = new CompareFilter("organisation", organisation, CompareFilter.CompareType.Equals);
    }

    @Override
    @Transactional
    public String create() {
        throw new UnsupportedOperationException("No supported.");
    }

    @Override
    @Transactional
    public  String create(BaseUser entity) {
        throw new UnsupportedOperationException("No supported.");
    }

    @Transactional
    @Override
    public  String createSystemUser() {
        SystemUser user = new SystemUser();
        user.setOrganisation(organisation);
        return super.create(user);
    }

    @Transactional
    @Override
    public  String createSystemUserReference(SystemUser user) {
        SystemUserReference userref = new SystemUserReference();
        userref.setOrganisation(organisation);
        userref.setUser(user);
        return super.create(userref);
    }

    @Override
    public List<String> listIds(Filter filter, Sorter sorter, Limit limit) {
        Filter wrapFilter;
        if(filter==null) {
            wrapFilter = orgFilter;
        } else {
            wrapFilter = new AndFilter(orgFilter, filter);
        }

        return super.listIds(wrapFilter, sorter, limit);
    }



}
