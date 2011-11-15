package dk.apaq.shopsystem.annex;


import java.awt.print.Printable;
import java.io.OutputStream;

/**
 * A service that offers methods to write receipts, invoices and more.
 * @author michael
 */
public interface AnnexService {

        void generate(AnnexContext<CommercialDocumentContent, OutputStream> context, AnnexType annexType, OutputType outputType) throws Exception;
        Printable generatePrintable(AnnexContext<CommercialDocumentContent, Void> context, AnnexType annexType, OutputType outputType) throws Exception;

        @Deprecated
        void generateReceipt(AnnexContext<CommercialDocumentContent, OutputStream> context, OutputType outputType) throws Exception ;
        @Deprecated
        Printable generatePrintableReceipt(AnnexContext<CommercialDocumentContent, Void> context) throws Exception ;

        @Deprecated
        void generateInvoice(AnnexContext<CommercialDocumentContent, OutputStream> context, OutputType outputType) throws Exception ;
        @Deprecated
        Printable generatePrintableInvoice(AnnexContext<CommercialDocumentContent, Void> context) throws Exception ;
        
}
