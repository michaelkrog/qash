<%--
    Document   : index
    Created on : 16-05-2011, 11:20:51
    Author     : michaelzachariassenkrog
--%>
<%@page import="dk.apaq.shopsystem.qash.Qash"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="org.springframework.security.authentication.AnonymousAuthenticationToken"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@page import="org.springframework.security.core.context.SecurityContext"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="java.util.Locale"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URL"%>
<%

String redirect = request.getParameter("redirect");
if(redirect==null) {
        redirect="dashboard.jsp?autoRedirect=true";
}

SecurityContext ctx = SecurityContextHolder.getContext();
Authentication auth = ctx.getAuthentication();
if(auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
    response.sendRedirect(redirect);
    return;
}

ResourceBundle res = Qash.getResourceBundle(request.getLocale());

String spec = "/j_spring_rpx_security_check?redirect="+URLEncoder.encode(redirect,"utf-8");
URL tokenUrl = new URL(new URL(request.getRequestURL().toString()),spec);

String returnUrl = tokenUrl.toString();
Locale locale = request.getLocale();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Qash - Login</title>
        <meta name="description" content="Login for Qash" />
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
                <ul>

                    <!-- benefit item -->
                    <li style="width:430px">
                        <span class="benefits-icon"><img src="images/account/login.png" alt="Login" width="70" height="70" /></span>
                        <div class="benefits-text" style="width:315px;padding-right:45px;">
                            <h2><%=res.getString("login.option1.title")%></h2>
                            <p>
                                <form action="j_spring_security_check" method="POST">
                                    <table>
                                        <tr><td><label for="j_username"><%=res.getString("login.option1.username")%></label></td>
                                            <td><input id="username" type="text" name="j_username" id="j_username"/></td>
                                        </tr>

                                        <tr><td><label for="j_password"><%=res.getString("login.option1.password")%></label></td>
                                            <td> <input type="password" name="j_password" id="j_password"/></td><td><input type="submit" value="<%=res.getString("login.option1.login")%>" class="button-standard"/></td>
                                        </tr>

                                    </table>

                                </form>
                            </p>
      
                        </div>
                    </li>
                    <!-- /benefit item -->

                    <li style="width:430px" class="endbenefits">
                        <span class="benefits-icon"><img src="images/account/signup.png" alt="Sign up" width="70" height="70" /></span>
                        <div class="benefits-text" style="width:315px;padding-right:45px">
                            <h2><%=res.getString("login.option2.title")%></h2>
                            <p><%=res.getString("login.option2.text")%></p>
                            <ul class="in-more">
                                <li class="morestart"><a href="create_account.jsp" class="button-standard"><%=res.getString("login.option2.button")%></a></li>
                                <!--li class="morestart"><a href="#">Learn More</a></li-->
                                <!--li><a href="#">Preview</a></li-->
                            </ul>
                        </div>
                    </li>
                    

                    <li style="width:430px">
                        <span class="benefits-icon"><img src="images/account/social.png" alt="Login" width="70" height="70" /></span>
                        <div class="benefits-text" style="width:315px;padding-right:45px;">
                            <h2><%=res.getString("login.option3.title")%></h2>
                            <p><%=res.getString("login.option3.text")%>
                            </p>
                            <ul class="in-more">
                                <li class="morestart"><a class="rpxnow button-standard" onclick="return false;" href="https://qash.rpxnow.com/openid/v2/signin?language_preference=<%=locale.getLanguage() %>&token_url=<%=URLEncoder.encode(returnUrl,"utf-8") %>"><%=res.getString("login.option3.button")%></a></li>
                                <!--li class="morestart"><a href="#">Learn More</a></li-->
                                <!--li><a href="#">Preview</a></li-->
                            </ul>
                        </div>
                    </li>
                    
                    
                </ul>
                <div class="clear"></div>
            </div>
        </div>
        <!-- /benefits -->

        <jsp:include page="inc/footer.jsp" />


        <script type="text/javascript" charset="utf-8">
          var rpxJsHost = (("https:" == document.location.protocol) ? "https://" : "http://static.");
          document.write(unescape("%3Cscript src='" + rpxJsHost +
        "rpxnow.com/js/lib/rpx.js' type='text/javascript'%3E%3C/script%3E"));
        </script>
        <script type="text/javascript">
          RPXNOW.overlay = true;
          RPXNOW.language_preference = 'en';
        </script>
    </body>
</html>

