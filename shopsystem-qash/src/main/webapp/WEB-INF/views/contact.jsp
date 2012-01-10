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
                <h1><spring:message code="contact.title"/></h1>
            <br/>
            <br/>
            <ul>

                    <!-- benefit item -->
                    <li style="width:430px">
                        <span class="benefits-icon"><img src="images/contact/home.png" alt="Address" width="70" height="70" /></span>
                        <div class="benefits-text" style="width:315px;padding-right:45px;">
                            <h2><spring:message code="contact.address.title"/></h2>
                            <p>
                                Apaq<br />
                                Stovringparken 10<br />
                                DK-9530 Stovring<br />
                                <i>Scandiavian Denmark</i>
                            </p>

                        </div>
                    </li>
                    <!-- /benefit item -->

                    <li style="width:430px" class="endbenefits">
                        <span class="benefits-icon"><img src="images/contact/email.png" alt="Direct contact" width="70" height="70" /></span>
                        <div class="benefits-text" style="width:315px;padding-right:45px">
                            <h2><spring:message code="contact.direct.title"/></h2>
                            <p>
                                <table>
                                    <tr><td><spring:message code="contact.direct.sales.title"/>:</td><td><spring:message code="contact.direct.sales.text"/></td></tr>
                                    <tr><td><spring:message code="contact.direct.support.title"/>:</td><td><spring:message code="contact.direct.support.text"/></td></tr>

                                </table>
                            </p>
                        </div>
                    </li>




                </ul>
                <div class="clear"></div>
            </div>
        </div>
        <!-- /benefits -->

        <jsp:include page="inc/footer.jsp" />


    </body>
</html>

