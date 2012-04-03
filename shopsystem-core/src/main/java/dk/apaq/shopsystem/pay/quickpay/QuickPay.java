package dk.apaq.shopsystem.pay.quickpay;

import dk.apaq.shopsystem.pay.PaymentException;
import dk.apaq.shopsystem.pay.PaymentGateway;
import dk.apaq.shopsystem.pay.PaymentInformation;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author krog
 */
public class QuickPay implements PaymentGateway {

    private static final Logger LOG = LoggerFactory.getLogger(QuickPay.class);

    private String merchantId = null;
    private String secretWord = null;
    private String url = null;
    private boolean testMode;
    private org.apache.http.client.HttpClient httpClient;
    private final static String protocolVersion = "4";


    public QuickPay(String merchantId, String secretWord){
        this.merchantId = merchantId;
        this.secretWord = secretWord;
        this.url = "https://secure.quickpay.dk/api";
    }

    public QuickPay(String merchantId, String secretWord, String url){
        this.merchantId = merchantId;
        this.secretWord = secretWord;
        this.url = url;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
    
    private HttpClient getHttpClient() {
        if(httpClient == null) {
            httpClient = new DefaultHttpClient();
        }
        return httpClient;
    }
    
    @Override
    public void cancel(String transactionId) {
        try {
            LOG.debug("Cancelling transaction [transactionId={}]", transactionId);
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "cancel"));
            nvps.add(md5.getBasicNameValuePair("merchant", merchantId));
            nvps.add(md5.getBasicNameValuePair("transaction", transactionId));
            
            if(testMode) {
                nvps.add(md5.getBasicNameValuePair("testmode", "1"));
            }
            
            md5.add(secretWord);
            nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            post.getEntity().writeTo(System.out);

            HttpResponse response = getHttpClient().execute(post);
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
            entity.writeTo(ba);
            String result = new String(ba.toByteArray(), 0, ba.size());
            checkQuickpayResult(new QuickPayResult(result));
            
        } catch (IOException ex) {
            throw new PaymentException("Unable to cancel payment.", ex);
        } 
        
    }

    public PaymentInformation getPaymentInformation(String transactionId) {
        try {
            LOG.debug("Retrieving information about transaction [transactionId={}]", transactionId);
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "status"));
            nvps.add(md5.getBasicNameValuePair("merchant", merchantId));
            nvps.add(md5.getBasicNameValuePair("transaction", transactionId));
            
            if(testMode) {
                nvps.add(md5.getBasicNameValuePair("testmode", "1"));
            }
            
            md5.add(secretWord);
            nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse response = getHttpClient().execute(post);
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
            entity.writeTo(ba);
            String result = new String(ba.toByteArray(), 0, ba.size());
            
            QuickPayResult qpresult = new QuickPayResult(result);
            //TODO
            return null;
        } catch (IOException ex) {
            throw new PaymentException("Unable to get status for payment.", ex);
        }
    }



    @Override
    public void capture(long amountInCents, String transactionId) {
        try {
            LOG.debug("Capturing money for transaction [transactionId={}; amountInCents={}]", new Object[]{transactionId, amountInCents});
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "capture"));
            nvps.add(md5.getBasicNameValuePair("merchant", merchantId));
            nvps.add(md5.getBasicNameValuePair("amount", "" + amountInCents));
            nvps.add(md5.getBasicNameValuePair("finalize", "1"));
            nvps.add(md5.getBasicNameValuePair("transaction", transactionId));
            if(testMode) {
                nvps.add(md5.getBasicNameValuePair("testmode", "1"));
            }
            
            md5.add(secretWord);
            nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse response = getHttpClient().execute(post);
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
            entity.writeTo(ba);
            String result = new String(ba.toByteArray(), 0, ba.size());
            checkQuickpayResult(new QuickPayResult(result));
        } catch (IOException ex) {
            throw new PaymentException("Unable to capture payment.", ex);
        }
    }

    public void recurring(String orderNumber, long amountInCents, String currency, boolean autocapture, String transactionId) {
        try {
            LOG.debug("Recurrings authorization for transaction [transactionId={}; orderNumber={}; amountInCents={}; currency={}; autoCapture={}]", 
                                                                    new Object[]{transactionId, orderNumber, amountInCents, currency, autocapture});
            
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "capture"));
            nvps.add(md5.getBasicNameValuePair("merchant", merchantId));
            nvps.add(md5.getBasicNameValuePair("ordernumber", orderNumber));
            nvps.add(md5.getBasicNameValuePair("amount", "" + amountInCents));
            nvps.add(md5.getBasicNameValuePair("currency", currency));
            nvps.add(md5.getBasicNameValuePair("autocapture", autocapture ? "1" : "0"));
            nvps.add(md5.getBasicNameValuePair("transaction", transactionId));
            
            if(testMode) {
                nvps.add(md5.getBasicNameValuePair("testmode", "1"));
            }
            
            md5.add(secretWord);
            nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse response = getHttpClient().execute(post);
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
            entity.writeTo(ba);
            String result = new String(ba.toByteArray(), 0, ba.size());
            checkQuickpayResult(new QuickPayResult(result));
        } catch (IOException ex) {
            throw new PaymentException("Unable to create recurring payment.", ex);
        }
    }

    public void renew(long amountInCents, String transactionId) {
        try {
            LOG.debug("Renewing transaction [transaction={}; amountInCents={}]", new Object[]{transactionId, amountInCents});
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "renew"));
            nvps.add(md5.getBasicNameValuePair("merchant", merchantId));
            nvps.add(md5.getBasicNameValuePair("transaction", transactionId));
            
            if(testMode) {
                nvps.add(md5.getBasicNameValuePair("testmode", "1"));
            }
            
            md5.add(secretWord);
            nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse response = getHttpClient().execute(post);
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
            entity.writeTo(ba);
            String result = new String(ba.toByteArray(), 0, ba.size());
            checkQuickpayResult(new QuickPayResult(result));
        } catch (IOException ex) {
            throw new PaymentException("Unable to renew payment.", ex);
        }
    }



    public void refund(long amountInCents, String transactionId) {
        try {
            LOG.debug("Refunding transaction [transaction={}; amountInCents={}]", new Object[]{transactionId, amountInCents});
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "refund"));
            nvps.add(md5.getBasicNameValuePair("merchant", merchantId));
            nvps.add(md5.getBasicNameValuePair("amount", "" + amountInCents));
            nvps.add(md5.getBasicNameValuePair("transaction", transactionId));
            
            if(testMode) {
                nvps.add(md5.getBasicNameValuePair("testmode", "1"));
            }
            
            md5.add(secretWord);
            nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse response = getHttpClient().execute(post);
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
            entity.writeTo(ba);
            String result = new String(ba.toByteArray(), 0, ba.size());
            checkQuickpayResult(new QuickPayResult(result));
        } catch (IOException ex) {
            throw new PaymentException("Unable to refund payment.", ex);
        }
    }

    /*public static void main(String[] args) throws Exception {
        QuickPay qp = new QuickPay("29331847", "Q9N7D16ri3EkAeH482fvUtZ67Md29W9LPbY1hxgQ3c34l54w5GImKFp1y636J725T");
        qp.capture(12000, "33942345");
        
    }*/

    private void checkQuickpayResult(QuickPayResult result) {
        String status = result.getParameter("qpstat");
        String statusMessage = result.getParameter("qpstatmsg");
        
        if("000".equals(status)) {
            return;
        } else if("001".equals(status)) {
            throw new PaymentException("001: " + statusMessage + ". Rejected by acquirer.");
        } else if("002".equals(status)) {
            throw new PaymentException("002: " + statusMessage + ". Communication error.");
        } else if("003".equals(status)) {
            throw new PaymentException("003: " + statusMessage + ". Card expired.");
        } else if("004".equals(status)) {
            throw new PaymentException("004: " + statusMessage + ". Transition is not allowed for transaction current state.");
        } else if("005".equals(status)) {
            throw new PaymentException("005: " + statusMessage + ". Authorization is expired.");
        } else if("006".equals(status)) {
            throw new PaymentException("006: " + statusMessage + ". Error reported by acquirer.");
        } else if("007".equals(status)) {
            throw new PaymentException("007: " + statusMessage + ". Error reported by QuickPay.");
        } else if("008".equals(status)) {
            throw new PaymentException("008: " + statusMessage + ". Error in request data.");
        } else if("009".equals(status)) {
            throw new PaymentException("009: " + statusMessage + ". Payment aborted by shopper.");
        } else {
            throw new PaymentException("Unknown status. [status=" + status + "]");
        }

    }
    
    /**
     * md5check: 1baff8b58c67d036b2d3a5a158bf33df, hash: 98891297330cb31793562898d2e2b3ce
     */
}

