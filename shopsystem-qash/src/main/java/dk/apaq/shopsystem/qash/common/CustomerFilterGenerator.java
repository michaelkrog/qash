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
            LikeFilter filter1 = new LikeFilter("customer.contactName", filterString, false);
            LikeFilter filter2 = new LikeFilter("customer.companyName", filterString, false);
            LikeFilter filter3 = new LikeFilter("customer.email", filterString, false);
            LikeFilter filter4 = new LikeFilter("customer.companyRegistration", filterString, false);

            return new OrFilter(filter1, filter2, filter3, filter4);
        } else {
            return null;
        }
    }
}
