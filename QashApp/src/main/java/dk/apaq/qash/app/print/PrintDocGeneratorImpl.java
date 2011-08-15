package dk.apaq.qash.app.print;

import dk.apaq.qash.server.OutputType;
import dk.apaq.qash.server.annex.AnnexContext;
import dk.apaq.qash.server.annex.AnnexService;
import dk.apaq.qash.server.annex.CommercialDocumentContent;
import dk.apaq.qash.server.annex.Page;
import dk.apaq.qash.server.annex.PageSize;
import dk.apaq.qash.share.model.Order;
import dk.apaq.qash.share.model.Payment;
import dk.apaq.qash.share.model.Shop;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;
import javax.print.Doc;
import javax.print.PrintService;

/**
 *
 * @author michaelzachariassenkrog
 */
public class PrintDocGeneratorImpl implements PrintDocGenerator {

    //OrderEditor.this.annexService.writeReceipt(OrderEditor.this.shop, dataSource.getBean(), OutputType.SvgBundle, PageSize.Receipt, os, getLocale());

    private final AnnexService annexService;
    private final Shop shop;

    public PrintDocGeneratorImpl(AnnexService annexService, Shop shop) {
        this.annexService = annexService;
        this.shop = shop;
    }

    public Doc generateInvoice(Order order, PageSize pageSize) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        annexService.writeInvoice(shop, order, OutputType.SvgBundle, pageSize, os, Locale.FRENCH);
        return new SvgBundleDoc(os.toByteArray());
    }

    public Doc generateReceipt(Order order, PageSize pageSize) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        annexService.writeReceipt(shop, order, OutputType.SvgBundle, pageSize, os, Locale.FRENCH);
        return new SvgBundleDoc(os.toByteArray());
    }

    public void printInvoice(Order order, PrintService printservice) throws Exception {
        Page page = new Page(PageSize.A4);
        CommercialDocumentContent content = new CommercialDocumentContent(shop, order, null);
        annexService.printInvoice(new AnnexContext<CommercialDocumentContent, PrintService>(content, printservice, page, 300, Locale.FRENCH));
    }

    public void printReceipt(Order order, List<Payment> payments, PrintService printService) throws Exception {
        Page page = new Page(PageSize.A7);
        CommercialDocumentContent content = new CommercialDocumentContent(shop, order, payments);
        annexService.printReceipt(new AnnexContext<CommercialDocumentContent, PrintService>(content, printService, page, 300, Locale.FRENCH));
    }


}
