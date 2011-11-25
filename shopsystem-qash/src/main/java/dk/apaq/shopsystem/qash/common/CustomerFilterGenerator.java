package dk.apaq.shopsystem.qash.common;

import dk.apaq.filter.Filter;
import dk.apaq.filter.FilterGenerator;
import dk.apaq.filter.core.LikeFilter;
import dk.apaq.filter.core.OrFilter;

/**
 *
 * @author michael
 */
public class CustomerFilterGenerator implements FilterGenerator {

    @Override
    public Filter generateFilter(String filterString) {
        if (!filterString.startsWith("*")) {
            filterString = filterString + "*";
        }

        if (!filterString.endsWith("*")) {
            filterString = filterString + "*";
        }

        if (filterString != null && !"".equals(filterString)) {
            LikeFilter filter1 = new LikeFilter("contactName", filterString, false);
            LikeFilter filter2 = new LikeFilter("companyName", filterString, false);
            LikeFilter filter3 = new LikeFilter("username", filterString, false);
            LikeFilter filter4 = new LikeFilter("email", filterString, false);
            LikeFilter filter5 = new LikeFilter("companyRegistration", filterString, false);

            return new OrFilter(filter1, filter2, filter3, filter4, filter5);
        } else {
            return null;
        }
    }
}
