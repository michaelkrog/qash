<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!-- footer -->
<div id="ge-footer-container">
    <div class="ge-footer">
        <div class="ge-calltoaction">
            <!-- call to action -->
            <h4><spring:message code="footer.title"/></h4>
            <p><spring:message code="footer.description"/>
            </p>
            <!-- /call to action -->
        </div>
        <!-- call to action button -->
        <span class="ge-calltoaction-button"><a href="dashboard.jsp"><spring:message code="footer.start"/></a></span>
        <!-- /call to action button -->
        <div class="clear"></div>
        <div class="ge-copyright">
            <!-- copyright -->
            <ul class="copyright">
                <li class="copyrightstart">&copy; 2011 Apaq. All rights reserved</li>
                <li><a href="#"><spring:message code="footer.contact"/></a></li>
                <li><a href="#"><spring:message code="footer.help"/></a></li>
                <li><a href="#"><spring:message code="footer.policy"/></a></li>
                <li><a href="#"><spring:message code="footer.terms"/></a></li>
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