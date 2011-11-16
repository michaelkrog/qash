<%--
    Document   : index
    Created on : 16-05-2011, 11:20:51
    Author     : michaelzachariassenkrog
--%>
<%@page import="dk.apaq.shopsystem.entity.Organisation"%>
<%@page import="dk.apaq.shopsystem.service.SystemService"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="dk.apaq.crud.Crud"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("utf-8");

boolean create = "true".equalsIgnoreCase(request.getParameter("create"));

if(create) {
    String name = request.getParameter("name");
    if(name != null && !"".equals(name)) {
        WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(application);
        SystemService service = wac.getBean(SystemService.class);
        Crud.Editable<String, Organisation> orgCrud = service.getOrganisationCrud();
        Organisation org = orgCrud.read(orgCrud.create());
        org.setName(name);
        orgCrud.update(org);
        response.sendRedirect("dashboard.jsp");
        return;
    }
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Qash - Create new Shop</title>
        <meta name="description" content="Creates a new shop." />
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

    <body onload="document.getElementById('name').focus()">
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
                <h1>Create shop</h1>
                <br/>
            <p>
                <form method="POST">
                    <input type="hidden" name="create" value="true" />
                    Name:&nbsp;<input id="name" type="text" name="name" />&nbsp;<input type="submit" class="button-standard" value="Create" />
                </form>
               

            </p>
            </div>
        </div>
        <!-- /benefits -->

        <jsp:include page="inc/footer.jsp" />


    </body>
</html>
