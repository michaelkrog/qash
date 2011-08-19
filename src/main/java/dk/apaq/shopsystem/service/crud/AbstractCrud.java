package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.CrudListener;
import dk.apaq.crud.HasId;
import dk.apaq.crud.core.BaseEditableCrud;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
public abstract class AbstractCrud<I, T extends HasId<I>> extends BaseEditableCrud<I, T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCrud.class);
    
    @PersistenceContext
    protected EntityManager em;
    private List<CrudListener> listeners = new ArrayList<CrudListener>();

    private final Class<T> entityClass;

    public AbstractCrud(Class<T> entityClass) {
        this.entityClass = entityClass;
    }



    public List<I> listIds() {
        return this.listIds(null);
    }

    @Transactional
    public I create() {
        fireOnBeforeCreate();
        
        if(LOG.isDebugEnabled()) {
            LOG.debug("Creating new entity [type=" + entityClass.getName() + "]");
        }

        T entity = createEntityInstance();
        entity = em.merge(entity);
        em.flush();
        fireOnCreate(entity.getId(), entity);
        return entity.getId();
    }

    public T read(I id) {
        if(id == null) {
            return null;
        }

        fireOnBeforeRead(id);

        if(LOG.isDebugEnabled()) {
            LOG.debug("Reading entity [type=" + entityClass.getName() + ";id=" + id + "]");
        }

        T entity = em.find(entityClass, id);
        if (entity == null) {
           return null;
        }
        fireOnRead(entity.getId(), entity);
        return entity;
    }

    @Transactional
    public void update(T entity) {
        fireOnBeforeUpdate(entity.getId(), entity);
        
        if(LOG.isDebugEnabled()) {
            LOG.debug("Updating entity [type=" + entityClass.getName() + ";id=" + entity.getId() + "]");
        }

        entity = em.merge(entity);
        em.flush();
        fireOnUpdate(entity.getId(), entity);
    }

    @Transactional
    public void delete(I id) {
        fireOnBeforeDelete(id);
        
        if(LOG.isDebugEnabled()) {
            LOG.debug("Deleting entity [type=" + entityClass.getName() + ";id=" + id + "]");
        }

        T existingEntity = read(id);
        em.remove(existingEntity);
        em.flush();
        fireOnDelete(id);
    }

    protected abstract T createEntityInstance();

    
    protected Authentication getAuthentication() {
        LOG.debug("Getting user authentication");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            LOG.debug("User not authenticated.");
            throw new SecurityException("User not authenticated.");
        }
        return auth;
    }

    /*
    protected boolean isAdministrator(Authentication auth) {
        LOG.debug("Checking whether user is administrator");
        if (auth == null) {
            LOG.debug("IS not administrator because authetication was null.");
            return false;
        }

        for (GrantedAuthority authority : auth.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                LOG.debug("Was administrator because user had role 'ROLE_ADMIN'.");

                return true;

            }
        }

        LOG.debug("Was NOT administrator because user did NOT have role 'ROLE_ADMIN'.");
        return false;
    }
     * */
     

    
}
