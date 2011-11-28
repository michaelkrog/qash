<%--
    Document   : index
    Created on : 16-05-2011, 11:20:51
    Author     : michaelzachariassenkrog
--%>
<%@page import="dk.apaq.shopsystem.util.AddressUtil"%>
<%@page import="dk.apaq.shopsystem.entity.Store"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="dk.apaq.shopsystem.service.crud.OrganisationCrud"%>
<%@page import="dk.apaq.shopsystem.service.OrganisationService"%>
<%@page import="dk.apaq.shopsystem.qash.Qash"%>
<%@page import="dk.apaq.shopsystem.entity.Organisation"%>
<%@page import="dk.apaq.shopsystem.service.SystemService"%>
<%@page import="dk.apaq.filter.core.CompareFilter"%>
<%@page import="dk.apaq.crud.Crud"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.Date"%>
<%@page import="org.apache.commons.lang.time.DateUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%

WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(application);
SystemService service = wac.getBean(SystemService.class);
OrganisationCrud orgCrud = service.getOrganisationCrud();

//TODO List organisations
List<String> idlist = orgCrud.listIds();

boolean autoRedirect = "true".equalsIgnoreCase(request.getParameter("autoRedirect"));
int shopCount = idlist.size();


//Wait - if user only has one shop and this is not forced, then redirect to the shop
if(idlist.size() == 1 && autoRedirect) {
    response.sendRedirect("register/id:" + idlist.get(0));
    return;
}

ResourceBundle res = Qash.getResourceBundle(request.getLocale());


DateFormat df = new SimpleDateFormat("yyyyMMdd");
Date since30Days = DateUtils.addDays(new Date(), -30);
String since30DaysString = df.format(since30Days);
%>
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
                <h1><%=res.getString("dashboard.title")%></h1>
            <p>
                <table class="table-minimalistic table-minimalistic-lined" style="width:950px" summary="Shop list">
                    <thead>
                        <tr>
                            <th scope="col"><%=res.getString("dashboard.column.name")%></th>
                            <th scope="col"><%=res.getString("dashboard.column.plan")%></th>
                            <th scope="col"><%=res.getString("dashboard.column.orderUsage")%></th>
                            <th scope="col"><%=res.getString("dashboard.column.inventoryUsage")%></th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                        String noLimit = res.getString("dashboard.noLimit");
                        for(String id : idlist) {
                            Organisation org = orgCrud.read(id);
                            OrganisationService orgService = service.getOrganisationService(org);
                            dk.apaq.filter.Filter dateFilter = new CompareFilter<Date>("dateCreated", since30Days, CompareFilter.CompareType.GreaterOrEqual);
                            int itemCount = orgService.getProducts().listIds().size();
                            int orderCount = orgService.getOrders().listIds(dateFilter, null).size();

                            out.println("<tr><td>");
                            out.println(StringEscapeUtils.escapeHtml(org.getCompanyName()));
                            out.println("</td>");
                            out.println("<td>"/* + org.getServiceLevel()*/);
                            //out.println("&nbsp;<a href=\"change_plan.jsp?shopid=" + shop.getId() + "\" class=\"button-small\">Change</a>");
                            out.println("</td>");
                            out.println("<td>" + orderCount + " <span class=\"light\">(" + noLimit+ ")</span> </td>");
                            out.println("<td>" + itemCount + " <span class=\"light\">(" + noLimit+ ")</span></td>");
                            out.println("<td>");
                            out.println("<a icon=\"hyperlink\" href=\"/admin/id:" + org.getId() + "\">");
                            out.println(res.getString("dashboard.administer"));
                            out.println("</a>");
                            out.println("</td>");
                            //out.println("<td>" + StringEscapeUtils.escapeHtml(org.getCreatedBy()) + "</td>");
                            out.println("</tr>");
                            
                            Crud<String, Store> storeCrud = orgService.getStores();
                            List<Store> storeList = storeCrud.list();
                            if(storeList.isEmpty()) {
                                out.println("<tr class=\"oddrow\"><td colspan=\"5\">-&nbsp;");
                                    out.println("<i>");
                                    out.println(StringEscapeUtils.escapeHtml(res.getString("dashboard.no_registers")));
                                    out.println("</i>");
                                    out.println("</td>");
                                    out.println("</tr>");
                            } else {
                                for(Store store : storeList) {
                                    out.println("<tr class=\"oddrow\"><td colspan=\"5\">-&nbsp;");
                                    out.println("<a icon=\"hyperlink\" href=\"/register/id:" + org.getId() + "\">");
                                    out.println("<b>");
                                    out.println(StringEscapeUtils.escapeHtml(store.getName()));
                                    out.println("</b>");
                                    out.println("(" + res.getString("dashboard.register") + ")</a>");
                                    out.println("</td>");
                                    out.println("</tr>");
                                }
                            }
                            
                        }
                        %>

                    </tbody>
                </table>
                <%=res.getString("dashboard.footnote")%>
                <!--Restrictions are based on the plan chosen for the Shop.--><br/>
                
 
            </p>
            </div>
        </div>
        <!-- /benefits -->

        <jsp:include page="inc/footer.jsp" />


    </body>
</html>

