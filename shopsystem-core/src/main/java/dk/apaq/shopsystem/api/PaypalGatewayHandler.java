package dk.apaq.shopsystem.api;

import dk.apaq.shopsystem.entity.Order;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author krog
 */
public class PaypalGatewayHandler implements PaymentGatewayHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PaypalGatewayHandler.class);
    private String gatewayUrl;

    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }
    
    @Override
    public String getOrganisationId(HttpServletRequest request) {
        return request.getParameter("custom");
    }

    @Override
    public String getOrderId(HttpServletRequest request) {
        return request.getParameter("invoice");
    }

    @Override
    public double getAmount(HttpServletRequest request) {
        return Double.parseDouble(request.getParameter("mc_gross"));
    }

    @Override
    public String getCurrency(HttpServletRequest request) {
        return request.getParameter("mc_currency");
    }

    @Override
    public void validate(HttpServletRequest request, HttpServletResponse response, Order order) throws IOException {
        LOG.debug("Got callback from Paypal Gateway");

        Map<String, String> map = request.getParameterMap();
        String receiverEmail = map.get("receiver_email");
        String paymentCurrency = getCurrency(request);

        if (!receiverEmail.equals(order.getOrganisation().getMerchantId())) {
            throw new InvalidRequestException("Receiver email not correct.[order=" + order.getOrganisation().getMerchantId() + ",request=" + receiverEmail + "]");
        }

        if (!paymentCurrency.equals(order.getCurrency())) {
            throw new InvalidRequestException("Currency from payment not same as currency on order.[order=" + order.getCurrency() + ",request=" + paymentCurrency + "]");
        }

        // read post from PayPal system and add 'cmd'
        Set<String> keys = map.keySet();

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(gatewayUrl);

        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("cmd", new StringBody("_notify-validate"));


        StringBuilder sb = new StringBuilder();
        sb.append("cmd=_notify-validate");
        for (String key : keys) {
            String paramName = key;
            String paramValue = map.get(key);
            reqEntity.addPart(paramName, new StringBody(paramValue));
        }

        LOG.debug("Posting back to paypal to get data verified.[data=" + sb.toString() + "]");

        // post back to PayPal system to validate
        httpPost.setEntity(reqEntity);
        HttpResponse paypalResponse = httpclient.execute(httpPost);


        BufferedReader in = new BufferedReader(new InputStreamReader(paypalResponse.getEntity().getContent()));
        String res = in.readLine();
        in.close();

        // assign posted variables to local variables
        String uniqueId = map.get("custom");
        String paymentStatus = map.get("payment_status");

        //check notification validation
        if (res.equals("VERIFIED") && "Completed".equals(paymentStatus)) {
            LOG.debug("Data from paypal verified. Payment has succeded.");
        } else if (res.equals("INVALID")) {
            LOG.debug("Data from paypal was invalid. Payment has failed.");
            throw new RuntimeException("Data from paypal was invalid. Payment has failed.");
        } else {
            LOG.debug("Could not read response from paypal. Payment has failed.");
            throw new RuntimeException("Could not read response from paypal. Payment has failed.");
        }
    }
}
