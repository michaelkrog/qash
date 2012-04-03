<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Qash - Your account</title>
        <meta name="description" content="The account information you have registered with Qash" />
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
                <h1><spring:message code="dashboard.title"/></h1>
                <p>
                    
                    <c:if test="${!service.hasUserVerifiedEmail(user.name)}">
                        <div class="information"><h2>You're email address has not been verified.</h2>Without a valid email address you may not recieve important emails like payment notifications, password information etc. <a href="/verify_email_send.htm">Send verification email</a></div>
                    </c:if>
                    <table class="table-minimalistic table-minimalistic-lined" style="width:950px" summary="Shop list">
                        <thead>
                            <tr>
                                <th scope="col"><spring:message code="dashboard.column.name"/></th>
                                <th scope="col"><spring:message code="dashboard.column.plan"/></th>
                                <th scope="col"><spring:message code="dashboard.column.fee"/></th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="organisation" items="${organisations}">
                                <tr>
                                    <td>
                                        ${organisation.companyName}
                                    </td>

                                    <c:set var="orderCount" value="${service.getOrganisationService(organisation).getOrders().listIds(oneMonthFilter, null).size()}" scope="request" />
                                    <c:choose>
                                        <c:when test="${orderCount<10}">
                                            <c:set var="ordersLeft" value="${10-orderCount}" scope="request" />

                                        </c:when>

                                        <c:otherwise>
                                            <c:set var="ordersLeft" value="0" scope="request" />

                                        </c:otherwise>
                                        
                                    </c:choose>
                                    

                                    <c:choose>
                                        <c:when test="${organisation.subscriber}">
                                            <c:set var="currency" value="${subscription.getPaymentCurrencyForOrganisation(organisation)}" scope="request" />
                                            <td><spring:message code="dashboard.unlimited_access"/></td>    
                                            <td><fmt:formatNumber value='${subscription.getSubscriptionFee(currency)}' currencyCode="${currency}" type='currency'/></td>    
                                            <td><a href="unsubscribe.htm?organisationId=${organisation.id}" class="button-standard"><spring:message code="dashboard.unsubscribe_basic_plan"/></a></td>
                                        </c:when> 
                                        <c:otherwise>
                                            
                                            <td><spring:message code="dashboard.limited_access"/> (<spring:message code="dashboard.free_limit_orders" arguments="${ordersLeft}"/>)*</td>
                                            <td><spring:message code="dashboard.free"/></td>    
                                            <td><a href="subscribe.htm?organisationId=${organisation.id}" class="button-standard"><spring:message code="dashboard.subscribe_basic_plan"/></a></td>
                                        </c:otherwise>
                                    </c:choose>
                                </tr>
                                <c:set var="stores" value="${service.getOrganisationService(organisation).getStores().list()}" scope="request" />

                                <tr class="oddrow">
                                                <td colspan="6">
                                                    <a icon="hyperlink" href="/admin/org/${organisation.id}"><b><spring:message code="dashboard.administration"/></b>&nbsp;(<spring:message code="dashboard.backoffice"/>)</a>&nbsp;
                                                    
                                        <c:forEach var="store" items="${service.getOrganisationService(organisation).getStores().list()}"> 

                                            |&nbsp;<a icon="hyperlink" href="/register/org/${organisation.id}/${store.id}"><b>${store.name}</b> (<spring:message code="dashboard.register"/>)</a>

                                        </c:forEach>   
                                                </td>
                                </tr>
                            </c:forEach>

                        </tbody>
                    </table>
                    <spring:message code="dashboard.footnote"/>
                   <br/>


                </p>
            </div>
        </div>
        <!-- /benefits -->

        <jsp:include page="inc/footer.jsp" />


    </body>
</html>

