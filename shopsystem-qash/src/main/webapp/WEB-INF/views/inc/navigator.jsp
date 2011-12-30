<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%
String path = request.getServletPath();
String current = path==null ? "" : path.substring(path.lastIndexOf("/") + 1);

%>
<!-- menu -->
<div class="ge-navigator-container">
    <div class="ge-navigator">
        <div>
            <div class="ge-navigator-link <%=("index.htm".equals(current) || "".equals(current) ? "ge-navigator-link-current" : "")%>"><a href="/"><span><spring:message code="navigator.homeLink"/></span></a></div>
        </div>
        <div class="ge-navigator-space">&nbsp;</div>
        <div>
            <div class="ge-navigator-link <%=("tour.htm".equals(current) ? "ge-navigator-link-current" : "")%>"><a href="tour.htm"><span><spring:message code="navigator.tourLink"/></span></a></div>
        </div>
        <div class="ge-navigator-space">&nbsp;</div>
        <div>
            <div class="ge-navigator-link <%=("prices.htm".equals(current) ? "ge-navigator-link-current" : "")%>"><a href="prices.htm"><span><spring:message code="navigator.pricesLink"/></span></a></div>
        </div>
        <div class="ge-navigator-space">&nbsp;</div>
        <div>
            <div class="ge-navigator-link <%=("contact.htm".equals(current) ? "ge-navigator-link-current" : "")%>"><a href="contact.htm"><span><spring:message code="navigator.contactLink"/></span></a></div>

        </div>
    </div>
</div>
<!-- /menu -->
