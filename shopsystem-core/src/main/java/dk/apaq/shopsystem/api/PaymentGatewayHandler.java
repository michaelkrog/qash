package dk.apaq.shopsystem.api;

import dk.apaq.shopsystem.entity.Order;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author krog
 */
public interface PaymentGatewayHandler {

    String getOrganisationId(HttpServletRequest request);
    String getOrderId(HttpServletRequest request);
    double getAmount(HttpServletRequest request);
    String getCurrency(HttpServletRequest request);
    
    void validate(HttpServletRequest request, HttpServletResponse response, Order order) throws IOException;
}
