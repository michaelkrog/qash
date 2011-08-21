package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.jpa.EntityManagerCrud;
import dk.apaq.filter.limit.Limit;
import dk.apaq.shopsystem.entity.Organisation;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author michaelzachariassenkrog
 */
public class OrganisationCrudImpl extends EntityManagerCrudForSpring<String, Organisation> implements OrganisationCrud {


     private static class OrganisationCrudAssist implements EntityManagerCrudAssist<String, Organisation> {

        @Override
        public Class<Organisation> getEntityClass() {
            return Organisation.class;
        }

        @Override
        public Organisation createInstance() {
            Organisation org = new Organisation();
            return org;
        }

        @Override
        public String getIdForEntity(Organisation entity) {
            return entity.getId();
        }



    }

    public OrganisationCrudImpl(EntityManager em) {
        super(em, new OrganisationCrudAssist());
    }


    @Override
    public List<String> listIds(Limit limit, String forUser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
