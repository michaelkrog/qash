package dk.apaq.shopsystem.annex;


import java.awt.print.Printable;
import java.io.OutputStream;

/**
 * A service that offers methods to write receipts, invoices and more.
 * @author michael
 */
public interface AnnexService {

        void generatePurchaseDocument(AnnexContext<OrderDocumentContent, OutputStream> context, AnnexType annexType, OutputType outputType) throws Exception;
        Printable generatePurchaseDocumentPrintable(AnnexContext<OrderDocumentContent, Void> context, AnnexType annexType) throws Exception;

        void generateAuditReport(AnnexContext<AuditReportContent, OutputStream> context, AnnexType annexType, OutputType outputType);
        Printable generateAuditReportPrintable(AnnexContext<AuditReportContent, Void> context, AnnexType annexType);
        
}
