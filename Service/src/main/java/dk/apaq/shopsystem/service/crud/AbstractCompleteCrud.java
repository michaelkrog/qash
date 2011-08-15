package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.crud.HasId;
import dk.apaq.crud.core.BaseCompleteCrud;
import dk.apaq.filter.Filter;
import dk.apaq.filter.jpa.FiltrationJpaTranslator;
import dk.apaq.filter.limit.Limit;
import dk.apaq.filter.sort.Sorter;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
public abstract class AbstractCompleteCrud<I, T extends HasId<I>> extends BaseCompleteCrud<I, T> implements Crud.Complete<I, T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCompleteCrud.class);
    @PersistenceContext
    protected EntityManager em;
    private final Class<T> entityClass;

    public AbstractCompleteCrud(Class entityClass) {
        this.entityClass = entityClass;
    }

    protected Authentication getAuthentication() {
        LOG.debug("Retrieving user authentication.");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            LOG.debug("User not authenticated");
            throw new SecurityException("User not authenticated.");
        }
        return auth;
    }

    protected boolean isAdministrator(Authentication auth) {
        boolean returnVal = false;

        if (auth != null) {
            for (GrantedAuthority authority : auth.getAuthorities()) {
                if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                    returnVal = true;
                    break;
                }
            }
        }

        LOG.debug(returnVal ? "User IS administrator" : "User IS NOT administrator");

        return returnVal;
    }

    protected abstract T createInstance();

    @Override
    @Transactional
    public I create() {
        LOG.debug("Creating new entity instance.");

        fireOnBeforeCreate();
        T entity = createInstance();
        entity.setId((I) UUID.randomUUID().toString());
        entity = em.merge(entity);
        em.flush();
        fireOnCreate(entity.getId(), entity);
        return entity.getId();
    }

    @Override
    public T read(I id) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Reading item [id=" + id + "]");
        }

        if (id == null) {
            return null;
        }

        fireOnBeforeRead(id);

        T entity = em.find(entityClass, id);
        if(entity != null) {
            fireOnRead(entity.getId(), entity);
        }
        return entity;
    }

    @Override
    @Transactional
    public void update(T entity) {
        LOG.debug("Updating entity [id={}]", entity.getId());
        
        fireOnBeforeUpdate(entity.getId(), entity);
        em.merge(entity);
        em.flush();
        fireOnUpdate(entity.getId(), entity);
    }

    @Override
    @Transactional
    public void delete(I id) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting entity [id=" + id + "]");
        }
        fireOnBeforeDelete(id);
        T entity = read(id);
        em.remove(entity);
        em.flush();
        fireOnDelete(id);
    }

    protected Class getEntityClass() {
        return entityClass;
    }

    public List<I> listIds() {
        return listIds(null, null, null);
    }

    public List<I> listIds(Limit limit) {
        return listIds(null, null, limit);
    }

    @Override
    public List<I> listIds(Filter filter, Sorter sorter) {
        return listIds(filter, sorter, null);
    }

    @Override
    public List<I> listIds(Filter filter, Sorter sorter, Limit limit) {
        Query q = FiltrationJpaTranslator.translate(em, new String[]{"id"}, getEntityClass(), filter, sorter, limit);
        return q.getResultList();
    }
}
