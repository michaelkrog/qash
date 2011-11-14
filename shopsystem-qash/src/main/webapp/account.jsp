<%@page import="dk.apaq.shopsystem.qash.Qash"%>
<%@page import="dk.apaq.shopsystem.security.SystemUserDetails"%>
<%@page import="dk.apaq.shopsystem.entity.SystemUser"%>
<%@page import="dk.apaq.shopsystem.service.SystemService"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="dk.apaq.crud.Crud"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.springframework.security.core.context.SecurityContext"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%!
    private String prepareText(String text) {
        if(text==null) {
            return "";
        }
        return StringEscapeUtils.escapeHtml(text);
    }

    private boolean equals(Object o1, Object o2) {
        if(o1 ==null && o2==null) {
            return true;
        }

        if(o1==null) {
            return false;
        }
        return o1.equals(o2);
    }
%>
<%
request.setCharacterEncoding("utf-8");
WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(application);
SystemService service = wac.getBean(SystemService.class);
Crud.Editable<String, SystemUser> userCrud = service.getSystemUserCrud();

SecurityContext ctx = SecurityContextHolder.getContext();
Authentication auth = ctx.getAuthentication();
SystemUserDetails details = (SystemUserDetails) auth.getPrincipal();

SystemUser account = details.getUser();

String name = account.getDisplayname();
String email = account.getEmail();
String password="UNCHANGED";

boolean save = "true".equals(request.getParameter("save"));

if(save) {
    email = request.getParameter("email");
    if(!equals(email, account.getEmail())) {
        account.setEmail(email);
        account.setEmailVerified(false);
    }
    account.setDisplayname(request.getParameter("name"));
   
    userCrud.update(account);
    response.sendRedirect("dashboard.jsp");
    return;
}

ResourceBundle res = Qash.getResourceBundle(request.getLocale());


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
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
                <h1><%=res.getString("account.title")%></h1>
            <br/>
            <br/>
            <p>
            <form method="POST" enctype="application/x-www-form-urlencoded">
                <input type="hidden" name="save" value="true"/>
            <table class="table-minimalistic">
                <tr>
                    <td class="lined"><%=res.getString("account.username")%></td><td class="lined"><%=prepareText(account.getName())%></td>
                </tr>
                <tr>
                    <td><%=res.getString("account.name")%></td><td><input type="text" name="name" value="<%=prepareText(name)%>" /></td>
                </tr>
                <tr>
                    <td><%=res.getString("account.email")%></td><td><input type="text" name="email" value="<%=prepareText(email)%>" /></td>
                </tr>
                <tr>
                    <td><%=res.getString("account.password")%></td><td><input type="password" name="password1" value="<%=prepareText(password)%>" /></td>
                </tr>
                <tr>
                    <td><%=res.getString("account.confirmPassword")%></td><td><input type="password" name="password2" value="<%=prepareText(password)%>" /></td>
                </tr>

                <tr>
                    <td></td><td><input type="submit" class="button-standard" value="<%=res.getString("account.save")%>" /></td>
                </tr>
            </table>
            </form>
            </p>
            </div>
        </div>
        <!-- /benefits -->

        <jsp:include page="inc/footer.jsp" />


    </body>
</html>

