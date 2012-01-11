<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
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
                <h1><spring:message code="unsubscribe.title"/></h1>
                <br/>
            <p>
                <div style="width:400px;padding-bottom:20px;">
                <spring:message code="unsubscribe.text1"/>
                <br/><br/>
                <spring:message code="unsubscribe.text2"/>
                </div>
                <spring:message code="unsubscribe.text3"/><br/><br/>
                <a href="dashboard.jsp" class="button-standard"><spring:message code="unsubscribe.button.no"/></a>
                &nbsp;&nbsp;
                <a href="unsubscribe_do.htm?organisationId=${organisationId}" class="button-standard"><spring:message code="unsubscribe.button.yes"/></a>
                
                
            </p>
            </div>
        </div>
        <!-- /benefits -->

        <jsp:include page="inc/footer.jsp" />


    </body>
</html>

