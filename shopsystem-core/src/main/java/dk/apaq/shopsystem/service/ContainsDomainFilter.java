package dk.apaq.shopsystem.service;

import dk.apaq.filter.FiltrationItem;
import dk.apaq.filter.core.AbstractFilterComponent;

/**
 *
 * @author michael
 */
public class ContainsDomainFilter extends AbstractFilterComponent{

    private final String domain;

    public ContainsDomainFilter(String propertyId, String domain) {
        super(propertyId);
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }
    
    @Override
    public boolean passesFilter(FiltrationItem item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
