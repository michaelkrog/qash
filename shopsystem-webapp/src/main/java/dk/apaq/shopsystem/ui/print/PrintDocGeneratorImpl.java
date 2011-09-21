package dk.apaq.shopsystem.ui.print;

import dk.apaq.shopsystem.annex.AnnexService;
import dk.apaq.shopsystem.annex.PageSize;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Payment;
import java.util.List;
import javax.print.Doc;
import javax.print.PrintService;

/**
 *
 * @author michael
 */
public class PrintDocGeneratorImpl implements PrintDocGenerator {

    private final AnnexService annexService;
    private final Organisation organisation;

    public PrintDocGeneratorImpl(AnnexService annexService, Organisation organisation) {
        this.annexService = annexService;
        this.organisation = organisation;
    }
    
    @Override
    public Doc generateReceipt(Order order, PageSize pageSize) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Doc generateInvoice(Order order, PageSize pageSize) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void printInvoice(Order order, PrintService printservice) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void printReceipt(Order order, List<Payment> payments, PrintService printservice) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
