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

                    <fmt:formatNumber var="formattedExampleRevenue" value='10000' currencyCode="${feeCurrency}" type='currency'/>
                    <fmt:formatNumber var="formattedExampleFee" value='20' currencyCode="${feeCurrency}" type='currency'/>
                    <fmt:formatNumber var="formattedFee" maxFractionDigits="3" value='0.002' type='percent'/>
                    <fmt:formatNumber var="formattedStartFee" value='${startupFee}' currencyCode="${feeCurrency}" type='currency'/>


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
                                </td>
                            </tr>
                        </table>
                        <br/><br/>
	
                        <%
                        StringBuilder md5Builder = new StringBuilder();
                        %>
                        <form action="https://secure.quickpay.dk/form/" method="post">
                            <input type="hidden" name="protocol" value="4" />
                            <%md5Builder.append("4");%>
                            <input type="hidden" name="msgtype" value="authorize" />
                            <%md5Builder.append("authorize");%>
                            <input type="hidden" name="merchant" value="29331847" />
                            <%md5Builder.append("29331847");%>
                            <input type="hidden" name="language" value="da" />
                            <%md5Builder.append("da");%>
                            <input type="hidden" name="ordernumber" value="123" />
                            <%md5Builder.append("123");%>
                            <input type="hidden" name="amount" value="100" />
                            <%md5Builder.append("100");%>
                            <input type="hidden" name="currency" value="DKK" />
                            <%md5Builder.append("DKK");%>
                            <input type="hidden" name="continueurl" value="http://quickpay.net/features/payment-window/ok.php" />
                            <%md5Builder.append("http://quickpay.net/features/payment-window/ok.php");%>
                            <input type="hidden" name="cancelurl" value="http://quickpay.net/features/payment-window/error.php" />
                            <%md5Builder.append("http://quickpay.net/features/payment-window/error.php");%>
                            <input type="hidden" name="callbackurl" value="http://quickpay.net/features/payment-window/callback.php" />
                            <%md5Builder.append("http://quickpay.net/features/payment-window/callback.php");%>
                            <input type="hidden" name="autocapture" value="0" />
                            <%md5Builder.append("0");%>
                            <input type="hidden" name="cardtypelock" value="" />
                            <%md5Builder.append("");%>
                            <input type="hidden" name="splitpayment" value="1" />
                            <%md5Builder.append("1");%>
                            <%md5Builder.append("9N7D16ri3EkAeH482fvUtZ67Md29W9LPbY1hxgQ3c34l54w5GImKFp1y636J725T");%>
                            <input type="hidden" name="md5check" value="<%=DigestUtils.md5Hex(md5Builder.toString())%>" />
                            <input type="submit" value="Betal"/>
                        </form>
                            <a href="dashboard.htm" class="button-standard"><spring:message code="subscribe.button.no"/></a>&nbsp;&nbsp;<button type="submit" class="button-standard"><spring:message code="subscribe.button.yes"/></button>
                        
                    </div>

                    * <spring:message code="subscribe.footnote" argumentSeparator="|" arguments="${formattedExampleRevenue}|${formattedFee}|${formattedExampleFee}"/>

                </p>
            </div>
        </div>
        <!-- /benefits -->

        <jsp:include page="inc/footer.jsp" />


    </body>
</html>

