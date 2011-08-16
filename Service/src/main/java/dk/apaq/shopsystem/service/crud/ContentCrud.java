package dk.apaq.shopsystem.service.crud;

import java.util.List;

import java.util.logging.Level;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.apaq.crud.HasId;
import dk.apaq.crud.jpa.EntityManagerCrud;
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.AndFilter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.limit.Limit;
import dk.apaq.filter.sort.Sorter;
import dk.apaq.shopsystem.model.AbstractContentEntity;
import dk.apaq.shopsystem.model.Organisation;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author michaelzachariassenkrog
 */
public abstract class ContentCrud<T extends AbstractContentEntity> extends EntityManagerCrud<String, T> {

    private static final Logger LOG = LoggerFactory.getLogger(ContentCrud.class);
    protected final Organisation organisation;
    private final Filter orgFilter;
    
    
    
    public ContentCrud(EntityManager em, Organisation organisation, EntityManagerCrudAssist<String, T> assist) {
        super(em, assist);
        this.organisation = organisation;
        this.orgFilter = new CompareFilter("organisation", organisation, CompareFilter.CompareType.Equals);
    }

    @Override
    public List<String> listIds(Filter filter, Sorter sorter, Limit limit) {
        Filter wrapFilter;
        if(filter==null) {
            wrapFilter = orgFilter;
        } else {
            wrapFilter = new AndFilter(orgFilter, filter);
        }

        return super.listIds(wrapFilter, sorter, limit);
    }



}
