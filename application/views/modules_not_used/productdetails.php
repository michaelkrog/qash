<div class="box" style="width:100%;">

	<table cellspacing="0" cellpadding="3" border="0" width="100%">
		<tr>
			<td width="328" valign="top">
				<!-- IF <?=lang("productdetails_image_small")?> -->
					<div id="large_pic" class="box2" style="width:100%;"></div>
					<div class="box2" style="width:100%;">
						<!-- BEGIN productdetails_image_block -->
							<img src="<?=lang("productdetails_image_small")?>" width="60" onclick="open_pic('<?=lang("productdetails_image_crypt")?>',<?=lang("productdetails_image_height")?>,<?=lang("productdetails_image_width")?>);" class="makehand" title="<?=lang("productdetails_title")?>">
						<!-- END productdetails_image_block -->
					</div>
				<!-- ENDIF <?=lang("productdetails_image_small")?> -->
			</td><td valign="top">
				<div class="box2" style="width:100%;height:100%;">
					<div style="margin-bottom:15px;"><h1 class="headertext1"><?=lang("productdetails_title")?></h1></div>






			<table width="100%" cellspacing="0" cellpadding="0" border="0">


				<!-- IF <?=lang("productdetails_file_extension")?> -->
					<tr><td class="headertext2">* <?=lang("lang_module_11_file_available")?></td></tr>
					<tr><td height="10"></td></tr>
				<!-- ENDIF <?=lang("productdetails_file_extension")?> -->

				<tr><td>

					<div style="display:none;"><form method="get" name="cartform" id="cartform" action="cart_data.asp"></div>

					<!-- IF <?=lang("productdetails_available")?> -->
						<table width="100%" cellspacing="0" cellpadding="0" border="0">
						<tr><td valign="top">
							<input type="hidden" name="mode" value="add_product">
							<input type="hidden" name="id_product" value=<?=lang("productdetails_id")?>>
							<input type="hidden" name="history_count" value="1">

							<table>
								<tr><td width=25><input type="text" name="quantity" class="input" style="width:32px;" value="1"></td><td><a href="javascript:add_cart();" class="headertext2" nowrap><b><?=lang("lang_module_11_add_cart")?>Â»</b></a></td></tr>
							</table>

							<!-- IF <?=lang("productdetails_use_checkout_comment")?> -->
								<div class="headertext2"><?=lang("lang_module_11_checkout_comment")?></div>
								<div><textarea rows="2" class="input" name="comment"></ textarea></div>
							<!-- ENDIF <?=lang("productdetails_use_checkout_comment")?> -->

						</td></tr>
						</table>
					<!-- ELSE <?=lang("productdetails_available")?> -->
						<div id="productdetails_not_available" class="headertext1"><?=lang("lang_module_11_product_not_available")?></div>
					<!-- ENDIF <?=lang("productdetails_available")?> -->


						<!-- BEGIN productdetails_variant_block -->

						<!-- IF <?=lang("productdetails_variant_newlist")?> -->
							<select name="id_variants" class="input">
						<!-- ENDIF <?=lang("productdetails_variant_newlist")?> -->

						<option value="<?=lang("productdetails_variant_data_id")?>"><?=lang("productdetails_variant_name")?>: <?=lang("productdetails_variant_data")?> <?=lang("productdetails_variant_price")?> <!-- IF <?=lang("productdetails_variant_price")?> --><?=lang("website_currency")?><!-- ENDIF <?=lang("productdetails_variant_price")?> --></option>

						<!-- IF <?=lang("productdetails_variant_endlist")?> -->
							</select><br />
						<!-- ENDIF <?=lang("productdetails_variant_endlist")?> -->

						<!-- END productdetails_variant_block -->



				</td></tr>
				<tr><td height=3></td></tr>
				<tr><td>

					<table width="100%" cellspacing="0" cellpadding="0" border="0">
					<tr><td valign="top">

						<div class="box" style="width:100%;">
						<table width="100%" cellspacing="0" cellpadding="0" border="0">

							<!-- IF <?=lang("productdetails_discount")?> -->
								<tr><td width="140" class="headertext1"><?=lang("lang_module_11_your_price")?> (-<?=lang("productdetails_discount")?>%):</td><td class="headertext1"><?=lang("productdetails_discount_price")?> <?=lang("website_currency")?> (<?=lang("productdetails_unit")?>)</td></tr>
								<tr><td colspan="2" height="3"></td></tr>
							<!-- ENDIF <?=lang("productdetails_discount")?> -->

							<!-- IF <?=lang("productdetails_rebate")?> -->

								<!-- IF <?=lang("productdetails_discount")?> -->
									<tr style='text-decoration: line-through;'><td width="120"><b><?=lang("lang_module_11_save")?> <?=lang("productdetails_rebate")?>%:</b></td><td><?=lang("productdetails_rebate_price")?> <?=lang("website_currency")?> (<?=lang("productdetails_unit")?>)</td></tr>
								<!-- ELSE <?=lang("productdetails_discount")?> -->
									<tr><td width="140" class="headertext1"><?=lang("lang_module_11_save")?> <?=lang("productdetails_rebate")?>%:</td><td class="headertext1"><?=lang("productdetails_rebate_price")?> <?=lang("website_currency")?> (<?=lang("productdetails_unit")?>)</td></tr>
								<!-- ENDIF <?=lang("productdetails_discount")?> -->

								<tr><td colspan="2" height="3"></td></tr>

							<!-- ENDIF <?=lang("productdetails_rebate")?> -->

							<!-- IF <?=lang("productdetails_price_result")?> -->
								<tr style='text-decoration: line-through;'><td width="120"><b><!-- IF <?=lang("productdetails_auction_id")?> --><?=lang("lang_module_11_buy_now")?> <!-- ENDIF <?=lang("productdetails_auction_id")?> --><?=lang("lang_module_11_price")?>:</b></td><td><?=lang("productdetails_price")?> <?=lang("website_currency")?> (<?=lang("productdetails_unit")?>)</td></tr>
							<!-- ELSE <?=lang("productdetails_price_result")?> -->
								<tr ><td width="140" class="headertext1"><!-- IF <?=lang("productdetails_auction_id")?> --><?=lang("lang_module_11_buy_now")?> <!-- ENDIF <?=lang("productdetails_auction_id")?> --><?=lang("lang_module_11_price")?>:</td><td class="headertext1"><?=lang("productdetails_price")?> <?=lang("website_currency")?> (<?=lang("productdetails_unit")?>)</td></tr>
							<!-- ENDIF <?=lang("productdetails_price_result")?> -->

							<tr><td colspan="2" height="3"></td></tr>

							<tr><td><b><?=lang("lang_module_11_product_number")?>:</b></td><td>#<?=lang("productdetails_manufacture_id")?></td></tr>

							<tr><td><b><?=lang("lang_module_11_weight")?>:</b></td><td><?=lang("productdetails_weight")?> kg</td></tr>

							<!-- IF <?=lang("productdetails_auction_id")?> -->
								<tr><td colspan="2" height="3"></td></tr>
								<tr><td><b><?=lang("lang_module_11_auction_no")?>:</b></td><td>#<?=lang("productdetails_auction_id")?></td></tr>
								<tr><td colspan="2" height="3"></td></tr>
								<tr><td><b><?=lang("lang_module_11_auction_contains")?>:</b></td><td>1 <?=lang("productdetails_unit")?></td></tr>
								<tr><td colspan="2" height="3"></td></tr>
								<tr><td><b><?=lang("lang_module_11_next_bid")?>:</b></td><td><div id="bidsize"></div></td></tr>
								<tr><td colspan="2" height="3"></td></tr>
								<tr><td class="headertext2" style="font-size:13px;"><b><?=lang("lang_module_11_auction_ends")?>:</b></td><td><div id="countdown" class="headertext1" style="font-size:13px;"></div></td></tr>

								<!-- IF <?=lang("productdetails_auction_extended")?> -->
									<tr><td colspan="2" class="headertext2"><?=lang("lang_module_11_auction_extended")?></td></tr>
								<!-- ENDIF <?=lang("productdetails_auction_extended")?> -->

							<!-- ENDIF <?=lang("productdetails_auction_id")?> -->
						</table>
						</div>

					</td></tr>
					</table>

					<!-- IF <?=lang("productdetails_auction_id")?> -->
					<div class="box2" style="width:100%">
						<input type="hidden" name="id_auction" value="<?=lang("productdetails_auction_id")?>">
						<input type="hidden" name="idp" value="<?=lang("productdetails_id")?>">
						<input type="hidden" name="bid" id="bid" value="">

						<div class="auction_box">
							<div id="bidbox" class="headertext2" style="font-size:18px;"></div>
						</div>
						<div id="auction_bids"></div>
					</div>
					<!-- ENDIF <?=lang("productdetails_auction_id")?> -->



					<div class="box" style="width:100%;"><?=lang("productdetails_description_long")?></div>

					<div style="display:none;"></form></div>

				</td></tr>
			</table>



				</div>

			</td>
		</tr>
	</table>

