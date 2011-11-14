<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.io.*" %>

<%
// read post from PayPal system and add 'cmd'
            Enumeration en = request.getParameterNames();
            String str = "cmd=_notify-validate";
            while (en.hasMoreElements()) {
                String paramName = (String) en.nextElement();
                String paramValue = request.getParameter(paramName);
                str = str + "&" + paramName + "=" + URLEncoder.encode(paramValue);
            }


            URL u = new URL("https://www.sandbox.paypal.com/cgi-bin/webscr");
            URLConnection uc = u.openConnection();
            uc.setDoOutput(true);
            uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            PrintWriter pw = new PrintWriter(uc.getOutputStream());
            pw.println(str);
            pw.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(uc.getInputStream()));
            String res = in.readLine();
            in.close();

// assign posted variables to local variables
            String itemName = request.getParameter("item_name");
            String itemNumber = request.getParameter("item_number");
            String paymentStatus = request.getParameter("payment_status");
            String paymentAmount = request.getParameter("mc_gross");
            String paymentCurrency = request.getParameter("mc_currency");
            String txnId = request.getParameter("txn_id");
            String receiverEmail = request.getParameter("receiver_email");
            String payerEmail = request.getParameter("payer_email");

            System.out.println("result: " + res);

//check notification validation
            if (res.equals("VERIFIED")) {
                for (Map.Entry entry : (Set<Map.Entry>)request.getParameterMap().entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
// check that paymentStatus=Completed
// check that txnId has not been previously processed
// check that receiverEmail is your Primary PayPal email
// check that paymentAmount/paymentCurrency are correct
// process payment
            } else if (res.equals("INVALID")) {
// log for investigation
            } else {
// error
            }
%>
