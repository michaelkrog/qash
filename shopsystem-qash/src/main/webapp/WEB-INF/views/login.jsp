<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://qashapp.com/common" prefix="common" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Qash - Login</title>
        <meta name="description" content="Login for Qash" />
        <meta name="keywords" content="login, qash"/>
        <jsp:include page="inc/htmlhead.jsp" />
    </head>

    <body onload="document.getElementById('j_username').focus()">
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
                    <li style="width:430px; height:105px">
                        <span class="benefits-icon"><img src="images/account/login.png" alt="Login" width="70" height="70" /></span>
                        <div class="benefits-text" style="width:315px;padding-right:45px;">
                            <h2><spring:message code="login.option1.title"/></h2>
                            <p>
                                <form action="j_spring_security_check" method="POST">
                                    <table>
                                        <tr><td><label for="j_username"><spring:message code="login.option1.username"/></label></td>
                                            <td><input type="text" name="j_username" id="j_username"/></td>
                                        </tr>

                                        <tr><td><label for="j_password"><spring:message code="login.option1.password"/></label></td>
                                            <td> <input type="password" name="j_password" id="j_password"/></td>
                                        </tr>
                                            <tr>
                                            <td colspan="2"><input type="submit" value="<spring:message code="login.option1.login"/>" class="button-standard"/>
                                                &nbsp;<a href="forgot_password.htm" class="button-standard"><spring:message code="login.option1.forgotpassword"/></a>
                                            </td>
                                            </tr>
                                    </table>

                                </form>
                            </p>
      
                        </div>
                    </li>
                    <!-- /benefit item -->

                    <li style="width:430px; height:105px" class="endbenefits">
                        <span class="benefits-icon"><img src="images/account/signup.png" alt="Sign up" width="70" height="70" /></span>
                        <div class="benefits-text" style="width:315px;padding-right:45px">
                            <h2><spring:message code="login.option2.title"/></h2>
                            <p><spring:message code="login.option2.text"/></p>
                            <ul class="in-more">
                                <li class="morestart"><a href="createAccount.htm" class="button-standard"><spring:message code="login.option2.button"/></a></li>
                                <!--li class="morestart"><a href="#">Learn More</a></li-->
                                <!--li><a href="#">Preview</a></li-->
                            </ul>
                        </div>
                    </li>
                    

                    <li style="width:430px; height:105px">
                        <span class="benefits-icon"><img src="images/account/social.png" alt="Login" width="70" height="70" /></span>
                        <div class="benefits-text" style="width:315px;padding-right:45px;">
                            <h2><spring:message code="login.option3.title"/></h2>
                            <p><spring:message code="login.option3.text"/>
                            </p>
                            <ul class="in-more">
                                
                                <c:set var="rpxUrl" value="https://qash.rpxnow.com/openid/v2/signin?language_preference=${pageContext.request.locale.language}&token_url=${common:encodeUrl(rpxReturnUrl)}" scope="request" />

                                <li class="morestart"><a class="rpxnow button-standard" onclick="return false;" href=""><spring:message code="login.option3.button"/></a></li>
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

