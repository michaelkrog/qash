package dk.apaq.shopsystem.pay.quickpay;

import java.io.UnsupportedEncodingException;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import dk.apaq.shopsystem.pay.PaymentStatus;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;

/**
 *
 * @author krog
 */
public class QuickPayTest {

    public QuickPayTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    private QuickPay quickPay = new QuickPay("qwerty", "12345");

    /**
     * Test of cancel method, of class QuickPay.
     */
    @Test
    public void testCancel() throws IOException {
        System.out.println("cancel");
        String responseBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                            + "<response>"
                            + "<qpstat>000</qpstat>"
                            + "<qpstatmsg>OK</qpstatmsg>"
                            + "</response>";
        HttpResponse response = prepareResponse(200, responseBody);
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.execute(Mockito.any(HttpUriRequest.class))).thenReturn(response);

        quickPay.setHttpClient(mockHttpClient);
        quickPay.cancel("123");

    }

    /**
     * Test of status method, of class QuickPay.
     */
    @Test
    public void testStatus() throws IOException {
        System.out.println("status");
        String responseBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                            + "<response>"
                            + "<qpstat>000</qpstat>"
                            + "<qpstatmsg>OK</qpstatmsg>"
                            + "</response>";
        HttpResponse response = prepareResponse(200, responseBody);
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.execute(Mockito.any(HttpUriRequest.class))).thenReturn(response);

        quickPay.setHttpClient(mockHttpClient);
        PaymentStatus status = quickPay.status("123");

    }

    /**
     * Test of capture method, of class QuickPay.
     */
    @Test
    public void testCapture() throws IOException {
        System.out.println("capture");
        String responseBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                            + "<response>"
                            + "<qpstat>000</qpstat>"
                            + "<qpstatmsg>OK</qpstatmsg>"
                            + "</response>";
        HttpResponse response = prepareResponse(200, responseBody);
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.execute(Mockito.any(HttpUriRequest.class))).thenReturn(response);

        quickPay.setHttpClient(mockHttpClient);
        quickPay.capture(10000, "123");
    }

    /**
     * Test of recurring method, of class QuickPay.
     */
    @Test
    public void testRecurring() throws IOException {
        System.out.println("recurring");
        String responseBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                            + "<response>"
                            + "<qpstat>000</qpstat>"
                            + "<qpstatmsg>OK</qpstatmsg>"
                            + "</response>";
        HttpResponse response = prepareResponse(200, responseBody);
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.execute(Mockito.any(HttpUriRequest.class))).thenReturn(response);

        quickPay.setHttpClient(mockHttpClient);
        quickPay.recurring("121212",10000,"DKK", false, "123");
    }

    /**
     * Test of renew method, of class QuickPay.
     */
    @Test
    public void testRenew() throws IOException {
        System.out.println("renew");
        String responseBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                            + "<response>"
                            + "<qpstat>000</qpstat>"
                            + "<qpstatmsg>OK</qpstatmsg>"
                            + "</response>";
        HttpResponse response = prepareResponse(200, responseBody);
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.execute(Mockito.any(HttpUriRequest.class))).thenReturn(response);

        quickPay.setHttpClient(mockHttpClient);
        quickPay.renew(10000, "123");
    }

    /**
     * Test of refund method, of class QuickPay.
     */
    @Test
    public void testRefund() throws IOException {
        System.out.println("refund");
        String responseBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                            + "<response>"
                            + "<qpstat>000</qpstat>"
                            + "<qpstatmsg>OK</qpstatmsg>"
                            + "</response>";
        HttpResponse response = prepareResponse(200, responseBody);
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.execute(Mockito.any(HttpUriRequest.class))).thenReturn(response);

        quickPay.setHttpClient(mockHttpClient);
        quickPay.refund(10000, "123");
    }


    private HttpResponse prepareResponse(int expectedResponseStatus, String expectedResponseBody) {
        HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), expectedResponseStatus, ""));
        response.setStatusCode(expectedResponseStatus);
        try {
            response.setEntity(new StringEntity(expectedResponseBody));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        return response;
    }
}
