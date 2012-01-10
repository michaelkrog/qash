<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Qash - New account</title>
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
                <h1><spring:message code="createaccount.title"/></h1>
            <br/>
            <br/>
            <p>
                <form:form action="createAccount.htm" modelAttribute="accountInfo">
                <input type="hidden" name="save" value="true"/>
            <table class="table-minimalistic">
                <tr>
                    <td class="lined"><spring:message code="createaccount.company"/></td>
                    <td class="lined"><form:input path="companyName" /><form:errors path="companyName" /></td>
                </tr>
                <tr>
                    <td><spring:message code="createaccount.username"/></td>
                    <td><form:input path="userName" /><form:errors path="userName" /></td>
                </tr>
                <tr>
                    <td><spring:message code="createaccount.name"/></td>
                    <td><form:input path="displayName" /><form:errors path="displayName" /></td>
                </tr>
                <tr>
                    <td><spring:message code="createaccount.email"/></td>
                    <td><form:input path="email" /><form:errors path="email" /></td>
                </tr>
                <tr>
                    <td class="lined"><spring:message code="createaccount.email_confirm"/></td>
                    <td class="lined"><form:input path="email2" /><form:errors path="email2" /></td>
                </tr>
                <tr>
                    <td><spring:message code="createaccount.password"/></td>
                    <td><form:input path="password" /><form:errors path="password" /></td>
                </tr>
                <tr>
                    <td><spring:message code="createaccount.password_confirm"/></td>
                    <td><form:input path="password2" /><form:errors path="password2" /></td>
                </tr>

                <tr>
                    <td></td><td><button type="submit" class="button-standard"><spring:message code="createaccount.submit"/></button></td>
                </tr>
            </table>
            </form:form>
            </p>
            </div>
        </div>
        <!-- /benefits -->

        <jsp:include page="inc/footer.jsp" />


    </body>
</html>

