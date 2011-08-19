/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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

    private final EntityManager em;
    private final EntityManagerCrudAssist<IDTYPE, BEANTYPE> assist;

    public EntityManagerCrudForSpring(EntityManager em, EntityManagerCrudAssist<IDTYPE, BEANTYPE> assist) {
        super(em, assist);
        this.em = em;
        this.assist = assist;
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


    public List<IDTYPE> listIds(Filter filter, Sorter sorter, Limit limit) {
        Query q = FilterTranslatorForJPA.translate(em, new String[]{getIdProperty()}, assist.getEntityClass(), filter, sorter, limit);
        return q.getResultList();
    }

    private String getIdProperty() {
        Field field = getIdField();
        if (field != null) {
            return field.getName();
        }
        return null;
    }

    private Field getIdField() {
        Class clazz = assist.getEntityClass();

        while(clazz!=null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getAnnotation(Id.class) != null) {
                    return field;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

}
