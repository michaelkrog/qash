<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Form extends CI_model {


    private $id = null; // Object id
    private $header = null;
    private $field = null;
    private $object = null;
    private $object_properties = null;
    private $data = null;
    private $action = null;


    public function set_id($value) {
	$this->id = $value;
    }

    public function set_header($value) {
	$this->header = $value;
    }

    public function set_field($value) {
	$this->field[] = $value;
    }

    public function set_object($value) {
	$this->object = $value;
    }

    public function set_object_properties($value) {
	$this->object_properties[] = $value;
    }

    public function set_data($data) {
	$this->data = $data;
    }

    public function set_action($value) {
	$this->action = $value;
    }

    public function build()
    {
	if (!$this->data)
		die("No data...");

	# Build header
	$output = "<div class='header header_current'>$this->header</div>";
	$output .= "<div class='content'>";

	// Create javascript object for posting and evaluate values
	//$postdata = "var postdata = {};\n";

	// Create header
	//if (!$this->header == "")
	//	$output .= "<div class='boxheader'>" . $this->header . "</div>\n";

	// Create form, set default action if empty
	if ($this->action == null) {
	    $this->set_action("/index.php/admin/object/save");
	}
	//if (!$this->id == null)
	    $form_id = "form";
	//else
	//    $form_id = $this->id;

	$output .= "<form enctype='multipart/form-data' id='" . $form_id . "' accept-charset='utf-8'>\n";

	// Create hidden field containing object target info
	$output .= "<input type='hidden' name='object' value='$this->object'>\n";
	$output .= "<input type='hidden' name='id' value='$this->id'>\n";
	
	// Create table
	$output .= "<table class='table form box'>\n";

	// Create fields (select,input,password,checkbox,hidden,textarea etc.)
	$i = 0;
	foreach($this->field as $fieldinfo) {

		// Create field
		$text = "";
		$description = "";
		$field = "";
		$field_value = "";
		$field_type = $fieldinfo[3];
		$getter = "get_" . $fieldinfo[2];

		// Check if we should reload with a new id, when data is changed (Select box only)
		$reload = "";
		//if ($field[1])) {
		    //$field_type = $field[$i][2][0];
		//    $reload = "onchange=\"ajax('" . $field[$i][2][1] . "/' + this.value,'','box_popup','');\"";
		//}

		// Set up field
		switch ($field_type) {
		case "select":
			// Requires an object of data for selection (id and name are used)
			$field = "<select class='input_large' name='" . $fieldinfo[2] . "_association' " . $reload . ">";

			$ii = 0;
			$option_value_array = "";

			foreach($fieldinfo[4] as $object) {

				$selected = "";
				if ($this->data->$getter()->get_id() == $object->get_id()) {
				    $selected = "selected";
				}
				$field = $field . "<option value='" . $object->get_id() . "' " . $selected . ">" . $object->get_name() . "</option>";
				$ii++;
			}

			$field = $field . "</select>";
			break;

		case "text":
		case "integer":
		case "email":
			$field = "<input type='text' name='" . $fieldinfo[2] . "' value='" . $this->data->$getter() . "' maxlength='100'>";
			$text = $fieldinfo[0];
			break;

		case "date":
			$field = "<input class='input_large' type='text' name='" . $fieldinfo[2] . "' value='" . $this->data->$fieldinfo[2] . "' maxlength='" . $setup_array[$i][3] . "'>";
			$text = $fieldinfo[0];
			break;

		case "input_disabled":
			$field = "<input class='input_large' type='text' name='" . $fieldinfo[2] . "' value='" . $this->data->$fieldinfo[2] . "' maxlength='" . $setup_array[$i][3] . "' disabled>";
			$text = $fieldinfo[0];
			break;

		case "password":
			$field = "<input class='input_large' type='password' name='" . $fieldinfo[2] . "' value='" . $this->data->$fieldinfo[2] . "' maxlength='" . $setup_array[$i][3] . "'>";
			$text = $fieldinfo[0];
			break;

		case "checkbox":
			$selected = "";
			if ($this->data->$getter() == 0)
				$selected = "selected";

			$field = "<select name='" . $fieldinfo[2] . "'><option value='1'>" . lang("yes") . "</option><option value='0' " . $selected . ">" . lang("no") . "</option></select>";
			$text = $fieldinfo[0];
			break;

		case "hidden":
			$field = "<input type='hidden' name='" . $setup_array[$i][6] . "' value='" . $setup_array[$i][5] . "' maxlength='" . $setup_array[$i][3] . "'>";
			break;

		case "textarea":
			$field = "<textarea class='input_large' rows='5' name='" . $setup_array[$i][6] . "' maxlength='" . $setup_array[$i][3] . "'>" . $setup_array[$i][5] . "</textarea>";
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

			$field = "<input class='input_large' type='file' name='" . $setup_array[$i][6] . "' maxlength='" . $setup_array[$i][3] . "'>";
			break;
		}

		
		
		if($fieldinfo[2] != "hidden")
			$output .= "<tr><td class='title'>" . $text . "</td><td class='field'>" . $field . "</td></tr>";
		else
			$output .= $field;

		// Create description
		if ($fieldinfo[1] != "")
			$output .= "<tr><td></td><td><div style='margin-top:3px;' class='description'><span class='icon'><img src='/application/assets/graphics/admin/arrow_up.png'></span>" . $fieldinfo[1] . "</div></td></tr>";

		$i++;
	}

	// Create submit button
	$output .= "<tr><td></td><td class='submitbutton'><span style='border: 1px solid #aaaaaa;' class='header' id='button_form' onclick=\"ajax('$this->action', $('#form').serialize(),'','alert,hide_dialog,update');\">Gem</span></td></tr>";

	// Finish table creation
	$output .= "</table>";

	// Finish form creation
	$output .= "</form>";

	return $output;
    }

}