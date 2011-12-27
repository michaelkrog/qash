package dk.apaq.shopsystem.pay.quickpay;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
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
public class QuickPay {

    private static final Logger log = LoggerFactory.getLogger(QuickPay.class);

    private String pbsShopID = null;
    private String secretWord = null;
    private String url = null;
    private final static String protocolVersion = "4";


    public QuickPay(String pbsShopID, String secretWord){
        this.pbsShopID = pbsShopID;
        this.secretWord = secretWord;
        this.url = "https://secure.quickpay.dk/api";
    }

    public QuickPay(String pbsShopID, String secretWord, String url){
        this.pbsShopID = pbsShopID;
        this.secretWord = secretWord;
        this.url = url;
    }

    public QuickPayResult cancel(String transactionID) throws Exception {
        QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
        org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
        nvps.add(md5.getBasicNameValuePair("msgtype", "cancel"));
        nvps.add(md5.getBasicNameValuePair("merchant", pbsShopID));
        nvps.add(md5.getBasicNameValuePair("transaction", transactionID));
        md5.add(secretWord);
        nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
        post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        post.getEntity().writeTo(System.out);

        HttpResponse response = httpclient.execute(post);
        HttpEntity entity = response.getEntity();
        ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
        entity.writeTo(ba);
        String result = new String(ba.toByteArray(), 0, ba.size());
        return new QuickPayResult(result);
    }

    public QuickPayResult status(String transactionID) throws Exception {
        QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
//        HttpPost post = new HttpPost("http://www.less-is-more.dk/callback.jsp");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
        nvps.add(md5.getBasicNameValuePair("msgtype", "status"));
        nvps.add(md5.getBasicNameValuePair("merchant", pbsShopID));
        nvps.add(md5.getBasicNameValuePair("transaction", transactionID));
        md5.add(secretWord);
        nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
        post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        HttpResponse response = httpclient.execute(post);
        HttpEntity entity = response.getEntity();
        ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
        entity.writeTo(ba);
        String result = new String(ba.toByteArray(), 0, ba.size());
        return new QuickPayResult(result);
    }



    public QuickPayResult capture(long amountInCents, String transactionID) throws Exception {
        QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
        nvps.add(md5.getBasicNameValuePair("msgtype", "capture"));
        nvps.add(md5.getBasicNameValuePair("merchant", pbsShopID));
        nvps.add(md5.getBasicNameValuePair("amount", "" + amountInCents));
        nvps.add(md5.getBasicNameValuePair("finalize", "1"));
        nvps.add(md5.getBasicNameValuePair("transaction", transactionID));
        md5.add(secretWord);
        nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
        post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        HttpResponse response = httpclient.execute(post);
        HttpEntity entity = response.getEntity();
        ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
        entity.writeTo(ba);
        String result = new String(ba.toByteArray(), 0, ba.size());
        return new QuickPayResult(result);
    }

    public QuickPayResult recurring(String orderNumber, long amountInCents, String currency, boolean autocapture, String transactionID) throws Exception {
        QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
        nvps.add(md5.getBasicNameValuePair("msgtype", "capture"));
        nvps.add(md5.getBasicNameValuePair("merchant", pbsShopID));
        nvps.add(md5.getBasicNameValuePair("ordernumber", orderNumber));
        nvps.add(md5.getBasicNameValuePair("amount", "" + amountInCents));
        nvps.add(md5.getBasicNameValuePair("currency", currency));
        nvps.add(md5.getBasicNameValuePair("autocapture", autocapture ? "1" : "0"));
        nvps.add(md5.getBasicNameValuePair("transaction", transactionID));
        md5.add(secretWord);
        nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
        post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        HttpResponse response = httpclient.execute(post);
        HttpEntity entity = response.getEntity();
        ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
        entity.writeTo(ba);
        String result = new String(ba.toByteArray(), 0, ba.size());
        return new QuickPayResult(result);
    }

    public QuickPayResult renew(long amountInCents, String transactionID) throws Exception {
        QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
        nvps.add(md5.getBasicNameValuePair("msgtype", "renew"));
        nvps.add(md5.getBasicNameValuePair("merchant", pbsShopID));
        nvps.add(md5.getBasicNameValuePair("transaction", transactionID));
        md5.add(secretWord);
        nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
        post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        HttpResponse response = httpclient.execute(post);
        HttpEntity entity = response.getEntity();
        ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
        entity.writeTo(ba);
        String result = new String(ba.toByteArray(), 0, ba.size());
        return new QuickPayResult(result);
    }



    public QuickPayResult refund(long amountInCents, String transactionID) throws Exception {
        QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
        nvps.add(md5.getBasicNameValuePair("msgtype", "refund"));
        nvps.add(md5.getBasicNameValuePair("merchant", pbsShopID));
        nvps.add(md5.getBasicNameValuePair("amount", "" + amountInCents));
        nvps.add(md5.getBasicNameValuePair("transaction", transactionID));
        md5.add(secretWord);
        nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
        post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        HttpResponse response = httpclient.execute(post);
        HttpEntity entity = response.getEntity();
        ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
        entity.writeTo(ba);
        String result = new String(ba.toByteArray(), 0, ba.size());
        return new QuickPayResult(result);
    }

    public static void main(String[] args) throws Exception {
        QuickPay qp = new QuickPay("32777481", "XdRc8PyF12799z8w4ZE3mUMqHLD456jkpSb71K67B6is46NY5527CV4e1tQ28Irh");
        qp.capture(0, "33942345");
    }


}

