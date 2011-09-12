package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.jpa.EntityManagerCrud;
import dk.apaq.filter.Filter;
import dk.apaq.filter.jpa.FilterTranslatorForJPA;
import dk.apaq.filter.limit.Limit;
import dk.apaq.filter.sort.Sorter;
import java.lang.reflect.Field;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author michaelzachariassenkrog
 */
public class EntityManagerCrudForSpring<IDTYPE, BEANTYPE> extends EntityManagerCrud<IDTYPE, BEANTYPE> {

    public EntityManagerCrudForSpring(EntityManager em, Class clazz) {
        super(em, clazz);
    }

    @Override
    @Transactional
    public void update(BEANTYPE entity) {
        super.update(entity);
    }

    @Override
    @Transactional
    public void delete(IDTYPE id) {
        super.delete(id);
    }

    @Override
    @Transactional
    public IDTYPE create() {
        return super.create();
    }

}
