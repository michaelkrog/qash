<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<html>

    <head>
	<title>${administration}</title>

	<meta name="title" content="${administration}" />
	<meta http-equiv="Content-Type" content="text/html; charset=<?//=$charset?>" />

	<script type="text/javascript">

    </script>

	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script>
	<script type="text/javascript" src="http://cdn.jquerytools.org/1.2.5/all/jquery.tools.min.js"></script>
	<script type="text/javascript" src="/application/assets/js/init_admin.js"></script>
	<script type="text/javascript" src="/application/assets/js/functions.js"></script>

	<link rel="icon" type="image/png" href="http://www.shoppinnet.com/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="/application/assets/css/admin/default_v2.css" rel="stylesheet" />
    </head>

    <body>
	<div id="page_container">

	    <div id="page_margin_top">

		<!-- Section Margin Top -->
		    <?//=$section_6?>
		<!-- // Section Margin Top -->

	    </div>

	    <div id="page_holder">

		<div id="page_top">

		    <div id="page_logo">
			<span id="page_logo_text">ShoppinNet.com</span><span id="page_logo_reg">&reg;</span>
		    </div>

		  
		    <!-- Section Top -->
			<?//=$section_1?>
		    <!-- // Section Top -->

		</div>



		<table id="page_mid">
		    <tr>
			<td id="section_left">

			    <!-- Section Left -->
				<div id="navigation">
				    <div>
					    <h2 class="current">${settings}</h2>
					    <div class="pane" style="display:block;">
						<ul>
						    <li><a href="javascript:ajax('/index.php/admin/organisation/edit','','dialog','');">${organisation_settings}</a></li>
						    <li><a href="javascript:ajax('/index.php/admin/website/edit','','dialog','');">${website_settings}</a></li>
						    <br />
						    <li><a href="javascript:ajax('/index.php/admin/domain/listing','','section_mid','');">${domains}</a></li>
						    <li><a href="javascript:ajax('/index.php/admin/domain/edit','','section_mid','');">${create_domain}</a></li>
						    <br />
						    <li><a href="javascript:ajax('/index.php/admin/payment_method/listing','','section_mid','');">${payment_methods}</a></li>
						    <li><a href="javascript:ajax('/index.php/admin/payment_method/edit','','section_mid','');">${create_payment_method}</a></li>
						</ul>
					    </div>
				    </div>
				    <div>
					    <h2>${users}</h2>
					    <div class="pane">
						<ul>
						    <li><a href="javascript:ajax('/index.php/admin/user/listing','','section_mid','');">${user_overview}</a></li>
						    <li><a href="javascript:ajax('/index.php/admin/user/edit','','dialog','');">${create_user}</a></li>
						    <br />
						    <li><a href="javascript:ajax('/index.php/admin/user_group/listing','','section_mid','');">${user_group_overview}</a></li>
						    <li><a href="javascript:ajax('/index.php/admin/user_group/edit','','dialog','');">${create_user_group}</a></li>

						</ul>
					    </div>
				    </div>
				    <div>
					    <h2>${categories}</h2>
					    <div class="pane">
						<ul>
						    <li><a href="">${category_overview}</a></li>
						    <li><a href="">${create_category}</a></li>
						</ul>
					    </div>
				    </div>
				     <div>
					    <h2>${products}</h2>
					    <div class="pane">
						<ul>
						    <li><a href="">${product_overview}</a></li>
						    <li><a href="">${create_product}</a></li>
						</ul>
					    </div>
				    </div>
				     <div>
					    <h2>${newsletters}</h2>
					    <div class="pane">
						<ul>
						    <li><a href="">${newsletter_overview}</a></li>
						    <li><a href="">${outbox}</a></li>
						    <li><a href="">${create_newsletter}</a></li>
						</ul>
					    </div>
				    </div>
				</div>
			    <!-- // Section Left -->

			</td>

			<td id="section_mid">
			    <div id="section_mid_holder">
			    <!-- Section Mid -->
				<?//=$content?>
				<?//=$section_4?>
			    <!-- // Section Mid -->
			    </div>
			</td>

		    </tr>
		</table>


		<div id="page_bottom">

		    <!-- Section Right -->
			<?//=$section_2?>
		    <!-- // Section Right -->

		</div


	    </div>


	     <div id="page_margin_bottom">

		<!-- Section Margin Bottom -->
		    <?//=$section_7?>
		<!-- // Section Margin Bottom -->

	    </div>


	</div>

	<div id="message"></div>
	
	<div id="dialog">
	    <div class="header header_current">Overskrift</div>
	    <div class="content">Indhold</div>
	</div>

	</body>
	
</html>
		