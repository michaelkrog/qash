<!-- IF <?=lang("cart_isempty")?> -->

	<p><?=lang("module_16_cart_is_empty")?>!</p>

<!-- ELSE <?=lang("cart_isempty")?> -->

			<table style="width:100%;">
			<tr><td style="width:50%;">

				<div class="box_header"><?=lang("module_16_order")?></div>
				<div class="box" style="height:150px;">
				<table style="width:100%;padding:5px;">
					<tr><td class="box_bottom_padding" class="headertext4"><?=lang("module_16_status")?></td><td style="text-align:right;" class="headertext4"><b><?=lang("cart_status")?></b></td></tr>
					<tr><td class="box_bottom_padding"><?=lang("module_16_order_number")?></td><td style="text-align:right;"><?=lang("cart_order")?></td></tr>

					<!-- IF <?=lang("cart_invoice")?> -->
						<tr><td class="box_bottom_padding"><?=lang("module_16_invoice_number")?></td><td style="text-align:right;"><?=lang("cart_invoice")?></td></tr>
					<!-- ENDIF <?=lang("cart_invoice")?> -->

					<tr><td class="box_bottom_padding"><?=lang("module_16_date")?></td><td style="text-align:right;"><?=lang("cart_orderdate")?></td></tr>

					<tr><td class="box_bottom_padding" valign="top" class="headertext4"><?=lang("module_16_paymethod")?></td><td style="text-align:right;" class="headertext4"><b><?=lang("cart_payment")?></b></td></tr>
					<tr><td class="box_bottom_padding"><?=lang("cart_payment_description")?></td><td style="text-align:right;" class="headertext4"><!-- IF <?=lang("cart_payment_identification_text")?> --><?=lang("cart_payment_identification_text")?>: <?=lang("cart_payment_identification")?><!-- ENDIF <?=lang("cart_payment_identification_text")?> --></td></tr>
				</table>
				</div>

			</td>
			<td class="pagesplit"></td>
			<td style="width:50%;">


				<!-- IF <?=lang("cart_paid")?> -->

				<div class="box_header"><?=lang("module_16_customer")?></div>
				<div class="box" style="width:auto;min-height:150px;">
					<table style="width:100%;padding:5px;">
						<tr><td style="width:50%;" class="headertext4 box_bottom_padding"><?=lang("module_16_company")?><td><td style="width:50%;text-align:right;"><?=lang("cart_user_company")?></td></tr>
						<tr><td class="headertext4 box_bottom_padding"><?=lang("module_16_name")?><td><td style="text-align:right;"><?=lang("cart_user_name")?></td></tr>
						<tr><td class="headertext4 box_bottom_padding"><?=lang("module_16_address")?><td><td style="text-align:right;"><?=lang("cart_user_address")?></td></tr>
						<tr><td class="headertext4 box_bottom_padding"><?=lang("module_16_address")?><td><td style="text-align:right;"><?=lang("cart_user_address2")?></td></tr>
						<tr><td class="headertext4 box_bottom_padding"><?=lang("module_16_zip")?><td><td style="text-align:right;"><?=lang("cart_user_zip")?></td></tr>
						<tr><td class="headertext4 box_bottom_padding"><?=lang("module_16_city")?><td><td style="text-align:right;"><?=lang("cart_user_city")?></td></tr>
						<tr><td class="headertext4 box_bottom_padding"><?=lang("module_16_telephone")?><td><td style="text-align:right;"><?=lang("cart_user_telephone")?></td></tr>
						<tr><td class="headertext4 box_bottom_padding"><?=lang("module_16_email")?><td><td style="text-align:right;"><?=lang("cart_user_email")?></td></tr>
					</table>
				</div>

				<!-- ELSE <?=lang("cart_paid")?> -->
				<div class="box_header"><?=lang("module_16_features")?></div>
				<div class="box" style="height:150px;">

					<table style="width:auto;padding:5px;">
						<tr><td class="headertext4"><?=lang("module_16_use_code")?>:</td><td style="text-align:right;"><form style="display:inline;" method="post" name="codeform" action="cart_data.asp?mode=add_discount_code&order=<?=lang("cart_order")?>"><input name="code" id="code" class="input" style="width:75px;"><span class="button makehand" style="margin-left:5px;" onclick="javascript:document.codeform.submit();"><?=lang("module_16_save")?></span></form></td></tr>
						<tr><td style="width:50%;" class="headertext4"><?=lang("module_16_select_paymethod")?>:</td><td style="text-align:right;">

							<div class="hidden"><form name="payform" method="post" action="<?=lang("cart_proceed_url")?>"></div>
							<input type="hidden" name="redirect_url" value="<?=lang("cart_payment_url")?>">
							<input type="hidden" name="order" value="<?=lang("cart_order")?>">
							<select name="id_payment" class="input" onchange="document.getElementById('cart_submit_button').innerHTML='<img src=_grafik/reading.gif style=width:15px;>';document.location.href='cart_data.asp?mode=select_payment&id_payment='+this.value+'&order=<?=lang("cart_order")?>';">

								<!-- BEGIN cart_payment_block -->
									<option value="<?=lang("cart_payment_id")?>" <!-- IF <?=lang("cart_payment_selected")?> --> selected<!-- ENDIF <?=lang("cart_payment_selected")?> -->><?=lang("cart_payment_name")?></option>
								<!-- END cart_payment_block -->

							</select>
							<div class=hidden></form></div>

						</td></tr>
					</table>
					<br />
					<span class="button makehand" id="cart_submit_button" onclick="javascript:document.payform.submit();"><?=lang("module_16_pay_now")?></span>
				</div>


				<!-- ENDIF <?=lang("cart_paid")?> -->

			</td></tr>
			</table>


			<table style="width:100%;"><tr><td>
			<table class="box" style="width:100%;">

			<!-- BEGIN cart_product_block -->
				<tr class="box_bottom_padding">
				<td valign="top" style="width:75px;">

					<!-- IF <?=lang("cart_product_fee")?> -->
					<!-- ELSE <?=lang("cart_product_fee")?> -->

						<!-- IF <?=lang("cart_product_image")?> -->
							<img src="<?=lang("cart_product_image")?>" style="width:70px;">
						 <!-- ELSE <?=lang("cart_product_image")?> -->
						 	<span class="box2" style="width:70px;height:65px;"></span>
						 <!-- ENDIF <?=lang("cart_product_image")?> -->

					<!-- ENDIF <?=lang("cart_product_fee")?> -->

				</td><td valign="top">

					<div class="headertext4" style="margin-bottom:5px;">

					<!-- IF <?=lang("cart_product_url")?> -->
						<a class="headertext4" href=<?=lang("cart_product_url")?>><u><?=lang("cart_product_title")?></u></a>
					<!-- ELSE <?=lang("cart_product_url")?> -->
						<?=lang("cart_product_title")?>
					<!-- ENDIF <?=lang("cart_product_url")?> -->

					</div>

					<div style="margin-bottom:5px;">
					<!-- IF <?=lang("cart_product_delete_url")?> -->
						<span class="button" onclick="javascript:document.location.href='<?=lang("cart_product_delete_url")?>';" style="padding-top:0px;padding-bottom:0px;"><?=lang("module_16_delete")?></span>
					<!-- ENDIF <?=lang("cart_product_delete_url")?> -->

					<!-- IF <?=lang("cart_product_fee")?> -->
					<!-- ELSE <?=lang("cart_product_fee")?> -->
						<?=lang("cart_product_quantity")?> <?=lang("cart_product_unit")?> Varenr: <?=lang("cart_manufacture_id")?>
					<!-- ENDIF <?=lang("cart_product_fee")?> -->

					<!-- IF <?=lang("cart_product_price")?> -->
						(<?=lang("cart_product_price")?> <?=lang("website_currency")?>)
					<!-- ENDIF <?=lang("cart_product_price")?> -->

					<!-- IF <?=lang("cart_product_auction")?> -->
						(<?=lang("module_16_auction")?>)
					<!-- ENDIF <?=lang("cart_product_auction")?> -->
					</div>


					<!-- IF <?=lang("cart_product_variants")?> -->
						<?=lang("cart_product_variants")?>
					<!-- ENDIF <?=lang("cart_product_variants")?> -->

					<!-- IF <?=lang("cart_paid")?> -->
						<!-- IF <?=lang("cart_product_download_url")?> -->
							<br /><a href="<?=lang("cart_product_download_url")?>" target="dataframe" class="headertext2"><u><b><?=lang("module_16_download")?></b></u></a>
						<!-- ENDIF <?=lang("cart_product_download_url")?> -->
					<!-- ENDIF <?=lang("cart_paid")?> -->

				</td>

				<td style="width:100px;text-align:right;" class="headertext4" valign="top"><?=lang("cart_linesum")?> <?=lang("website_currency")?></td></tr>

			<!-- END cart_product_block -->

			<tr><td></td><td valign="top" class="headertext4"><?=lang("module_16_price_total")?>:</td><td style="text-align:right;" class="headertext4"><u><?=lang("cart_totalprice")?> <?=lang("website_currency")?></u></td></tr>
			</table>

			</td></tr></table>

<!-- ENDIF <?=lang("cart_isempty")?> -->
