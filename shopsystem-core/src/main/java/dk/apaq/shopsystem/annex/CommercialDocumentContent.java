/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.annex;

import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.Store;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author michaelzachariassenkrog
 */
public class CommercialDocumentContent {

    private final Organisation seller;
    private final Order order;
    private final List<Payment> paymentList;

    public CommercialDocumentContent(Organisation seller, Order order, List<Payment> paymentList) {
        this.seller = seller;
        this.order = order;
        this.paymentList = paymentList == null ? new ArrayList<Payment>() : paymentList;
    }

    public Order getOrder() {
        return order;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public Organisation getSeller() {
        return seller;
    }
    
}
