<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Qash - Prices</title>
        <meta name="description" content="The list of prices for Qash." />
        <meta name="keywords" content="prices, subscription, qash"/>
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

            <div class="in-benefits" style="padding-bottom:150px">
                <h1><spring:message code="prices.title"/></h1>
            <br/>
            <ul>
                    <fmt:formatNumber var="formattedFee" maxFractionDigits="3" value='0.001' type='percent'/>
                   
                    <!-- benefit item -->
                    <li style="width:430px; height:105px">
                        <span class="benefits-icon"><img src="images/prices/free.png" alt="Login" width="70" height="70" /></span>
                        <div class="benefits-text" style="width:315px;padding-right:45px;">
                            <h2><spring:message code="prices.free.title"/></h2>
                            <spring:message code="prices.free.text"/>
                        </div>
                    </li>
                    
                    <!-- benefit item -->
                    <li style="width:430px; height:105px">
                        <span class="benefits-icon"><img src="images/prices/payg.png" alt="Login" width="70" height="70" /></span>
                        <div class="benefits-text" style="width:315px;padding-right:45px;">
                        <h2><spring:message code="prices.paid.title"/></h2>
                        <spring:message code="prices.paid.text" argumentSeparator="|" arguments="${formattedFee}"/>
                        </div>
                    </li>
            </ul>
            
            </div>
        </div>
        <!-- /benefits -->

        <jsp:include page="inc/footer.jsp" />


    </body>
</html>

