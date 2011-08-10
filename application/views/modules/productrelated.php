<!-- IF {{product_related_title}} -->
	<div class="box_header">{{lang_module_12_related_products}}</div>
	<div class="box" style="width:100%;text-align:center;margin-bottom:5px;">

	<!-- BEGIN product_related_block -->

	<span class="box2" style="width:155px;margin:2px 2px 5px 2px;text-align:center;display:-moz-inline-block;display:-moz-inline-box;display:inline-block;vertical-align: top;">

		<table width="100%">
			<tr><td valign="top" align="center">
				<div style="height:28px;"><a href="{{product_related_url}}" class="headertext4" title="{{product_related_title}}">{{product_related_title}}</a></div>
				<div style="width:auto;text-align:center;margin-top:3px;">

					<!-- IF {{product_related_auction_maxbid}} -->
						<span class="headertext2">{{lang_module_12_bid}} {{product_related_auction_maxbid}} {{website_currency}}</span>
						<div id="countdown_{{product_related_id}}"></div>
						<script language="javascript">getTimeLeft('{{product_related_auction_ends_jsformat}}','countdown_{{product_related_id}}');</script>
					<!-- ELSE {{product_related_auction_maxbid}} -->
						<!-- IF {{product_related_rebate}} -->
							<span style="text-decoration: line-through;">{{product_related_price}}</span>
							<span class="headertext2">{{product_related_rebate_price}} {{website_currency}}</span>
							<div style="width:100%;text-align:center;margin-top:0px;">{{lang_module_12_save}} {{product_related_rebate}}%</div>
						<!-- ELSE {{product_related_rebate}} -->
							<span class="headertext2">{{product_related_price}} {{website_currency}}</span>
							<div style="width:100%;text-align:center;margin-top:0px;">Â </div>
						<!-- ENDIF {{product_related_rebate}} -->
					<!-- ENDIF {{product_related_auction_maxbid}} -->
				</div>
			</td></tr>
			<tr><td>
				<a href="{{product_related_url}}" title="{{product_related_title}}">
				<!-- IF {{product_related_image_url}} -->
					<img style="width:150px;height:140px;padding-bottom:5px;margin-bottom:3px;" src="{{product_related_image_url}}">
				<!-- ELSE {{product_related_image_url}} -->
					<div class="box makehand" style="width:150px;height:140px;"></div>
				<!-- ENDIF {{product_related_image_url}} -->
				</a>
			</td></tr>
		</table>
	</span>

	<!-- END product_related_block -->

	</div>
<!-- ENDIF {{product_related_title}} -->