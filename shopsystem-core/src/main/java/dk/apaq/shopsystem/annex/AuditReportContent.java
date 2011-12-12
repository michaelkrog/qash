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
        checkNotNull(seller, "seller");
        checkNotNull(dateFrom, "dateFrom");
        checkNotNull(dateTo, "dateTo");
        checkNotNull(orders, "orders");
        checkNotNull(payments, "payments");
        
        this.seller = seller;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.orders = orders;
        this.payments = payments;
    }

    private void checkNotNull(Object o, String name) {
        if(o == null) {
            throw new NullPointerException(name + " must not be null");
        }
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
