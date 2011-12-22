package dk.apaq.shopsystem.service.crud;

import dk.apaq.filter.limit.Limit;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.OrganisationUserReference;
import dk.apaq.shopsystem.entity.SystemUser;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author michaelzachariassenkrog
 */
public class OrganisationCrudImpl extends EntityManagerCrudForSpring<String, Organisation> implements OrganisationCrud {

    private EntityManager em;

    public OrganisationCrudImpl(EntityManager em) {
        super(em, Organisation.class);
        this.em = em;
    }

    
    
    @Override
    public List<String> listIds(Limit limit, SystemUser user) {
        return generateOrganisationIdList(user);
    }

    @Override
    public List<Organisation> list(Limit limit, SystemUser user) {
        List<Organisation> orgList = new ArrayList<Organisation>();
        for(String id : generateOrganisationIdList(user)) {
            orgList.add(read(id));
        }
        return orgList;
    }

    private List<String> generateOrganisationIdList(SystemUser user) {
        Query q = em.createQuery("select userref.organisation.id from " + OrganisationUserReference.class.getSimpleName() + " userref where userref.user=:user");
        q.setParameter("user", user);
        return q.getResultList();
    }

}
