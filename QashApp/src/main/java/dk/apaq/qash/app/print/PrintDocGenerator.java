/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.qash.app.print;

import dk.apaq.qash.server.annex.PageSize;
import dk.apaq.qash.share.model.Order;
import dk.apaq.qash.share.model.Payment;
import java.util.List;
import javax.print.Doc;
import javax.print.PrintService;

/**
 *
 * @author michaelzachariassenkrog
 */
public interface PrintDocGenerator {

    //OrderEditor.this.annexService.writeReceipt(OrderEditor.this.shop, dataSource.getBean(), OutputType.SvgBundle, PageSize.Receipt, os, getLocale());

    Doc generateReceipt(Order order, PageSize pageSize) throws Exception;
    Doc generateInvoice(Order order, PageSize pageSize) throws Exception;

    void printInvoice(Order order, PrintService printservice) throws Exception;
    void printReceipt(Order order, List<Payment> payments, PrintService printservice) throws Exception;
}
