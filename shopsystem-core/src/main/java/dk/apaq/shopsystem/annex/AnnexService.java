package dk.apaq.shopsystem.annex;

import java.util.List;

import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Store;
import dk.apaq.shopsystem.entity.Tax;
import java.io.OutputStream;
import java.util.Locale;
import javax.print.PrintService;

/**
 * A service that offers methods to write receipts, invoices and more.
 * @author michael
 */
public interface AnnexService {


        void printReceipt(AnnexContext<CommercialDocumentContent, PrintService> context) throws Exception ;
	void generateReceipt(AnnexContext<CommercialDocumentContent, OutputStream> context, OutputType outputType) throws Exception ;

        void printInvoice(AnnexContext<CommercialDocumentContent, PrintService> context) throws Exception ;
	void generateInvoice(AnnexContext<CommercialDocumentContent, OutputStream> context, OutputType outputType) throws Exception ;

        public void writeReceipt(Store shop, Order order, OutputType outputType, PageSize pagesize, OutputStream out, Locale locale) throws Exception;
	public void writeInvoice(Store shop, Order order, OutputType outputType, PageSize pagesize, OutputStream out, Locale locale) throws Exception;
	public void writeOrder(Store shop, Order order, OutputStream out, OutputType outputType, Locale locale) throws Exception;
	public void writeOrderList(Store shop, List<Order> orderlist, OutputStream out, OutputType outputType, Locale locale) throws Exception;
	public void writePostings(List<Order> orderlist,List<Tax> taxlist, int account, int offsetaccount, OutputStream out, OutputType outputType, Locale locale) throws Exception;
}
