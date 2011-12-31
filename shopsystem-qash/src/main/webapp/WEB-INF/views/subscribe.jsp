<%@page import="java.text.NumberFormat"%>
<%@page import="dk.apaq.shopsystem.entity.Order"%>
<%@page import="org.apache.commons.codec.digest.DigestUtils"%>
<%@page import="dk.apaq.shopsystem.pay.quickpay.QuickPayMd5SumPrinter"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Qash - Change plan for Shop</title>
        <meta name="description" content="Changes the plan in use for a specific Shop" />
        <meta name="keywords" content="login, qash"/>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="content-language" content="en"/>
        <meta name="google-site-verification" content="Dh7WdAlkzeXUeckVsd2uAAunO6sKnBTFBAYODINwz54" />
        <link rel="shortcut icon" href="favicon.ico"/>
        <link rel="stylesheet" type="text/css" href="css/reset.css"/>
        <link rel="stylesheet" type="text/css" href="css/layout.css"/>
        <link rel="stylesheet" type="text/css" href="css/lightbox.css"/>
        <link  href="http://fonts.googleapis.com/css?family=Open+Sans:300,300italic,400,400italic,600,600italic,700,700italic,800,800italic" rel="stylesheet" type="text/css" />

        <!--[if IE 6]>
        <link rel="stylesheet" type="text/css" href="css/ie6.css" />
        <![endif]-->
        <!--[if IE 7]>
        <link rel="stylesheet" type="text/css" href="css/ie7.css" />
        <![endif]-->


        <script type="text/javascript" src="lib/jquery-core.js"></script>
        <!--[if IE 6]>
        <script type="text/javascript" src="lib/jquery-pngfix.js"></script>
        <script type="text/javascript" src="lib/jquery-config-ie6.js"></script>
        <![endif]-->
        <script type="text/javascript" src="lib/jquery-cycle.js"></script>
        <script type="text/javascript" src="lib/jquery-form.js"></script>
        <script type="text/javascript" src="lib/jquery-lightbox.js"></script>
        <script type="text/javascript" src="lib/jquery-config.js"></script>
    </head>

    <body>
        <!-- splash -->
        <div id="in-splash-container-small">
            <div class="in-splash">

            </div>
        </div>
        <!-- /splash -->

        <jsp:include page="inc/header.jsp" />
        <jsp:include page="inc/navigator.jsp" />


        <!-- benefits -->
        <div id="in-benefits-container" >

            <div class="in-benefits" style="padding-bottom:44px">
                <h1><spring:message code="subscribe.title"/></h1>
                <br/>
                <p>
                    ${order.currency}
                    <fmt:formatNumber var="formattedExampleRevenue" value='10000' currencyCode="${order.currency}" type='currency'/>
                    <fmt:formatNumber var="formattedExampleFee" value='20' currencyCode="${order.currency}" type='currency'/>
                    <fmt:formatNumber var="formattedFee" maxFractionDigits="3" value='0.002' type='percent'/>
                    <fmt:formatNumber var="formattedStartFee" value='${order.total}' currencyCode="${order.currency}" type='currency'/>
                    <fmt:formatNumber var="formattedStartFeeTax" value='${order.totalTax}' currencyCode="${order.currency}" type='currency'/>
                    <fmt:formatNumber var="formattedStartFeeTotal" value='${order.totalWithTax}' currencyCode="${order.currency}" type='currency'/>


                    <div style="width:400px;padding-bottom:20px;">
                        <spring:message code="subscribe.text1" argumentSeparator="|" arguments="${formattedStartFee}"/><br/><br/>
                        <h2><spring:message code="subscribe.example.title"/></h2>
                        <table>
                            <tr>
                                <td><b>
                                        <spring:message code="subscribe.example.fee.title"/>:
                                    </b>
                                </td>
                                <td>
                                    <spring:message code="subscribe.example.fee.text" argumentSeparator="|" arguments="${formattedFee}"/> *
                                </td>
                            </tr>
                            <tr>
                                <td><b>
                                        <spring:message code="subscribe.example.startfee.title"/>:
                                    </b>
                                </td>
                                <td>
                                    <spring:message code="subscribe.example.startfee.text" argumentSeparator="|" arguments="${formattedStartFee}"/>
                                    <c:if test="${order.getTotalTax() > 0}"><spring:message code="subscribe.example.startfee.taxtext" argumentSeparator="|" arguments="${formattedStartFeeTotal}"/></c:if>
                                </td>
                            </tr>
                        </table>
                        <br/><br/>
	
                        <%
                        //TODO Should not have Java code here and not be bound to Quickpay
                        QuickPayMd5SumPrinter md5SumPrinter = new QuickPayMd5SumPrinter();
                        Order order = (Order) request.getAttribute("order");
                        
                        NumberFormat orderNumberFormatter = NumberFormat.getIntegerInstance();
                        orderNumberFormatter.setMinimumIntegerDigits(4);
                        orderNumberFormatter.setMaximumIntegerDigits(20);
                        orderNumberFormatter.setGroupingUsed(false);
                        %>
                        <form action="https://secure.quickpay.dk/form/" method="post">
                            <%=md5SumPrinter.printHtmlHidden("protocol", "4")%>
                            <%=md5SumPrinter.printHtmlHidden("msgtype", "authorize")%>
                            <%=md5SumPrinter.printHtmlHidden("merchant", "29331847")%>
                            <%=md5SumPrinter.printHtmlHidden("language", "da")%>
                            <%=md5SumPrinter.printHtmlHidden("ordernumber", orderNumberFormatter.format(order.getNumber()))%>
                            <%=md5SumPrinter.printHtmlHidden("amount", Integer.toString((int)(order.getTotalWithTax()*100)))%>
                            <%=md5SumPrinter.printHtmlHidden("currency", order.getCurrency())%>
                            <%=md5SumPrinter.printHtmlHidden("continueurl", "http://quickpay.net/features/payment-window/ok.php")%>
                            <%=md5SumPrinter.printHtmlHidden("cancelurl", "http://quickpay.net/features/payment-window/ok.php")%>
                            <%=md5SumPrinter.printHtmlHidden("callbackurl", "http://quickpay.net/features/payment-window/ok.php")%>
                            <%=md5SumPrinter.printHtmlHidden("autocapture", "0")%>
                            <%=md5SumPrinter.printHtmlHidden("cardtypelock", "")%>
                            <%=md5SumPrinter.printHtmlHidden("splitpayment", "0")%>
                            <%md5SumPrinter.add("2254zV7gN9nK96642q1U79b89euRQ5I1W5l8Bj8dF73vrCtyw36xD5YGsZHapJ1S");%>
                            
                            <input type="hidden" name="md5check" value="<%=md5SumPrinter.getMD5Result()%>" />
                            <a href="dashboard.htm" class="button-standard"><spring:message code="subscribe.button.no"/></a>&nbsp;&nbsp;<button type="submit" class="button-standard"><spring:message code="subscribe.button.yes"/></button>
                        </form>
                        
                    </div>

                    * <spring:message code="subscribe.footnote" argumentSeparator="|" arguments="${formattedExampleRevenue}|${formattedFee}|${formattedExampleFee}"/>

                </p>
            </div>
        </div>
        <!-- /benefits -->

        <jsp:include page="inc/footer.jsp" />


    </body>
</html>

