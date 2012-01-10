<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Qash - Contact information</title>
        <meta name="description" content="Login for Qash" />
        <meta name="keywords" content="login, qash"/>
        <jsp:include page="inc/htmlhead.jsp" />
    </head>

    <body onload="document.getElementById('username').focus()">
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

            <div class="in-benefits">
                <h1><spring:message code="terms.title"/></h1>
                <br/>
                <br/>
                <div>
                    
                    <h2><spring:message code="policy.header1"/></h2>
                    <spring:message code="policy.text1"/><br/><br/>

                    <h2><spring:message code="policy.header2"/></h2>
                    <spring:message code="policy.text2"/><br/><br/>

                    <h2><spring:message code="policy.header3"/></h2>
                    <spring:message code="policy.text3"/><br/><br/>

                    <h2><spring:message code="policy.header4"/></h2>
                    <spring:message code="policy.text4"/><br/><br/>


                    <br/><br/>
                </div>

            </div>
            <div class="clear"></div>
        </div>
        </div>
        <!-- /benefits -->

        <jsp:include page="inc/footer.jsp" />


    </body>
</html>

