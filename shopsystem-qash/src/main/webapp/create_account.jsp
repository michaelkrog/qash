<%@page import="dk.apaq.shopsystem.service.OrganisationService"%>
<%@page import="dk.apaq.shopsystem.entity.Organisation"%>
<%@page import="dk.apaq.shopsystem.entity.SystemUser"%>
<%@page import="dk.apaq.shopsystem.service.SystemService"%>
<%@page import="org.springframework.security.web.authentication.WebAuthenticationDetails"%>
<%@page import="org.springframework.security.authentication.AuthenticationManager"%>
<%@page import="org.springframework.security.authentication.UsernamePasswordAuthenticationToken"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dk.apaq.filter.core.CompareFilter"%>
<%@page import="dk.apaq.filter.Filter"%>
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

private void authenticateUserAndSetSession(AuthenticationManager am, SystemUser account,  HttpServletRequest request)
{
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            account.getName(), account.getPassword());

    // generate session if one doesn't exist
    request.getSession();

    token.setDetails(new WebAuthenticationDetails(request));
    Authentication authenticatedUser = am.authenticate(token);

    SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
}

%>
<%
request.setCharacterEncoding("utf-8");
WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(application);
AuthenticationManager am =  (AuthenticationManager)wac.getBean("authenticationManager");
SystemService service = wac.getBean(SystemService.class);
Crud.Complete<String, SystemUser> accountCrud = service.getSystemUserCrud();

boolean save = "true".equals(request.getParameter("save"));
boolean emailError = false;
boolean passwordError = false;
boolean usernameError = false;
boolean companyError = false;
String error = null;

String company = request.getParameter("company");
String username = request.getParameter("username");
String name = request.getParameter("name");
String email1 = request.getParameter("email1");
String email2 = request.getParameter("email2");
String password1 = request.getParameter("password1");
String password2 = request.getParameter("password2");

if(save) {
    Filter filterUsername = new CompareFilter("name", username, CompareFilter.CompareType.Equals);
    
    if(company.length()<4) {
        companyError = true;
        error = "Name of company too short. Use at least 4 characters";
    } else if(username.length()<4) {
        usernameError = true;
        error = "Username too short. Use at least 4 characters";
    } else if(!accountCrud.listIds(filterUsername, null).isEmpty()) {
        usernameError = true;
        error = "Username already in use";
    } else if(!email1.equals(email2)) {
        emailError = true;
        error = "Confirmed email does not match.";
    }/* else if(!EmailValidator.getInstance().isValid(email1)) {
        emailError = true;
        error = "Email does not seem to be a valid email address.";
    }*/
    else if(!password1.equals(password2)) {
        passwordError=true;
        error = "Confirmed password does not match.";
    } else if (password1.length() < 6) {
        passwordError=true;
        error = "Password too short. Use at least 6 characters.";
    }

    if(error==null) {
        Organisation org = new Organisation();
        org.setName(company);
        String orgId = service.getOrganisationCrud().create(org);
        org = service.getOrganisationCrud().read(orgId);
        OrganisationService orgService = service.getOrganisationService(org);
        
        SystemUser account = (SystemUser)orgService.getUsers().read(orgService.getUsers().createSystemUser());
        account.setName(username);
        account.setDisplayname(name);
        account.setEmail(email1);
        account.setPassword(password1);
        orgService.getUsers().update(account);
        
        //Login
        authenticateUserAndSetSession(am, account, request);
        
        //Redirect
        response.sendRedirect("dashboard.jsp");
        return;
    }
}


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Qash - New account</title>
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
                <h1>New account</h1>
            <br/>
            <br/>
            <p>
                <%=error==null ? "" : "<span style=\"background:#FFFFAF;padding:8px;font-weight:bold;\">" + error + "</span>"%>
            <form method="POST" enctype="application/x-www-form-urlencoded">
                <input type="hidden" name="save" value="true"/>
            <table class="table-minimalistic">
                <tr>
                    <td class="lined">Company</td><td class="lined"><input <%=companyError?"style=\"background:#EFAFAF\"":""%> type="text" name="company" value="<%=prepareText(company)%>"/></td>
                </tr>
                <tr>
                    <td>Username</td><td><input <%=usernameError?"style=\"background:#EFAFAF\"":""%> type="text" name="username" value="<%=prepareText(username)%>"/></td>
                </tr>
                <tr>
                    <td>Name</td><td><input type="text" name="name"  value="<%=prepareText(name)%>"/></td>
                </tr>
                <tr>
                    <td>Email</td><td><input <%=emailError?"style=\"background:#EFAFAF\"":""%> type="text" name="email1"  value="<%=prepareText(email1)%>"/></td>
                </tr>
                <tr>
                    <td class="lined">Confirm Email</td><td class="lined"><input <%=emailError?"style=\"background:#EFAFAF\"":""%> type="text" name="email2"  value="<%=prepareText(email2)%>"/></td>
                </tr>
                <tr>
                    <td>Password</td><td><input <%=passwordError?"style=\"background:#EFAFAF\"":""%> type="password" name="password1" value="<%=prepareText(password1)%>" /></td>
                </tr>
                <tr>
                    <td>Confirm Password</td><td><input <%=passwordError?"style=\"background:#EFAFAF\"":""%> type="password" name="password2" value="<%=prepareText(password2)%>" /></td>
                </tr>

                <tr>
                    <td></td><td><input type="submit" class="button-standard" value="Create Account" /></td>
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

