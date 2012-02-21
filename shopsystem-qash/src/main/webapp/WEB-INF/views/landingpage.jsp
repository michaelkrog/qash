<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Qash - The professional online cash register (POS / Point of Sale)</title>
        <meta name="description" content="Qash is an innovative approach to an online cash register ( POS / Point Of Sale ). With a user-friendly and attractive interface, it is both quick and easy to get started selling products from your shop." />
        <meta name="keywords" content="qash, point of sale, pos, pos in browser, retail, pos, pos software, apaq, macos, linux, windows, mac os, browser, online, sales, credit card, invoice, inventory, accounting, bookkeeping"/>
        <jsp:include page="inc/htmlhead.jsp" />
    </head>

    <body>
        <!-- splash -->
        <div id="in-splash-container">
            <div class="in-splash">
                <div class="in-splash-content">
                    <div class="in-splash-left">
                        <!-- splash text -->
                        <h1><spring:message code="splash.title"/></h1>
                        <ul>
                            <li><spring:message code="splash.subtitle1"/></li>
                            <li><spring:message code="splash.subtitle2"/></li>
                            <li><spring:message code="splash.subtitle3"/></li>
                            <li><spring:message code="splash.subtitle4"/></li>
                            <li><spring:message code="splash.subtitle5"/></li>
                            <li><spring:message code="splash.subtitle6"/></li>

                        </ul>
                        <!-- /splash text -->
                    </div>
                    <!-- splash image -->
                    <span class="in-splash-right"><img src="images/in-splash.jpg" alt="example" width="460" height="283" /></span>
                    <!-- /splash image -->
                    <!-- splash buttons -->
                    <span class="in-splash-buttons">
                        <span class="splash-button"><a href="login.htm"><spring:message code="splash.start"/></a></span>
                        <span class="splash-link"><a href="tour.htm"><spring:message code="splash.demo"/></a></span>
                    </span>
                    <!-- /splash buttons -->
                </div>
            </div>
        </div>
        <!-- /splash -->

        <jsp:include page="inc/header.jsp" />

        <jsp:include page="inc/navigator.jsp" />



        <!-- benefits -->
        <div id="in-benefits-container">
            <div class="in-benefits">
                <ul>
                    <!-- benefit item -->
                    <li>
                        <span class="benefits-icon"><img src="images/benefits/benefits-001.gif" alt="example" width="70" height="70" /></span>
                        <div class="benefits-text">
                            <h2><spring:message code="teaser.title1"/></h2>
                            <p><spring:message code="teaser.text1"/></p>
                            <ul class="in-more">
                                <li class="morestart"><!--<a href="#">Learn More</a>--></li>
                                <!--li><a href="#">Preview</a></li-->
                            </ul>
                        </div>
                    </li>
                    <!-- /benefit item -->
                    <li>
                        <span class="benefits-icon"><img src="images/benefits/benefits-002.gif" alt="example" width="70" height="70" /></span>
                        <div class="benefits-text">
                            <h2><spring:message code="teaser.title2"/></h2>
                            <p><spring:message code="teaser.text2"/></p>
                            <ul class="in-more">
                                <li class="morestart"><!--<a href="#">Learn More</a>--></li>
                                <!--li><a href="#">Preview</a></li-->
                            </ul>
                        </div>
                    </li>
                    <li class="endbenefits">
                        <span class="benefits-icon"><img src="images/benefits/benefits-003.gif" alt="example" width="70" height="70" /></span>
                        <div class="benefits-text">
                            <h2><spring:message code="teaser.title3"/></h2>
                            <p><spring:message code="teaser.text3"/></p>
                            <ul class="in-more">
                                <li class="morestart"><!--<a href="#">Learn More</a>--></li>
                                <!--li><a href="#">Preview</a></li-->
                            </ul>
                        </div>
                    </li>
                    <li>
                        <span class="benefits-icon"><img src="images/benefits/benefits-004.gif" alt="example" width="70" height="70" /></span>
                        <div class="benefits-text">
                            <h2><spring:message code="teaser.title4"/></h2>
                            <p><spring:message code="teaser.text4"/></p>
                            <ul class="in-more">
                                <li class="morestart"><!--<a href="#">Learn More</a>--></li>
                                <!--li><a href="#">Preview</a></li-->
                            </ul>
                        </div>
                    </li>
                    <li>
                        <span class="benefits-icon"><img src="images/benefits/benefits-005.gif" alt="example" width="70" height="70" /></span>
                        <div class="benefits-text">
                            <h2><spring:message code="teaser.title5"/></h2>
                            <p><spring:message code="teaser.text5"/></p>
                            <ul class="in-more">
                                <li class="morestart"><!--<a href="#">Learn More</a>--></li>
                                <!--li><a href="#">Preview</a></li-->
                            </ul>
                        </div>
                    </li>
                    <li class="endbenefits">
                        <span class="benefits-icon"><img src="images/benefits/benefits-006.gif" alt="example" width="70" height="70" /></span>
                        <div class="benefits-text">
                            <h2><spring:message code="teaser.title6"/></h2>
                            <p><spring:message code="teaser.text6"/></p>
                            <ul class="in-more">
                                <li class="morestart"><!--<a href="#">Learn More</a>--></li>
                                <!--li><a href="#">Preview</a></li-->
                            </ul>
                        </div>
                    </li>
                </ul>
                <div class="clear"></div>
            </div>
        </div>
        <!-- /benefits -->

        <!-- testimonials -->
        <!--div id="in-testimonials-container">
  	<ul class="in-testimonials">
              <li>
                <span class="testimonial-logo"><img src="images/testimonials/logo-001.gif" alt="example" width="138" height="52" /></span>
                <span class="testimonial-text">
                The Autopilot template has helped us convert users to buyers at an unbelievable rate.<br />I highly recommend this to everyone serious about making money online!
                </span>
              </li>
              <li>
                <span class="testimonial-logo"><img src="images/testimonials/logo-002.gif" alt="example" width="138" height="52" /></span>
                <span class="testimonial-text">
                Two thumbs up! This template exceeded my expectation, it contains all the necessary modules<br />for me to offer all my clients a generic and professional solution.
                </span>
              </li>
              <li>
                <span class="testimonial-logo"><img src="images/testimonials/logo-003.gif" alt="example" width="138" height="52" /></span>
                <span class="testimonial-text">
                This template is fantastic! The Autopilot template has been with us since day one of offering a<br />landing page service. All our customers have been happy with it - this is a worthwhile purchase!
                </span>
              </li>
            </ul>
        </div-->
        <!-- /testimonials -->

        <!-- quote -->
        <div id="in-quote-container">
            <div class="in-quote">
                <div class="quote">
                    <div class="quote-text">
                        <!-- contact details -->
                        <h2><spring:message code="quote.title"/></h2>
                        <p><spring:message code="quote.text"/><br /><br />
                            <spring:message code="quote.email"/>
                            <br /><br />
                            <spring:message code="quote.address"/>
                        </p>
                        <!-- /contact details -->
                    </div>
                    <div class="quote-form">
                        <!-- contact form -->
                        <form id="in-quote-form">
                            <fieldset>
                                <div class="form-left">
                                    <label for="fullname"><spring:message code="quote.form.fullname"/> <span>*</span></label>
                                    <!--input id="fullname" name="fullname" type="text" /-->
                                    <input id="name" name="name" path="fullName" />
                                </div>
                                <div class="form-right">
                                    <label for="emailaddress"><spring:message code="quote.form.email"/> <span>*</span></label>
                                    <!--input id="emailaddress" name="emailaddress" type="text" /-->
                                    <input id="email" name="email" path="emailAddress" />
                                </div>
                                <label for="message"><spring:message code="quote.form.message"/> <span>*</span></label>
                                <!--textarea id="message" name="message" cols="10" rows="5"></textarea-->
                                <textarea id="message" name="message" path="message" rows="5" cols="10"> </textarea>
                                <button type="submit"><spring:message code="quote.form.send"/></button>
                            </fieldset>
                        </form>
                        <!-- /contact form -->
                    </div>
                </div>
                <div class="screenshots">
                    <!-- screenshots -->
                    <ul class="gallery">
                        <!-- screenshot item -->
                        <li>
                            <span class="in-screenshots-view"><img src="images/in-screenshot-view.png" alt="view" width="19" height="19" /></span>
                            <a href="images/screenshots/screenshot-001.jpg" rel="prettyPhoto[gallery]"><img src="images/screenshots/thumbnail-001.jpg" alt="example" width="138" height="76" /></a>
                        </li>
                        <!-- /screenshot item -->
                        <li class="endscreenshot">
                            <span class="in-screenshots-view"><img src="images/in-screenshot-view.png" alt="view" width="19" height="19" /></span>
                            <a href="images/screenshots/screenshot-002.jpg" rel="prettyPhoto[gallery]"><img src="images/screenshots/thumbnail-002.jpg" alt="example" width="138" height="76" /></a>
                        </li>
                        <li>
                            <span class="in-screenshots-view"><img src="images/in-screenshot-view.png" alt="view" width="19" height="19" /></span>
                            <a href="images/screenshots/screenshot-003.jpg" rel="prettyPhoto[gallery]"><img src="images/screenshots/thumbnail-003.jpg" alt="example" width="138" height="76" /></a>
                        </li>
                        <li class="endscreenshot">
                            <span class="in-screenshots-view"><img src="images/in-screenshot-view.png" alt="view" width="19" height="19" /></span>
                            <a href="images/screenshots/screenshot-004.jpg" rel="prettyPhoto[gallery]"><img src="images/screenshots/thumbnail-004.jpg" alt="example" width="138" height="76" /></a>
                        </li>
                        <li>
                            <span class="in-screenshots-view"><img src="images/in-screenshot-view.png" alt="view" width="19" height="19" /></span>
                            <a href="images/screenshots/screenshot-005.jpg" rel="prettyPhoto[gallery]"><img src="images/screenshots/thumbnail-005.jpg" alt="example" width="138" height="76" /></a>
                        </li>
                        <li class="endscreenshot">
                            <span class="in-screenshots-view"><img src="images/in-screenshot-view.png" alt="view" width="19" height="19" /></span>
                            <a href="images/screenshots/instore_picture.jpg" rel="prettyPhoto[gallery]"><img src="images/screenshots/instore_picture_thumb.jpg" alt="In store illustration" width="138" height="76" /></a>
                        </li>
                    </ul>
                    <!-- /screenshots -->
                </div>
            </div>
        </div>
        <!-- /quote -->

        <!-- clients -->
        <!--div id="in-clients-container">
            <div class="in-clients">
                <h3>Qash is being used by shops worldwide.</h3>
                <!-- client logos ->
                <ul>
                    <li><img src="images/clients/logo-001.gif" alt="example" width="116" height="64" /></li>
                    <li><img src="images/clients/logo-002.gif" alt="example" width="116" height="64" /></li>
                    <li><img src="images/clients/logo-003.gif" alt="example" width="116" height="64" /></li>
                    <li><img src="images/clients/logo-004.gif" alt="example" width="116" height="64" /></li>
                    <li><img src="images/clients/logo-005.gif" alt="example" width="116" height="64" /></li>
                    <li><img src="images/clients/logo-006.gif" alt="example" width="116" height="64" /></li>
                    <li class="endclients"><img src="images/clients/logo-007.gif" alt="example" width="116" height="64" /></li>
                </ul>
                <!-- /client logos ->
            </div>
        </div-->
        <!-- /clients -->

        <jsp:include page="inc/footer.jsp" />
        

        <!--script type="text/javascript"> Cufon.now(); </script-->
        <script type="text/javascript" charset="utf-8">
            $(document).ready(function(){
                $(".gallery a[rel^='prettyPhoto']").prettyPhoto({animationSpeed:'fast',slideshow:10000, theme: 'autopilot'});
            });
            
            $('#in-quote-form').submit(function() {
              var data = $(this).serialize();
              $.get('sendQuote.htm', data, function(data) {
                  alert("Thank you. We will respond to yoor quote as soon as possible.")
                });
              return false;
            });
        </script>
    </body>
</html>

