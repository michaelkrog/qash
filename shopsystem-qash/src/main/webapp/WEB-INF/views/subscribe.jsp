<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Qash - Change plan for Shop</title>
        <meta name="description" content="Changes the plan in use for a specific Shop" />
        <meta name="keywords" content="login, qash"/>
        <jsp:include page="inc/htmlhead.jsp" />
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
                    <fmt:formatNumber var="formattedExampleRevenue" value='10000' currencyCode="${currency}" type='currency'/>
                    <fmt:formatNumber var="formattedExampleFee" value='10' currencyCode="${currency}" type='currency'/>
                    <fmt:formatNumber var="formattedFee" value='${subscriptionFee}' currencyCode="${currency}" type='currency'/>
                    

                    <div style="width:400px;padding-bottom:20px;">
                        <spring:message code="subscribe.text1" /><br/><br/>
                        <spring:message code="subscribe.text2" /><br/><br/>
                        <h2><spring:message code="subscribe.fee.title"/></h2>
                        <spring:message code="subscribe.fee.text" argumentSeparator="|" arguments="${formattedFee}"/> *

                            <br/><br/>

                       
                            <a href="dashboard.htm" class="button-standard"><spring:message code="subscribe.button.no"/></a>&nbsp;&nbsp;<a href="subscribe_do.htm?organisationId=${organisationId}" class="button-standard"><spring:message code="subscribe.button.yes"/></a>
                       

                    </div>

                </p>
            </div>
        </div>
        <!-- /benefits -->

        <jsp:include page="inc/footer.jsp" />


    </body>
</html>

