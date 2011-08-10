<script language="javascript">
		function limitText(limitField, limitNum) {
			if (limitField.value.length > limitNum) {
				limitField.value = limitField.value.substring(0, limitNum);
			} else {

				document.getElementById("countdown").innerHTML = "<?=lang("module_14_characters_left")?>: " + (limitNum - limitField.value.length);
			}
		}

		function show_shippingaddress(data) {
			if(document.getElementById("shippingaddress").checked){
				fader(100,'div_shipping',0,-10,2,'');
				//document.getElementById("div_shipping").style.display = 'none';
			}
			else{
				document.getElementById("div_shipping").style.display = 'block';
				fader(0,'div_shipping',100,10,0,'');
			}
		}

		function mainform_submit() {
			if(document.mainform.name.value=='')
				alert("<?=lang("module_14_name_missing")?>!");
			else if(document.mainform.address.value=='')
				alert("<?=lang("module_14_address_missing")?>!");
			else if(document.mainform.zip.value=='')
				alert("<?=lang("module_14_zip_missing")?>!");
			else if(document.mainform.city.value=='')
				alert("<?=lang("module_14_city_missing")?>!");
			else if(document.mainform.telephone.value=='')
				alert("<?=lang("module_14_telephone_missing")?>!");
			else if(document.mainform.email.value=='')
				alert("<?=lang("module_14_email_missing")?>!");
			else if(document.mainform.email.value!=document.mainform.email2.value)
				alert("<?=lang("module_14_emails_not_equal")?>!");
			else
				document.mainform.submit();
		}
</script>


<div id="module_login">

<table style="width:auto;">
<tr><td style="width:50%;" valign="top">

	<!-- IF <?=lang("customer_logged_in")?> -->
                <!-- ELSE <?=lang("customer_logged_in")?> -->

		<div class="box_header" style="margin-right:5px;"><?=lang("module_14_log_into_account")?></div>

		<div class=box style="width:auto;margin-right:5px;height:350px;">
		<form method="post" name="loginform" action="login_check.asp">
		<input type="hidden" name="redirect_url" value="<?=lang("login_redirect_url")?>">
		<input type="hidden" name="order" value="<?=lang("login_order")?>">
		<table width="100%">
		<tr><td><?=lang("module_14_email")?><td style="text-align:right;"><input type="text" class="input" name="email"></td></tr>
		<tr><td><?=lang("module_14_password")?></td><td style="text-align:right;"><input type="password" class="input" name="pass"></td></tr>
		<tr><td colspan="2"><?=lang("module_14_remember_me")?> <input type="checkbox" name="remember"></td></tr>
		<tr><td colspan="2"><span class="button makehand" onclick="javascript:document.loginform.submit();"><?=lang("module_14_login")?></span></td></tr>
		<tr><td colspan="2"><div class="headertext2"><?=lang("module_14_forgot_password")?>?</div><div><?=lang("module_14_leave_password_blank")?>.</div></td></tr>
		</table>
		</form>
		</div>
	<!-- ENDIF <?=lang("customer_logged_in")?> -->

	</td>
	<td class="pagesplit"></td>
	<td style="width:50%;" valign="top">

	<div class="box_header">
		<!-- IF <?=lang("customer_logged_in")?> -->
			<?=lang("module_14_enter_info")?>
		<!-- ELSE <?=lang("customer_logged_in")?> -->
			<?=lang("module_14_or_enter_info")?>
		<!-- ENDIF <?=lang("customer_logged_in")?> -->
	</div>

	<div class="box" style="height:350px;">

		<form method="post" name="mainform" action="customer_data.asp">
		<input type="hidden" name="mode" value="<?=lang("login_mode")?>">
		<input type="hidden" name="redirect_url" value="<?=lang("login_redirect_url")?>">
		<input type="hidden" name="order" value="<?=lang("login_order")?>">
		<table width="100%">
			<tr><td><?=lang("module_14_company")?></td><td style="text-align:right;"><input style="width:175px" class=input type="text" name="company" value="<?=lang("login_company")?>"></td></tr>
			<tr><td><?=lang("module_14_name")?></td><td style="text-align:right;"><input style="width:175px" class=input type="text" name="name" value="<?=lang("login_name")?>"></td></tr>
			<tr><td><?=lang("module_14_address")?></td><td style="text-align:right;"><input style="width:175px" class=input type="text" name="address" value="<?=lang("login_address")?>"></td></tr>
			<tr><td><?=lang("module_14_address")?></td><td style="text-align:right;"><input style="width:175px" class=input type="text" name="address2" value="<?=lang("login_address2")?>"></td></tr>
			<tr><td><?=lang("module_14_zip")?></td><td style="text-align:right;"><input style="width:175px" class=input type="text" name="zip" value="<?=lang("login_zip")?>"</td></tr>
			<tr><td><?=lang("module_14_city")?></td><td style="text-align:right;"><input style="width:175px" class=input type="text" name="city" value="<?=lang("login_city")?>"></td></tr>
			<tr><td><?=lang("module_14_country")?></td><td style="text-align:right;">
				<select name="id_country" class="input">
					<?=lang("login_country_options")?>
				</select>
			</td></tr>
			<tr><td><?=lang("module_14_language")?></td><td style="text-align:right;">
				<select name="id_language" class="input">
					<?=lang("login_language_options")?>
				</select>
			</td></tr>
			<tr><td><?=lang("module_14_telephone")?></td><td style="text-align:right;"><input style="width:175px" class=input type="text" name="telephone" value="<?=lang("login_telephone")?>"></td></tr>
			<tr><td><?=lang("module_14_email")?></td><td style="text-align:right;"><input style="width:175px" class=input type="text" name="email" value="<?=lang("login_email")?>"></td></tr>
			<tr><td><?=lang("module_14_email_confirm")?></td><td style="text-align:right;"><input style="width:175px" class=input type="text" name="email2" value="<?=lang("login_email")?>"></td></tr>
			<tr><td><?=lang("module_14_website")?></td><td style="text-align:right;"><input style="width:175px" class=input type="text" name="website_url" value="<?=lang("login_website_url")?>"></td></tr>
			<tr><td colspan="2">

				<span class="button makehand" onclick="document.mainform.submit();">
					<!-- IF <?=lang("customer_logged_in")?> -->
						<?=lang("module_14_continue")?>
					<!-- ELSE <?=lang("customer_logged_in")?> -->
						<?=lang("module_14_create_account")?>
					<!-- ENDIF <?=lang("customer_logged_in")?> -->
				</span>

			</td></tr>
		</table>
		</form>
	</div>

</td></tr>
</table>

</div>