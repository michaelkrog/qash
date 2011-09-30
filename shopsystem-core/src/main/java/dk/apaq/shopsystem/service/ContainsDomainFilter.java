package dk.apaq.shopsystem.service;

import dk.apaq.filter.FiltrationItem;
import dk.apaq.filter.core.AbstractFilterComponent;

/**
 *
 * @author michael
 */
public class ContainsDomainFilter extends AbstractFilterComponent{

    private final String domain;
    private final String subDomain;

    public ContainsDomainFilter(String propertyId, String domain, String subDomain) {
        super(propertyId);
        this.domain = domain;
        this.subDomain = subDomain;
    }

    public String getDomain() {
        return domain;
    }

    public String getSubDomain() {
        return subDomain;
    }

    @Override
    public boolean passesFilter(FiltrationItem item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
