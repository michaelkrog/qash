<?PHP
# Build header
$output = "";

// Create javascript object for posting and evaluate values
$postdata = "var postdata = {};\n";

// Create header
if (!$header == "")
	$output .= "<div class='boxheader'>" . $header . "</div>";

// Create form
$output .= "<form enctype='multipart/form-data' id='" . $id . "' accept-charset='" . $this->config->item('charset') . "'>";

// Create table
$output .= "<table class='table form box'>";

// Create "target" fields, if default posting method is used
if ($mode == "admin/process") {
	$postdata .= "postdata.target = '" . $target . "';\n";
	$postdata .= "postdata.target_id_names = '" . $target_id_names . "';\n";
	$postdata .= "postdata.target_id_values = '" . $target_id_values . "';\n";
}

// Get max field length from db
//$col_info = mysql_query("SELECT * FROM " . $this->encrypt->decode($target) . )
//$this->encrypt->encode("DeleteInsert_module_offer_option_join");

// Create fields (select,input,password,checkbox,hidden,textarea etc.)
$i = 0;
foreach($setup_array as $setup_field) {

	// Fill javascript object
	if (!$setup_array[$i][6] == "")
	    $postdata .= "postdata." . $setup_array[$i][6] . " = jQuery('#" . $setup_array[$i][6] . "').val();\n";

	// Create field
	$extra_text = "";
	$field_value = "";
	$field_type = $setup_array[$i][2];
	
	// Check if we should reload with a new id, when data is changed (Select box only)
	$reload = "";
	if (is_array($setup_array[$i][2])) {
	    $field_type = $setup_array[$i][2][0];
	    $reload = "onchange=\"ajax('" . $setup_array[$i][2][1] . "/' + this.value,'','box_popup','');\"";
	}

	// Set up field
	switch ($field_type) {
	case "select":
			// Requires an array of data containg an [0]:id and [1]:title
			$field = "<select class='input_large' id='" . $setup_array[$i][6] . "' " . $reload . ">";

			$ii = 0;
			$option_value_array = "";
			foreach($setup_array[$i][7] as $options=>$option) {

				$selected = "";
				if ($setup_array[$i][5] == $option['id'])
					$selected = "selected";

				$field = $field . "<option value='" . $option['id'] . "' " . $selected . ">" . $option['name'] . "</option>";
				$ii++;
			}

			$field = $field . "</select>";

			$text = $setup_array[$i][0];
			break;

		case "input":
		case "date":
			$field = "<input class='input_large' type='text' id='" . $setup_array[$i][6] . "' value='" . $setup_array[$i][5] . "' maxlength='" . $setup_array[$i][3] . "'>";
			$text = $setup_array[$i][0];
			break;

		case "input_disabled":
			$field = "<input class='input_large' type='text' id='" . $setup_array[$i][6] . "' value='" . $setup_array[$i][5] . "' maxlength='" . $setup_array[$i][3] . "' disabled>";
			$text = $setup_array[$i][0];
			break;

		case "password":
			$field = "<input class='input_large' type='password' id='" . $setup_array[$i][6] . "' value='" . $setup_array[$i][5] . "' maxlength='" . $setup_array[$i][3] . "'>";
			$text = $setup_array[$i][0];
			break;

		case "checkbox":
			$selected = "";
			if ($setup_array[$i][5] == 0)
				$selected = "selected";

			$field = "<select class='input_large' id='" . $setup_array[$i][6] . "'><option value='1'>Ja</option><option value='0' " . $selected . ">Nej</option></select>";
			$text = $setup_array[$i][0];
			break;

		case "hidden":
			$field = "<input type='hidden' id='" . $setup_array[$i][6] . "' value='" . $setup_array[$i][5] . "' maxlength='" . $setup_array[$i][3] . "'>";
			break;

		case "textarea":
			$field = "<textarea class='input_large' rows='5' id='" . $setup_array[$i][6] . "' maxlength='" . $setup_array[$i][3] . "'>" . $setup_array[$i][5] . "</textarea>";
			$text = $setup_array[$i][0];
			break;

		case "wysiwyg":
			//$field = "<textarea rows='5' id='" . $setup_array[$i][6] . "' maxlength='" . $setup_array[$i][3] . "'>" . $setup_array[$i][5] . "</textarea>";
			//$text = $setup_array[$i][0];
			break;

		case "file":
			if ($setup_array[$i][5]) {
				$text = "<img src='/upload/" . $setup_array[$i][5] . "?x=" . md5_create() . "' class='picture_thumb'>";
			}
			else {
				$text = $setup_array[$i][0];
			}

			$field = "<input class='input_large' type='file' id='" . $setup_array[$i][6] . "' maxlength='" . $setup_array[$i][3] . "'>";
			break;
	}

	if($setup_array[$i][2] != "hidden")
		$output .= "<tr><td class='title'>" . $text . "</td><td class='field'>" . $field . "</td></tr>";
	else
		$output .= $field;

	// Create description
	if ($setup_array[$i][1] != "")
		$output .= "<tr><td></td><td><div style='margin-top:3px;' class='description'><span class='icon'><img src='/assets/graphics/admin/arrow_up.png'></span>" . $setup_array[$i][1] . "</div></td></tr>";

	$i++;

	$field = "";
	$text = "";
}

// Create submit button
$output .= "<tr><td></td><td class='submitbutton'><span class='button_large' id='button_form'>Gem</span></td></tr>";

// Finish table creation
$output .= "</table>";

// Finish form creation
$output .= "</form>";

// Finish javascript by adding click handler
$postdata = "jQuery('#button_form').click(function() {" . $postdata . "\n ajax('" . site_url($mode) . "',postdata,'','" . $actions . "');});\n";
//$postdata = "jQuery('#button_form').click(function() {" . $postdata . " \n alert('test');});\n";

// Add javascript to the output
$output .= "<script language='javascript'>" . $postdata . "</script>";
//echo htmlentities($postdata);
//echo(htmlentities($output));

// Write form
echo $output;
?>