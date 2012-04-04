<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Qash - Contact information</title>
        <meta name="description" content="Payment" />
        <meta name="keywords" content="apyment, qash"/>
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

            <div class="in-benefits">
                <h1>Payment</h1>
                <br/>
                <br/>
                <div>
                    <form method="POST" action="${formUrl}">
                        <c:forEach var="entry" items="${formElements.entrySet()}">
                            <input type="hidden" name="${entry.key}" value="${entry.value}"/>
                        </c:forEach>    
                            <button class="button-standard">Betal</button>    
                    </form>    
                    <br/><br/>
                </div>

            </div>
            <div class="clear"></div>
        </div>
        <!-- /benefits -->

        <jsp:include page="inc/footer.jsp" />


    </body>
</html>

