package dk.apaq.shopsystem.service;

import dk.apaq.filter.Filter;
import dk.apaq.filter.jpa.WhereClause;
import dk.apaq.filter.jpa.interpreters.AbstractInterpreter;

/**
 *
 * @author michael
 */
public class ContainsDomainFilterInterpreter extends AbstractInterpreter<ContainsDomainFilter> {

    @Override
    public boolean interpret(WhereClause clause, ContainsDomainFilter filter) {
        if (filter.getDomain() == null) {
            return false;
        }

        String paramName = getParamName();
        String subJpql = "select from Domain as dom where dom.name = " + paramName;
        String jpql = "(" + subJpql + ") in elements(e." + filter.getPropertyId() + ")";
        clause.appendStatement(jpql);
        clause.appendParameter(paramName, filter.getDomain());
        return true;
    }
}
