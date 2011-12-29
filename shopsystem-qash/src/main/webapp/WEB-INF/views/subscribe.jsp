<%@page import="dk.apaq.shopsystem.service.OrganisationService"%>
<%@page import="dk.apaq.shopsystem.service.SystemServiceHolder"%>
<%@page import="dk.apaq.shopsystem.qash.Qash"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="dk.apaq.shopsystem.entity.Organisation"%>
<%@page import="dk.apaq.shopsystem.service.SystemService"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
String orgid = request.getParameter("orgid");

SystemService service = SystemServiceHolder.getSystemService();
Organisation org = service.getOrganisationCrud().read(orgid);
OrganisationService organisationService = service.getOrganisationService(org);
        

ResourceBundle res = Qash.getResourceBundle(request.getLocale());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Qash - Change plan for Shop</title>
        <meta name="description" content="Changes the plan in use for a specific Shop" />
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
                <h1><%=res.getString("subscribe.title")%></h1>
                <br/>
            <p>
                <div style="width:400px;padding-bottom:20px;">
                <%=res.getString("subscribe.text1")%><br/><br/>
                <h2><%=res.getString("subscribe.example.title")%></h2>
                <table>
                    <tr>
                        <td>
                            <%=res.getString("subscribe.example.revenue.title")%>:
                        </td>
                        <td>
                            <%=res.getString("subscribe.example.revenue.text")%>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <%=res.getString("subscribe.example.fee.title")%>:
                        </td>
                        <td>
                            <%=res.getString("subscribe.example.fee.text")%>
                        </td>
                    </tr>
                </table>
                <br/>
                <%=res.getString("subscribe.example.text2")%><br/>
                </div>
                
                <a href="dashboard.jsp" class="button-standard"><%=res.getString("subscribe.button.no")%></a>&nbsp;&nbsp;<a href="subscribe_2.jsp" class="button-standard"><%=res.getString("subscribe.button.yes")%></a>
                
                
            </p>
            </div>
        </div>
        <!-- /benefits -->

        <jsp:include page="inc/footer.jsp" />


    </body>
</html>

