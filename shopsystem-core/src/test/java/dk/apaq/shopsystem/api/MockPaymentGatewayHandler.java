package dk.apaq.shopsystem.api;

import dk.apaq.shopsystem.entity.Order;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author krog
 */
public class MockPaymentGatewayHandler implements PaymentGatewayHandler {

    @Override
    public String getOrganisationId(HttpServletRequest request) {
        return request.getParameter("orgid");
    }

    @Override
    public String getOrderId(HttpServletRequest request) {
        return request.getParameter("orderid");
    }

    @Override
    public double getAmount(HttpServletRequest request) {
        return Double.parseDouble(request.getParameter("amount"));
    }

    @Override
    public String getCurrency(HttpServletRequest request) {
        return request.getParameter("currency");
    }

    @Override
    public void validate(HttpServletRequest request, HttpServletResponse response, Order order) throws IOException {
        
    }
    
}
