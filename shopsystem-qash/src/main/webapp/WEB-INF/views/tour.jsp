<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Qash - The tour</title>
        <meta name="description" content="A tour through Qash - The online Cash register" />
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
                <h1>A tour through Qash</h1>
            <br/>
            <br/>
            <object width="950" height="560">
                <param name="movie" value="http://www.youtube.com/v/4sJQxlZfooA?fs=1&amp;hl=da_DK"></param>
                <param name="allowFullScreen" value="true"></param>
                <param name="allowscriptaccess" value="always"></param>
                <embed src="http://www.youtube.com/v/4sJQxlZfooA?fs=1&amp;hl=da_DK" type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true" width="950" height="560"></embed>
            </object>
            </div>
        </div>
        <!-- /benefits -->

        <jsp:include page="inc/footer.jsp" />


    </body>
</html>

