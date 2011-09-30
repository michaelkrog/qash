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

        String paramName1 = getParamName();
        String paramName2 = getParamName();
        String subJpql = "from e.domains as dr where dr.subDomain = :" + paramName1 + " and dr.domain.name = :"+paramName2;
        String jpql = "exists (" + subJpql + ")";
        //String jpql = "e.domains.subDomain = :"+paramName1+" and e.domains.domain.name = :"+paramName2;
        clause.appendStatement(jpql);
        clause.appendParameter(paramName1, filter.getSubDomain());
        clause.appendParameter(paramName2, filter.getDomain());
        return true;
    }
}
