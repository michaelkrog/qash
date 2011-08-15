package dk.apaq.shopsystem.service.crud;


import dk.apaq.crud.HasId;
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.AndFilter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.limit.Limit;
import dk.apaq.filter.lucene.FilterTranslatorForLucene;
import dk.apaq.filter.lucene.FiltrationLuceneTranslator;
import dk.apaq.filter.sort.SortDirection;
import dk.apaq.filter.sort.Sorter;
import dk.apaq.filter.sort.SorterEntry;
import dk.apaq.shopsystem.service.ServiceException;
import dk.apaq.shopsystem.model.Organisation;
import dk.apaq.shopsystem.model.Store;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author michaelzachariassenkrog
 */
public abstract class AbstractContentCrud<I, T extends HasId<I>> extends AbstractCompleteCrud<I, T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractContentCrud.class);
    protected final Organisation organisation;
    private final Filter orgFilter;

    @PersistenceUnit
    private EntityManagerFactory emf;

    public AbstractContentCrud(Organisation organisation, Class entityClass) {
        super(entityClass);
        this.organisation = organisation;
        this.orgFilter = new CompareFilter("organisation", organisation, CompareFilter.CompareType.Equals);
    }

    @Override
    public List<I> listIds(Filter filter, Sorter sorter, Limit limit) {
        Filter wrapFilter;
        if(filter==null) {
            wrapFilter = orgFilter;
        } else {
            wrapFilter = new AndFilter(orgFilter, filter);
        }

        return super.listIds(wrapFilter, sorter, limit);
    }


    /*
    protected List<I> listIdsFromIndex(Filter filter, Sorter sorter, Limit limit, String[] fields, BooleanClause.Occur[] flags, Class clazz) {
        LOG.debug("List ids from Hibernate Search index.");
        List<I> idlist = new ArrayList<I>();

        EntityManager em = emf.createEntityManager();
        FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
        em.getTransaction().begin();

        Filter ownerFilter = new CompareFilter("organsiation", organisation.getId(), CompareFilter.CompareType.Equals);
        Filter finalFilter = new AndFilter(ownerFilter, filter);
        Query searchQuery = FilterTranslatorForLucene.getTranslator().translateToQuery(finalFilter);
        
        // wrap Lucene query in a javax.persistence.Query
        FullTextQuery persistenceQuery = fullTextEntityManager.createFullTextQuery(searchQuery, clazz);

        if (sorter != null) {
            //Lucene only supports one Sort field. We only use the first one
            if (!sorter.getSorterEntries().isEmpty()) {
                SorterEntry se = sorter.getSorterEntries().get(0);
                boolean sortReverse = se.getDirection() == SortDirection.Descending;
                Sort sort = new Sort(new SortField(se.getPropertyId(), SortField.STRING, sortReverse));

                if(LOG.isDebugEnabled()) {
                    LOG.debug("Order query by field [field = " + se.getPropertyId()  + "]");
                }

                persistenceQuery.setSort(sort);
            }
        }

        if (limit != null) {
            persistenceQuery.setFirstResult(limit.getOffset());
            persistenceQuery.setMaxResults(limit.getCount());

            if(LOG.isDebugEnabled()) {
                LOG.debug("Limiting query results. [offset = " + limit.getOffset() + "; count = " + limit.getCount() + "]");
            }
        }

        List<T> result = persistenceQuery.getResultList();

        em.getTransaction().commit();
        em.close();
        
        for(T current : result) {
            idlist.add(current.getId());
        }

        LOG.debug("Found " + idlist.size() + " matched when searching index.");

        return idlist;
    }
*/

}
