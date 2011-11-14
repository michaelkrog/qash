<%-- 
    Document   : navigator
    Created on : 16-05-2011, 11:42:09
    Author     : michaelzachariassenkrog
--%>
<%@page import="dk.apaq.shopsystem.qash.Qash"%>
<%@page import="java.util.ResourceBundle"%>
<%
String path = request.getServletPath();
String current = path==null ? "" : path.substring(path.lastIndexOf("/") + 1);
ResourceBundle res = Qash.getResourceBundle(request.getLocale());

%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- menu -->
<div class="ge-navigator-container">
    <div class="ge-navigator">
        <div>
            <div class="ge-navigator-link <%=("index.jsp".equals(current) || "".equals(current) ? "ge-navigator-link-current" : "")%>"><a href="/"><span><%=res.getString("navigator.homeLink")%></span></a></div>
        </div>
        <div class="ge-navigator-space">&nbsp;</div>
        <div>
            <div class="ge-navigator-link <%=("tour.jsp".equals(current) ? "ge-navigator-link-current" : "")%>"><a href="tour.jsp"><span><%=res.getString("navigator.tourLink")%></span></a></div>
        </div>
        <div class="ge-navigator-space">&nbsp;</div>
        <div>
            <div class="ge-navigator-link <%=("prices.jsp".equals(current) ? "ge-navigator-link-current" : "")%>"><a href="prices.jsp"><span><%=res.getString("navigator.pricesLink")%></span></a></div>
        </div>
        <div class="ge-navigator-space">&nbsp;</div>
        <div>
            <div class="ge-navigator-link <%=("contact.jsp".equals(current) ? "ge-navigator-link-current" : "")%>"><a href="contact.jsp"><span><%=res.getString("navigator.contactLink")%></span></a></div>

        </div>
    </div>
</div>
<!-- /menu -->
