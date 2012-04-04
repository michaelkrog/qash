package dk.apaq.shopsystem.data;

import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderLine;
import dk.apaq.shopsystem.entity.Tax;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author michael
 */
public class DataExchange {

    private static final DateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final NumberFormat usNumberFormat = NumberFormat.getNumberInstance(Locale.US);

    static {
        usNumberFormat.setMinimumFractionDigits(2);
        usNumberFormat.setMaximumFractionDigits(2);

    }

    /**
     * Writes postings based on orders and existing taxes. Date will be ISO-formatted and numbers wil be US-formatted.
     */
    public static void writePostings(List<Order> orderlist, int account, int offsetaccount, OutputStream out) {
        PrintWriter writer = new PrintWriter(out);
        writer.println("DATE;INVOICENUMBER;ITEMNO;DESCRIPTION;CURRENCY;TOTAL;TOTALINCVAT;TAXCODE;TAXRATE;ACCOUNT;OFFSETACCOUNT");

        for (Order order : orderlist) {
            for (OrderLine orderLine : order.getOrderLines()) {
                if (orderLine.getPrice() == 0) {
                    continue;
                }

                String itemNo = orderLine.getItemNo() == null ? "" : orderLine.getItemNo();
                String description = orderLine.getQuantity() + "X" + orderLine.getTitle();
                String taxCode = orderLine.getTax() == null ? "" : "S" + orderLine.getTax().getRate();
                double taxRate = orderLine.getTax() == null ? 0 : orderLine.getTax().getRate();
                Long total = orderLine.getTotal(true);
                Long totalWithTax = orderLine.getTotalWithTax(true);
                
                if(order.getDateInvoiced() == null) {
                    writer.print("-");
                } else {
                    writer.print(isoDateFormat.format(order.getDateInvoiced()));
                }
                
                writer.print(";");

                writer.print(order.getInvoiceNumber());
                writer.print(";");

                writer.print(itemNo);
                writer.print(";");

                writer.print(description);
                writer.print(";");

                writer.print(order.getCurrency());
                writer.print(";");

                
                writer.print(usNumberFormat.format(total.doubleValue() / 100));
                writer.print(";");

                writer.print(usNumberFormat.format(totalWithTax.doubleValue() / 100));
                writer.print(";");

                writer.print(taxCode);
                writer.print(";");

                writer.print(taxRate);
                writer.print(";");

                writer.print(account);
                writer.print(";");

                writer.println(offsetaccount);

            }
        }
        
        writer.flush();


    }
}
