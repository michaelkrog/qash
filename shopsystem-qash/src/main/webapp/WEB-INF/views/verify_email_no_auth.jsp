<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Qash - Email verified</title>
        <meta name="description" content="Email verified." />
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

        <div id="in-benefits-container" >

            <div class="in-benefits" style="padding-bottom:44px">
                <h1>Unable to verify email address</h1>
            <p>
                You are logged in with another user. Click the link again when you have logged out. <a href="/logout">Log out</a>.
            </p>
            </div>
        </div>

        <jsp:include page="inc/footer.jsp" />

    </body>
</html>