</div>

<iframe name="bidframe" id="bidframe" width="0" height="0" frameborder="0"></iframe>


<!-- IF <?=lang("productdetails_added")?> -->
<script language="javascript">
	alert("<?=lang("lang_module_11_added_to_cart")?>");
</script>
<!-- ENDIF <?=lang("productdetails_added")?> -->

<script language="javascript">

<!-- IF <?=lang("productdetails_auction_id")?> -->
	display_bids(<?=lang("productdetails_auction_id")?>);
<!-- ENDIF <?=lang("productdetails_auction_id")?> -->

<!-- IF <?=lang("productdetails_image_crypt")?> -->
	open_pic('<?=lang("productdetails_image_crypt")?>',<?=lang("productdetails_image_height")?>,<?=lang("productdetails_image_width")?>);
	fader(0,'large_pic',100,10,0,'');
<!-- ENDIF <?=lang("productdetails_image_crypt")?> -->


function display_bids(id_auction) {
	frames['bidframe'].location.href="auction_bids.asp?id_auction=" + id_auction;
}


function open_pic(crypt,height,width) {
	document.getElementById("large_pic").innerHTML="<a href=javascript:show_pic('" + crypt + "'," + height + "," + width + ");><img id='product_image' src='/upload/files/" + crypt + "_mid.jpg' border='0' width='320'>";
}


function place_bid() {
<!-- IF <?=lang("customer_name")?> -->
	var bid=document.getElementById("bid_value").value;

	if (/[^0-9]/.test(bid))
		alert("<?=lang("lang_module_11_enter_equal_number")?>");
	else if(confirm("<?=lang("lang_module_11_confirm_bid")?> " + bid + " <?=lang("website_currency")?>?"))
		{
		document.getElementById("bid").value=bid;
		document.cartform.target="dataframe";
		document.cartform.action="auction_data.asp";
		document.cartform.submit();
		}
<!-- ELSE <?=lang("customer_name")?> -->
	alert("<?=lang("lang_module_11_login_to_bid")?>");
	document.location.href="/?page=login";
<!-- ENDIF <?=lang("customer_name")?> -->
}


function add_cart() {
	document.cartform.action="<?=lang("productdetails_cart_url")?>";
	document.cartfo