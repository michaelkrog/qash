package dk.apaq.shopsystem.rendering.media;

import dk.apaq.filter.Filter;
import dk.apaq.filter.core.AndFilter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.core.OrFilter;

/**
 *
 * @author michael
 */
public class FilterTranslatorForCssMedia {
 
    public static Filter translate(String cssMediaString) {
        if(cssMediaString.startsWith("only ")) {
            cssMediaString.substring(5);
        }
        
        String[] queries = cssMediaString.split(",");
        OrFilter orFilter = new OrFilter();
        for(String query:queries) {
            orFilter.addFilter(getFilterForQuery(query));
        }
        
        return orFilter;
    }
    
    private static Filter getFilterForQuery(String query) {
        query = query.trim();
        String[] conditions = query.split(" and ");
        AndFilter andFilter = new AndFilter();
        
        for(String condition : conditions) {
            condition = condition.trim();
            if(condition.startsWith("(")) {
                //query
                condition = condition.substring(1, condition.length()-1);
                String[] conditionElements = condition.split(":");
                Filter filter = getFilterFromCondition(conditionElements);
                if(filter!=null) {
                    andFilter.addFilter(filter);
                }
            } else {
                //media
                if(!"all".equals(condition)) {
                    andFilter.addFilter(new CompareFilter("media", condition, CompareFilter.CompareType.Equals));
                }
            }
        }
        return andFilter;
    }
    
    private static Filter getFilterFromCondition(String[] elements) {
        String queryType = elements[0];
        String queryValue = elements.length < 2 ? null : elements[1];
        CompareFilter.CompareType compareType = CompareFilter.CompareType.Equals;
        
        if(queryType.startsWith("min-")) {
            queryType = queryType.substring(4);
            compareType = CompareFilter.CompareType.GreaterOrEqual;
        } else if(queryType.startsWith("max-")) {
            queryType = queryType.substring(4);
            compareType = CompareFilter.CompareType.LessOrEqual;
        }
        
        if("device-width".equals(queryType)) {
            int value = Integer.parseInt(queryValue);
            return new CompareFilter("device-width", value, compareType);
        }
        
        if("device-height".equals(queryType)) {
            int value = Integer.parseInt(queryValue);
            return new CompareFilter("device-height", value, compareType);
        }
        
        return null;
    }
}
