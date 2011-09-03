package dk.apaq.shopsystem.ui.util;

import dk.apaq.filter.Filter;
import dk.apaq.filter.FilterGenerator;
import dk.apaq.filter.core.LikeFilter;
import dk.apaq.filter.core.OrFilter;

/**
 *
 * @author michaelzachariassenkrog
 */
public class ProductFilterGenerator implements FilterGenerator {

        public Filter generateFilter(String filterString) {
            if (!filterString.endsWith("*")) {
                filterString = filterString + "*";
            }

            if (filterString != null && !"".equals(filterString)) {
                LikeFilter filter1 = new LikeFilter("name", filterString, 0.6F, false);
                LikeFilter filter2 = new LikeFilter("barcode", filterString, false);
                LikeFilter filter3 = new LikeFilter("itemNo", filterString, false);

                return new OrFilter(filter1, filter2, filter3);
            } else {
                return null;
            }
        }

}
