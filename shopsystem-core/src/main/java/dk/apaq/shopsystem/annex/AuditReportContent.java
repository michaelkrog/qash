package dk.apaq.shopsystem.annex;

import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Payment;
import java.util.Date;
import java.util.List;

/**
 *
 * @author krog
 */
public class AuditReportContent {
    private final Organisation seller;
    private final Date dateFrom; 
    private final Date dateTo;
    private final List<Order> orders;
    private final List<Payment> payments;

    public AuditReportContent(Organisation seller, Date dateFrom, Date dateTo, List<Order> orders, List<Payment> payments) {
        this.seller = seller;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.orders = orders;
        this.payments = payments;
    }

    public Organisation getSeller() {
        return seller;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Payment> getPayments() {
        return payments;
    }
    
}
