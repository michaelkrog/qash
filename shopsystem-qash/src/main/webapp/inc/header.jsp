<%-- 
    Document   : header
    Created on : 16-05-2011, 11:42:01
    Author     : michaelzachariassenkrog
--%>
<%@page import="dk.apaq.shopsystem.qash.Qash"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="org.springframework.security.core.context.SecurityContext"%>
<%
SecurityContext ctx = SecurityContextHolder.getContext();
Authentication auth = ctx.getAuthentication();
boolean authenticated = auth!=null && !"anonymousUser".equals(auth.getName());
ResourceBundle res = Qash.getResourceBundle(request.getLocale());

%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!-- header -->
<div id="ge-header-container">
    <div class="ge-header">
        <!-- logo -->
        <div class="ge-logo"><a href="/"><img src="images/ge-logo.png" alt="Qash" width="153" height="35" /></a></div>
        <!-- /logo -->
        <%
        if(authenticated) {
        %>
            <div class="ge-userinfo">
                <div><a href="account.jsp"><span><%=res.getString("header.accountLink")%></span></a></div>
                <div><a href="admin"><span><%=res.getString("header.shoplistLink")%></span></a></div>
            </div>
            <div class="ge-userinfo">
                <div><a href="/logout"><span><%=res.getString("header.logoutLink")%></span></a></div>
            </div>
        <%
        }

        if(!authenticated) {
        %>
            <div class="ge-userinfo">
                <div><a href="admin"><span><%=res.getString("header.loginLink")%></span></a></div>
            </div>
        <%
        }
        %>
        <!-- phone -->
        <div class="ge-phone"><a href="contact.jsp"><span>contact<script>document.write('@');</script>qashapp.com</span></a></div>
        <!-- /phone -->
    </div>
</div>
<!-- /header -->
