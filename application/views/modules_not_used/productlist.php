<div class="box" style="width:100%;text-align:center;margin-bottom:5px;">
<!-- IF NOT {{product_id}} -->
	<p class="headertext1">{{lang_module_10_no_products}}</p>
<!-- ELSE {{product_id}} -->

	<!-- BEGIN product_block -->

	<span class="box2" style="width:155px;margin:2px 2px 5px 2px;text-align:center;display:-moz-inline-block;display:-moz-inline-box;display:inline-block;vertical-align: top;">

		<table width="100%">
			<tr><td valign="top" align="center">
				<div style="height:28px;"><a href="{{product_url}}" class="headertext4" title="{{product_title}}">{{product_title}}</a></div>
				<div style="width:auto;height:auto;text-align:center;margin-top:3px;">

					<!-- IF {{product_auction_maxbid}} -->
						<span class="headertext2">{{lang_module_10_bid}} {{product_auction_maxbid}} {{website_currency}}</span>
						<div id="countdown_{{product_id}}"></div>
						<script language="javascript">getTimeLeft('{{product_auction_ends_jsformat}}','countdown_{{product_id}}');</script>
					<!-- ELSE {{product_auction_maxbid}} -->
						<!-- IF {{product_rebate}} -->
							<span style="text-decoration: line-through;">{{product_price}}</span>
							<span class="headertext2">{{product_rebate_price}} {{website_currency}}</span>
							<div style="width:100%;text-align:center;margin-top:0px;">{{lang_module_10_save}} {{product_rebate}}%</div>
						<!-- ELSE {{product_rebate}} -->
							<span class="headertext2">{{product_price}} {{website_currency}}</span>
							<div style="width:100%;text-align:center;margin-top:0px;"> </div>
						<!-- ENDIF {{product_rebate}} -->
					<!-- ENDIF {{product_auction_maxbid}} -->
				</div>
			</td></tr>
			<tr><td>
				<a href="{{product_url}}" title="{{product_title}}">
				<!-- IF {{product_image_url}} -->
					<img style="width:150px;height:140px;padding-bottom:5px;margin-bottom:3px;" src="{{product_image_url}}">
				<!-- ELSE {{product_image_url}} -->
					<div class="box makehand" style="width:auto;height:140px;"></div>
				<!-- ENDIF {{product_image_url}} -->
				</a>
			</td></tr>
		</table>
	</span>

	<!-- END product_block -->

<!-- ENDIF {{product_id}} -->
</div>

<div class="box headertext2" style="width:100%;margin-bottom:5px;">
	<div style="width:100%;text-align:center;margin-bottom:3px;">
		<!-- IF {{product_page_previous}} -->
			<a href="{{product_page_previous}}" class="headertext2">«{{lang_module_10_previous_page}}</a> |
		<!-- ENDIF {{product_page_previous}} -->
		<!-- BEGIN product_page_block -->
			<!-- IF {{product_page_selected}} -->
				<u>{{product_page}}</u>
			<!-- ELSE {{product_page_selected}} -->
				<a href="{{product_page_url}}">{{product_page}}</a>
			<!-- ENDIF {{product_page_selected}} -->

		<!-- END product_page_block -->
		<!-- IF {{product_page_next}} -->
			| <a href="{{product_page_next}}" class="headertext2">{{lang_module_10_next_page}}»</a>
		<!-- ENDIF {{product_page_next}} -->
	</div>
	<div style="width:100%;text-align:center;font-size:9px;">{{product_showing_from}} - {{product_showing_to}} ({{product_showing_total}}) {{lang_module_10_results}}</div>
</div>


<!-- IF {{product_auction}} -->
	<script language="javascript">
	setTimeout("reload_site()",60000);
	function reload_site() {
		document.location.href=document.location.href;
	}
	</script>
<!-- ENDIF {{product_auction}} -->