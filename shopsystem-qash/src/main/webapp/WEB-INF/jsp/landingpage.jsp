<%@page import="dk.apaq.shopsystem.qash.Qash"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ResourceBundle"%>
<%
ResourceBundle res = Qash.getResourceBundle(request.getLocale());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Qash - The professional online cash register (POS / Point of Sale)</title>
        <meta name="description" content="Qash is an innovative approach to an online cash register ( POS / Point Of Sale ). With a user-friendly and attractive interface, it is both quick and easy to get started selling products from your shop." />
        <meta name="keywords" content="qash, point of sale, pos, pos in browser, retail, pos, pos software, apaq, macos, linux, windows, mac os, browser, online, sales, credit card, invoice, inventory, accounting, bookkeeping"/>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="content-language" content="en"/>
        <meta name="google-site-verification" content="Dh7WdAlkzeXUeckVsd2uAAunO6sKnBTFBAYODINwz54" />
        <link rel="shortcut icon" href="favicon.ico"/>
        <link rel="stylesheet" type="text/css" href="css/reset.css"/>
        <link rel="stylesheet" type="text/css" href="css/layout.css"/>
        <link rel="stylesheet" type="text/css" href="css/lightbox.css"/>
        <link  href="http://fonts.googleapis.com/css?family=Open+Sans:300,300italic,400,400italic,600,600italic,700,700italic,800,800italic" rel="stylesheet" type="text/css" />

        <!--[if IE 6]>
        <link rel="stylesheet" type="text/css" href="css/ie6.css" />
        <![endif]-->
        <!--[if IE 7]>
        <link rel="stylesheet" type="text/css" href="css/ie7.css" />
        <![endif]-->


        <script type="text/javascript" src="lib/jquery-core.js"></script>
        <!--[if IE 6]>
        <script type="text/javascript" src="lib/jquery-pngfix.js"></script>
        <script type="text/javascript" src="lib/jquery-config-ie6.js"></script>
        <![endif]-->
        <script type="text/javascript" src="lib/jquery-cycle.js"></script>
        <script type="text/javascript" src="lib/jquery-form.js"></script>
        <script type="text/javascript" src="lib/jquery-lightbox.js"></script>
        <script type="text/javascript" src="lib/jquery-config.js"></script>
    </head>

    <body>
        <!-- splash -->
        <div id="in-splash-container">
            <div class="in-splash">
                <div class="in-splash-content">
                    <div class="in-splash-left">
                        <!-- splash text -->
                        <h1><%=res.getString("splash.title")%></h1>
                        <ul>
                            <li><%=res.getString("splash.subtitle1")%></li>
                            <li><%=res.getString("splash.subtitle2")%></li>
                            <li><%=res.getString("splash.subtitle3")%></li>
                            <li><%=res.getString("splash.subtitle4")%></li>
                            <li><%=res.getString("splash.subtitle5")%></li>
                            <li><%=res.getString("splash.subtitle6")%></li>

                        </ul>
                        <!-- /splash text -->
                    </div>
                    <!-- splash image -->
                    <span class="in-splash-right"><img src="images/in-splash.jpg" alt="example" width="460" height="283" /></span>
                    <!-- /splash image -->
                    <!-- splash buttons -->
                    <span class="in-splash-buttons">
                        <span class="splash-button"><a href="login.jsp"><%=res.getString("splash.start")%></a></span>
                        <span class="splash-link"><a href="tour.jsp"><%=res.getString("splash.demo")%></a></span>
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
                            <h2><%=res.getString("teaser.title1")%></h2>
                            <p><%=res.getString("teaser.text1")%></p>
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
                            <h2><%=res.getString("teaser.title2")%></h2>
                            <p><%=res.getString("teaser.text2")%></p>
                            <ul class="in-more">
                                <li class="morestart"><!--<a href="#">Learn More</a>--></li>
                                <!--li><a href="#">Preview</a></li-->
                            </ul>
                        </div>
                    </li>
                    <li class="endbenefits">
                        <span class="benefits-icon"><img src="images/benefits/benefits-003.gif" alt="example" width="70" height="70" /></span>
                        <div class="benefits-text">
                            <h2><%=res.getString("teaser.title3")%></h2>
                            <p><%=res.getString("teaser.text3")%></p>
                            <ul class="in-more">
                                <li class="morestart"><!--<a href="#">Learn More</a>--></li>
                                <!--li><a href="#">Preview</a></li-->
                            </ul>
                        </div>
                    </li>
                    <li>
                        <span class="benefits-icon"><img src="images/benefits/benefits-004.gif" alt="example" width="70" height="70" /></span>
                        <div class="benefits-text">
                            <h2><%=res.getString("teaser.title4")%></h2>
                            <p><%=res.getString("teaser.text4")%></p>
                            <ul class="in-more">
                                <li class="morestart"><!--<a href="#">Learn More</a>--></li>
                                <!--li><a href="#">Preview</a></li-->
                            </ul>
                        </div>
                    </li>
                    <li>
                        <span class="benefits-icon"><img src="images/benefits/benefits-005.gif" alt="example" width="70" height="70" /></span>
                        <div class="benefits-text">
                            <h2><%=res.getString("teaser.title5")%></h2>
                            <p><%=res.getString("teaser.text5")%></p>
                            <ul class="in-more">
                                <li class="morestart"><!--<a href="#">Learn More</a>--></li>
                                <!--li><a href="#">Preview</a></li-->
                            </ul>
                        </div>
                    </li>
                    <li class="endbenefits">
                        <span class="benefits-icon"><img src="images/benefits/benefits-006.gif" alt="example" width="70" height="70" /></span>
                        <div class="benefits-text">
                            <h2><%=res.getString("teaser.title6")%></h2>
                            <p><%=res.getString("teaser.text6")%></p>
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
                        <h2><%=res.getString("quote.title")%></h2>
                        <p><%=res.getString("quote.text")%><br /><br />
                            <%=res.getString("quote.email")%>
                            <br /><br />
                            <%=res.getString("quote.address")%>
                        </p>
                        <!-- /contact details -->
                    </div>
                    <div class="quote-form">
                        <!-- contact form -->
                        <form action="process.php" method="post" id="in-quote-form">
                            <fieldset>
                                <div class="form-left">
                                    <label for="fullname"><%=res.getString("quote.form.fullname")%> <span>*</span></label>
                                    <input id="fullname" name="fullname" type="text" />
                                </div>
                                <div class="form-right">
                                    <label for="emailaddress"><%=res.getString("quote.form.email")%> <span>*</span></label>
                                    <input id="emailaddress" name="emailaddress" type="text" />
                                </div>
                                <label for="message"><%=res.getString("quote.form.message")%> <span>*</span></label>
                                <textarea id="message" name="message" cols="10" rows="5"></textarea>
                                <button type="submit"><%=res.getString("quote.form.send")%></button>
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
        </script>
    </body>
</html>

