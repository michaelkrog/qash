package dk.apaq.shopsystem.qash.common;

import dk.apaq.filter.Filter;
import dk.apaq.filter.FilterGenerator;
import dk.apaq.filter.core.LikeFilter;
import dk.apaq.filter.core.OrFilter;

/**
 *
 * @author michael
 */
public class StoreFilterGenerator implements FilterGenerator {

    @Override
    public Filter generateFilter(String filterString) {
        if (!filterString.startsWith("*")) {
            filterString = filterString + "*";
        }

        if (!filterString.endsWith("*")) {
            filterString = filterString + "*";
        }

        if (filterString != null && !"".equals(filterString)) {
            LikeFilter filter1 = new LikeFilter("name", filterString, false);
            LikeFilter filter2 = new LikeFilter("email", filterString, false);
            
            return new OrFilter(filter1, filter2);
        } else {
            return null;
        }
    }
}
