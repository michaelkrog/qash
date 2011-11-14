<%@page import="dk.apaq.shopsystem.qash.Qash"%>
<%@page import="java.util.ResourceBundle"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
ResourceBundle res = Qash.getResourceBundle(request.getLocale());

%>
<!-- footer -->
<div id="ge-footer-container">
    <div class="ge-footer">
        <div class="ge-calltoaction">
            <!-- call to action -->
            <h4><%=res.getString("footer.title")%></h4>
            <p><%=res.getString("footer.description")%>
            </p>
            <!-- /call to action -->
        </div>
        <!-- call to action button -->
        <span class="ge-calltoaction-button"><a href="login.jsp"><%=res.getString("footer.start")%></a></span>
        <!-- /call to action button -->
        <div class="clear"></div>
        <div class="ge-copyright">
            <!-- copyright -->
            <ul class="copyright">
                <li class="copyrightstart">&copy; 2011 Apaq. All rights reserved</li>
                <li><a href="#"><%=res.getString("footer.contact")%></a></li>
                <li><a href="#"><%=res.getString("footer.help")%></a></li>
                <li><a href="#"><%=res.getString("footer.policy")%></a></li>
                <li><a href="#"><%=res.getString("footer.terms")%></a></li>
            </ul>
            <!-- /copyright -->
            <!-- social -->
            <ul class="socialmedia">
                <!--li><a href="#"><img src="images/social/icon-digg.png" alt="Digg" width="16" height="16" /></a></li>
                <li><a href="#"><img src="images/social/icon-facebook.png" alt="Facebook" width="16" height="16" /></a></li>
                <li><a href="#"><img src="images/social/icon-flickr.png" alt="Flickr" width="16" height="16" /></a></li>
                <li><a href="#"><img src="images/social/icon-linkedin.png" alt="LinkedIn" width="16" height="16" /></a></li>
                <li><a href="#"><img src="images/social/icon-rss.png" alt="RSS" width="16" height="16" /></a></li>
                <li><a href="#"><img src="images/social/icon-skype.png" alt="Skype" width="16" height="16" /></a></li-->
                <li><a href="http://twitter.com/share" class="twitter-share-button" data-count="none" data-via="qashapp">Tweet</a><script type="text/javascript" src="http://platform.twitter.com/widgets.js"></script></li>
                <!--li><a href="#"><img src="images/social/icon-vimeo.png" alt="Vimeo" width="16" height="16" /></a></li>
                <li><a href="#"><img src="images/social/icon-youtube.png" alt="YouTube" width="16" height="16" /></a></li-->
            </ul>
            <!-- /social -->
        </div>
    </div>
</div>
<!-- /footer -->