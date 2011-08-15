package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.model.Organisation;
import dk.apaq.shopsystem.model.Store;
import dk.apaq.shopsystem.model.Tax;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author michaelzachariassenkrog
 */
public class TaxCrudImpl extends AbstractContentCrud<String, Tax> implements Crud.Editable<String, Tax> {

    private static final Logger LOG = LoggerFactory.getLogger(TaxCrudImpl.class);

    public TaxCrudImpl(Organisation organisation) {
        super(organisation, Tax.class);
    }

    @Override
    protected Tax createInstance() {
        LOG.debug("Creating new Tax instance.");
        Tax tax = new Tax();
        tax.setOrganisation(organisation);
        return tax;
    }

}
