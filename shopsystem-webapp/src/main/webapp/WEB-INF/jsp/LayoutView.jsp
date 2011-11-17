<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<html>

    <head>
	<title>Administration</title>

	<meta name="title" content="Administration" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

	<script type="text/javascript">

    </script>

	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script>
	<script type="text/javascript" src="http://cdn.jquerytools.org/1.2.5/all/jquery.tools.min.js"></script>
	<script type="text/javascript" src="/js/init_admin.js"></script>
	<script type="text/javascript" src="/js/functions.js"></script>

	<link rel="icon" type="image/png" href="http://www.shoppinnet.com/graphic/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="/css/default_v2.css" />
    </head>

    <body>
	<div id="page_container">

	    <div id="page_margin_top">

		<!-- Section Margin Top -->
		   
		<!-- // Section Margin Top -->

	    </div>

	    <div id="page_holder">

		<div id="page_top">

		    <div id="page_logo">
			<span id="page_logo_text">ShoppinNet.com</span><span id="page_logo_reg">&reg;</span>
		    </div>

		  
		    <!-- Section Top -->
			
		    <!-- // Section Top -->

		</div>



		<table id="page_mid">
		    <tr>
			<td id="section_left">

			    <!-- Section Left -->
				<div id="navigation">
				    <div>
					    <h2 class="current">Settings</h2>
					    <div class="pane" style="display:block;">
						<ul>
						    <li><a href="javascript:ajax('/index.php/admin/organisation/edit','','dialog','');">Organisation Settings</a></li>
						    <li><a href="javascript:ajax('/index.php/admin/website/edit','','dialog','');">Websites</a></li>
						    <br />
						    <li><a href="javascript:ajax('/index.php/admin/domain/listing','','section_mid','');">Domains</a></li>
						    <li><a href="javascript:ajax('/index.php/admin/domain/edit','','section_mid','');">Add domain</a></li>
						    <br />
						    <li><a href="javascript:ajax('/index.php/admin/payment_method/listing','','section_mid','');">Payment Methods</a></li>
						    <li><a href="javascript:ajax('/index.php/admin/payment_method/edit','','section_mid','');">Add Payment Method</a></li>
						</ul>
					    </div>
				    </div>
				    <div>
					    <h2>Users</h2>
					    <div class="pane">
						<ul>
						    <li><a href="javascript:ajax('/index.php/admin/user/listing','','section_mid','');">User Overview</a></li>
						    <li><a href="javascript:ajax('/index.php/admin/user/edit','','dialog','');">Add User</a></li>
						    <br />
						    <li><a href="javascript:ajax('/index.php/admin/user_group/listing','','section_mid','');">User Group Overview</a></li>
						    <li><a href="javascript:ajax('/index.php/admin/user_group/edit','','dialog','');">Add User Group</a></li>

						</ul>
					    </div>
				    </div>
				    <div>
					    <h2>Categories</h2>
					    <div class="pane">
						<ul>
						    <li><a href="">Category Overview</a></li>
						    <li><a href="">Add Category</a></li>
						</ul>
					    </div>
				    </div>
				     <div>
					    <h2>Products</h2>
					    <div class="pane">
						<ul>
						    <li><a href="">Product Overview</a></li>
						    <li><a href="">Add Product</a></li>
						</ul>
					    </div>
				    </div>
				     <div>
					    <h2>Newsletters</h2>
					    <div class="pane">
						<ul>
						    <li><a href="">Newsletter Overview</a></li>
						    <li><a href="">Outbox</a></li>
						    <li><a href="">Add Newsletter</a></li>
						</ul>
					    </div>
				    </div>
				</div>
			    <!-- // Section Left -->

			</td>

			<td id="section_mid">
			    <div id="section_mid_holder">
			    <!-- Section Mid -->
				
			    <!-- // Section Mid -->
			    </div>
			</td>

		    </tr>
		</table>


		<div id="page_bottom">

		    <!-- Section Right -->
			
		    <!-- // Section Right -->

		</div


	    </div>


	     <div id="page_margin_bottom">

		<!-- Section Margin Bottom -->
		    
		<!-- // Section Margin Bottom -->

	    </div>


	</div>

	<div id="message"></div>
	
	<div id="dialog">
	    <div class="header header_current">Title</div>
	    <div class="content">Content</div>
	</div>

	</body>
	
</html>
		