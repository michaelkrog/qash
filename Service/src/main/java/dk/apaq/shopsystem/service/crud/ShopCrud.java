package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.filter.limit.Limit;
import dk.apaq.shopsystem.model.Store;
import java.util.List;

/**
 *
 * @author michaelzachariassenkrog
 */
public interface ShopCrud extends Crud.Editable<String, Store>{

    List<String> listIds(Limit limit, String forUser);
    
}
