package dk.apaq.shopsystem.service.crud;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.apaq.filter.Filter;
import dk.apaq.filter.core.AndFilter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.limit.Limit;
import dk.apaq.filter.sort.Sorter;
import dk.apaq.shopsystem.entity.AbstractContentEntity;
import dk.apaq.shopsystem.entity.Organisation;
import javax.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author michaelzachariassenkrog
 */
public class ContentCrud<T extends AbstractContentEntity> extends EntityManagerCrudForSpring<String, T> {

    private static final Logger LOG = LoggerFactory.getLogger(ContentCrud.class);
    protected final Organisation organisation;
    private final Filter orgFilter;
    private final Class<T> clazz;
    
    public ContentCrud(EntityManager em, Organisation organisation, Class<T> clazz) {
        super(em, clazz);
        this.organisation = organisation;
        this.clazz = clazz;
        this.orgFilter = new CompareFilter("organisation", organisation, CompareFilter.CompareType.Equals);
    }

    @Override
    @Transactional
    public String create() {
        T entity;
        try {
            entity = clazz.newInstance();
        } catch (Exception ex) {
            LOG.error("Unable to create instance of class " + clazz.getCanonicalName(), ex);
            return null;
        }
        entity.setOrganisation(organisation);
        return create(entity);
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
