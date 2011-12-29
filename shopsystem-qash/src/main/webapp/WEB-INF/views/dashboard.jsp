<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Qash - Your account</title>
        <meta name="description" content="The account information you have registered with Qash" />
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
                <h1><spring:message code="dashboard.title"/></h1>
                <p>


                    <table class="table-minimalistic table-minimalistic-lined" style="width:950px" summary="Shop list">
                        <thead>
                            <tr>
                                <th scope="col"><spring:message code="dashboard.column.name"/></th>
                                <th scope="col"><spring:message code="dashboard.column.plan"/></th>
                                <!--th scope="col"><spring:message code="dashboard.column.orderUsage"/></th>
                                <th scope="col"><spring:message code="dashboard.column.inventoryUsage"/></th-->
                                <th scope="col"></th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="organisation" items="${organisations}">
                                <tr>
                                    <td>
                                        ${organisation.companyName}
                                    </td>


                                    <c:choose>
                                        <c:when test="${organisation.subscriber}">
                                            <td><spring:message code="dashboard.plan_free"/></td>    
                                             <!--td>30 <span class="light">(<spring:message code="dashboard.noLimit"/>)</span></td>
                                             <td>30 <span class="light">(<spring:message code="dashboard.noLimit"/>)</span></td-->
                                            <td><a href="subscribe.htm" class="button-standard"><spring:message code="dashboard.subscribe_basic_plan"/></a></td>
                                            <td><a icon="hyperlink" href="/admin/org/${organisation.id}"><spring:message code="dashboard.administer"/></a></td>
                                        </c:when> 
                                        <c:otherwise>
                                            <td><spring:message code="dashboard.plan_basic"/></td>
                                           <!--td>30 <span class="light">(<spring:message code="dashboard.free_limit_orders"/>)</span></td>
                                           <td>30 <span class="light">(<spring:message code="dashboard.free_limit_products"/>)</span></td-->
                                            <td><a href="subscribe.htm" class="button-standard"><spring:message code="dashboard.unsubscribe_basic_plan"/></a></td>
                                            <td><a icon="hyperlink" href="/admin/org/${organisation.id}"><spring:message code="dashboard.administer"/></a></td>
                                        </c:otherwise>
                                    </c:choose>
                                </tr>
                                <c:set var="stores" value="${service.getOrganisationService(organisation).getStores().list()}" scope="request" />

                                <c:choose>
                                    <c:when test="${stores.isEmpty()}">
                                        <tr class="oddrow">
                                            <td colspan="4">
                                                -&nbsp;<i><spring:message code="dashboard.no_registers"/>)</i>
                                            </td>
                                        </tr>

                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="store" items="${service.getOrganisationService(organisation).getStores().list()}"> 

                                            <tr class="oddrow">
                                                <td colspan="4">
                                                    -&nbsp;<a icon="hyperlink" href="/register/org/${organisation.id}/${store.id}"><b>${store.name}</b> (<spring:message code="dashboard.register"/>)</a>
                                                </td>
                                            </tr>
                                        </c:forEach>   
                                    </c:otherwise>
                                </c:choose>

                            </c:forEach>
                            
                        </tbody>
                    </table>
                    <spring:message code="dashboard.footnote"/>
                    <!--Restrictions are based on the plan chosen for the Shop.--><br/>


                </p>
            </div>
        </div>
        <!-- /benefits -->

        <jsp:include page="inc/footer.jsp" />


    </body>
</html>

