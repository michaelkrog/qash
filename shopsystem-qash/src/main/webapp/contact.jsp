<%--
    Document   : index
    Created on : 16-05-2011, 11:20:51
    Author     : michaelzachariassenkrog
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Qash - Contact information</title>
        <meta name="description" content="Login for Qash" />
        <meta name="keywords" content="login, qash"/>
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

    <body onload="document.getElementById('username').focus()">
         <!-- splash -->
        <div id="in-splash-container-small">
            <div class="in-splash">

            </div>
        </div>
        <!-- /splash -->

        <jsp:include page="inc/header.jsp" />
        <jsp:include page="inc/navigator.jsp" />

        
        <!-- benefits -->
        <div id="in-benefits-container" >
    
            <div class="in-benefits">
                <h1>Contact Qash</h1>
            <br/>
            <br/>
            <ul>

                    <!-- benefit item -->
                    <li style="width:430px">
                        <span class="benefits-icon"><img src="images/contact/home.png" alt="Address" width="70" height="70" /></span>
                        <div class="benefits-text" style="width:315px;padding-right:45px;">
                            <h2>Address</h2>
                            <p>
                                Apaq<br />
                                Stovringparken 10<br />
                                DK-9530 Stovring<br />
                                <i>Scandiavian Denmark</i>
                            </p>

                        </div>
                    </li>
                    <!-- /benefit item -->

                    <li style="width:430px" class="endbenefits">
                        <span class="benefits-icon"><img src="images/contact/email.png" alt="Direct contact" width="70" height="70" /></span>
                        <div class="benefits-text" style="width:315px;padding-right:45px">
                            <h2>Direct contact</h2>
                            <p>
                                <table>
                                    <tr><td>Sales:</td><td>contact@qashapp.com</td></tr>
                                    <tr><td>Support:</td><td>support@qashapp.com</td></tr>

                                </table>
                            </p>
                        </div>
                    </li>




                </ul>
                <div class="clear"></div>
            </div>
        </div>
        <!-- /benefits -->

        <jsp:include page="inc/footer.jsp" />


    </body>
</html>
