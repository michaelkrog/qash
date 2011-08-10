<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<!--
********************************************************
*** Copyright @ ShoppinNet.com - All Rights Reserved ***
********************************************************
********************************************************
*** Start your own online store today -  100% free!! ***
***               www.ShoppinNet.com                 ***
********************************************************
-->






















<html>
    
    <head>
	<title><?=$page_title?></title>

	<meta name="title" content="<?=$page_title?>">
	<meta name="description" content="<?=$page_description?>">
	<meta name="keywords" content="<?=$page_keywords?>">
	<meta http-equiv="Content-Type" content="text/html; charset=<?=$charset?>">

	<link rel="icon" type="image/png" href="http://www.shoppinnet.com/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="/css/default.css" />
	<link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.11/themes/smoothness/jquery-ui.css" rel="stylesheet" />
	
	<style type="text/css">
	    <?/*=$template*/?>
	</style>

	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script>
	<script type="text/javascript" src="http://cdn.jquerytools.org/1.2.5/all/jquery.tools.min.js"></script>
	<script src="/application/assets/js/functions.js" type="text/javascript"></script>
	<script src="/application/assets/js/init.js" type="text/javascript"></script>

    </head>

    <body>
	<div id="page_container">

	    <div id="page_margin_top">

		<!-- Section Margin Top -->
		    <?=$section_6?>
		<!-- // Section Margin Top -->

	    </div>

	    <div id="page_holder">

		<div id="page_top">

		    <div id="page_logo">
			<?
                        if ($website_logo != "")
				echo("<img src=\"$website_logo\">");
			else
				echo("<div>".$website_name."</div>");
			?>
		    </div>

		   		    
		    <!-- ADS -->
		    <div id='ads_link' class='headertext1'></div>
		    <div id='ads_box' style='position:relative;height:100px;margin-bottom:5px;' class='box'>
			<div style='position:absolute;top:0px;'>
			    <script type='text/javascript'><!--
			    google_ad_client = 'pub-2754454392338775';
			    google_ad_width = 728;
			    google_ad_height = 90;
			    google_ad_format = '728x90_as';
			    google_ad_type = 'text';
			    google_ad_channel = '7308919970';
			    google_color_border = RGBcss2Hex($('.ui-widget-content').css('background-color'));
			    google_color_bg = RGBcss2Hex($('.ui-widget-content').css('background-color'));
			    google_color_link = RGBcss2Hex($('.ui-widget-header').css('background-color'));
			    google_color_text = RGBcss2Hex($('.ui-widget-content').css('color'));
			    google_color_url = RGBcss2Hex($('.ui-widget-header').css('background-color'));
			    //-->
			    </script>
			    
			    <script type='text/javascript'
			    src='http://pagead2.googlesyndication.com/pagead/show_ads.js'>
			    </script>
			
			</div>

			<div class="ads_text">
			    <script type="text/javascript">
				document.write("<a href='http://www.shoppinnet.com/?page=storecreate&a=arbejde-hjemmefra-tjen-penge-gratis-webshop-online-butik' target='_blank' style='font-size:12px;'><u><b>Tjen penge hjemmefra: Start en online butik - GRATIS!</u></b></a>");
			    </script>
			    <span style='margin-left:10px;'>
				<a href='http://www.shoppinnet.com/?page=storecreate&a=arbejde-hjemmefra-tjen-penge-gratis-webshop-online-butik' title='Start din egen gratis webshop / online butik'>www.ShoppinNet.com</a>
			    </span>
			</div>

		    </div>
		    <style type="text/css">.ads_text{position:absolute;top:76px;left:30px;margin-left: 6px;} .ads_text, .ads_text a:link, .ads_text a:visited, .ads_text a:hover {font-size:9px;font-family:verdana;}</style>
		    <!-- // ADS -->


		    <!-- Section Top -->
			<?=$section_1?>
		    <!-- // Section Top -->

		</div>



		<table id="page_mid">
		    <tr>
			<?if ($section_3 != "") {?>
			    <td id="section_left" style="width:200px;">

				<!-- Section Left -->
				    <?=$section_3?>
				<!-- // Section Left -->

			    </td>
			<?}?>

			<td id="section_mid">

			    <!-- Section Mid -->
				<?//=$content?>
				<?=$section_4?>
			    <!-- // Section Mid -->

			</td>

			<?if ($section_5 != "") {?>
			    <td id="section_right" style="width:200px;">

				<!-- Section Right -->
				    <?=$section_5?>
				<!-- // Section Right -->

			    </td>
			<?}?>
		    </tr>
		</table>


		<div id="page_bottom">

		    <!-- Section Right -->
			<?=$section_2?>
		    <!-- // Section Right -->

		</div


	    </div>


	     <div id="page_margin_bottom">

		<!-- Section Margin Bottom -->
		    <?=$section_7?>
		<!-- // Section Margin Bottom -->

	    </div>


	</div>

   


    </body>
</html>