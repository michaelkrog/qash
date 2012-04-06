<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page import="org.springframework.security.core.Authentication"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="org.springframework.security.core.context.SecurityContext"%>
<%
SecurityContext ctx = SecurityContextHolder.getContext();
Authentication auth = ctx.getAuthentication();
boolean authenticated = auth!=null && !"anonymousUser".equals(auth.getName());
%>
<!-- header -->
<div id="ge-header-container">
    <div class="ge-header">
        <!-- logo -->
        <div class="ge-logo"><a href="/"><img src="<spring:theme code="logo"/>" alt="Qash" width="153" height="35" /></a></div>
        <!-- /logo -->
        <%
        if(authenticated) {
        %>
            <div class="ge-userinfo">
                <div><a href="account.htm"><span><spring:message code="header.accountLink"/></span></a></div>
                <div><a href="dashboard.htm"><span><spring:message code="header.dashboardLink"/></span></a></div>
            </div>
            <div class="ge-userinfo">
                <div><a href="/logout"><span><spring:message code="header.logoutLink"/></span></a></div>
            </div>
        <%
        }

        if(!authenticated) {
        %>
            <div class="ge-userinfo">
                <div><a href="dashboard.htm"><span><spring:message code="header.loginLink"/></span></a></div>
            </div>
        <%
        }
        %>
        <!-- phone -->
        <div class="ge-phone"><a href="contact.htm"><span>contact<script>document.write('@');</script>qashapp.com</span></a></div>
        <!-- /phone -->
    </div>
</div>
<!-- /header -->
