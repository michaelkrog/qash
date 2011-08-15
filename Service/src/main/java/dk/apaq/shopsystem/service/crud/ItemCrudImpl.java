package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.filter.Filter;
import dk.apaq.filter.limit.Limit;
import dk.apaq.filter.sort.SortDirection;
import dk.apaq.filter.sort.Sorter;
import dk.apaq.filter.sort.SorterEntry;
import dk.apaq.shopsystem.model.Organisation;
import dk.apaq.shopsystem.model.Product;
import dk.apaq.shopsystem.model.Store;
import java.util.List;
import org.apache.lucene.search.BooleanClause;

/**
 *
 */
public class ItemCrudImpl extends AbstractContentCrud<String, Product> implements Crud.Complete<String, Product> {

    public ItemCrudImpl(Organisation organisation) {
        super(organisation, Product.class);
    }

    @Override
    protected Product createInstance() {
        Product product = new Product();
        product.setOrganisation(organisation);
        return product;
    }


    /*
    @Override
    public List<String> listIds(Filter filter, Sorter sorter, Limit limit) {
        if(filter == null && sorter == null) {
            return super.listIds(filter, sorter, limit);
        }

        if(sorter==null) {
            SorterEntry entry = new SorterEntry("name_sortable", SortDirection.Ascending);
            sorter = new Sorter(entry);
        }
        String[] fields = {"name", "barcode", "itemNo"};
        BooleanClause.Occur[] flags = {BooleanClause.Occur.SHOULD,
                                    BooleanClause.Occur.SHOULD,
                                    BooleanClause.Occur.SHOULD};

        return listIdsFromIndex(filter, sorter, limit, fields, flags, Product.class);
    }*/

    
}
