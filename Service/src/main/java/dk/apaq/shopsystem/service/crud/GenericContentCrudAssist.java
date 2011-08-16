package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.jpa.EntityManagerCrud.EntityManagerCrudAssist;
import dk.apaq.shopsystem.model.AbstractContentEntity;
import dk.apaq.shopsystem.model.Organisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author michael
 */
public class GenericContentCrudAssist<T extends AbstractContentEntity> implements EntityManagerCrudAssist<String, T> {

    private static final Logger LOG = LoggerFactory.getLogger(GenericContentCrudAssist.class);
    private final Organisation organisation;
    private final Class<T> clazz;

    public GenericContentCrudAssist(Organisation organisation, Class<T> clazz) {
        this.organisation = organisation;
        this.clazz = clazz;
    }

    @Override
    public Class<T> getEntityClass() {
        return clazz;
    }

    @Override
    public T createInstance() {
        try {
            T entity = clazz.newInstance();
            entity.setOrganisation(organisation);
            return entity;
        }
        catch (InstantiationException ex) {
            LOG.error("Unable to create new instance.", ex);
        }
        catch (IllegalAccessException ex) {
            LOG.error("Unable to create new instance.", ex);
        }
        return null;
    }

    @Override
    public String getIdForEntity(T entity) {
        return entity.getId();
    }
}
