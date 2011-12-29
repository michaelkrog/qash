<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%
SecurityContextHolder.setContext(null);
response.sendRedirect("./");
%>
