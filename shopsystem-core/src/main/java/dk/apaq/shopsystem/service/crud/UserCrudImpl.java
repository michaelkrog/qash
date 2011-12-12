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
import dk.apaq.shopsystem.entity.ContentEntity;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.entity.SystemUserReference;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author michaelzachariassenkrog
 */
public class UserCrudImpl extends ContentCrud<BaseUser> implements UserCrud {

    private static final Logger LOG = LoggerFactory.getLogger(UserCrudImpl.class);
    
    @Autowired
    private SystemService service;
    
    public UserCrudImpl(EntityManager em, Organisation organisation) {
        super(em, organisation, BaseUser.class);
    }

    @Override
    @Transactional
    public String create() {
        throw new UnsupportedOperationException("No supported.");
    }

    @Override
    @Transactional
    public String create(BaseUser entity) {
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
    @Transactional
    public void delete(String id) {
        
        BaseUser user = read(id);
        Organisation org = user.getOrganisation();
        
        //org should never be null, but we cant be 100% sure so we check anyway
        if(user instanceof SystemUser && org!=null) {
            OrganisationService organisationService = service.getOrganisationService(org);
            List<BaseUser> users = organisationService.getUsers().list();
            
            int realUserCount = 0;
            for(BaseUser current: users) {
                if(current instanceof SystemUser) {
                    realUserCount++;
                }
            }
            
            if(realUserCount<2) {
                throw new RuntimeException("Unable to delete user because it is the last user owned by the organisation.");
            }
        }
        
        super.delete(id);
    }


}
