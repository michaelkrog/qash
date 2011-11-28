package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.filter.limit.Limit;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.SystemUser;
import java.util.List;

/**
 *
 * @author michaelzachariassenkrog
 */
public interface OrganisationCrud extends Crud.Editable<String, Organisation>{

    List<String> listIds(Limit limit, SystemUser user);
    List<Organisation> list(Limit limit, SystemUser user);
    
}
