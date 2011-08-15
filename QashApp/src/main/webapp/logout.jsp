<%-- 
    Document   : logout.jsp
    Created on : 16-05-2011, 12:10:05
    Author     : michaelzachariassenkrog
--%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%
SecurityContextHolder.setContext(null);
response.sendRedirect("./");
%>
