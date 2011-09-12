package dk.apaq.shopsystem.service.crud;

import dk.apaq.filter.limit.Limit;
import dk.apaq.shopsystem.entity.Organisation;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author michaelzachariassenkrog
 */
public class OrganisationCrudImpl extends EntityManagerCrudForSpring<String, Organisation> implements OrganisationCrud {

    public OrganisationCrudImpl(EntityManager em) {
        super(em, Organisation.class);
    }


    @Override
    public List<String> listIds(Limit limit, String forUser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
