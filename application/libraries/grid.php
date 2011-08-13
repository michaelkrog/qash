<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Grid {

    private $object = "";
    private $id = ""; // Grid identification, for allowing multiple grids
    private $header = null;
    private $field = null;
    private $action = null;
    //private $link = null;
    private $data = null;


    public function set_object($value) {
	$this->object = $value;
    }

    public function set_id($value) {
	$this->id = $value;
    }

    public function set_header($value) {
	$this->header[] = $value;
    }

    public function set_field($value) {
	$this->field[] = $value;
    }

    public function set_action($value) {
	$this->action[] = $value;
    }

    //public function set_link($value) {
    //	$this->link[] = $value;
    //}

    public function set_data($data) {
	$this->data = $data;
    }


    //* Custom field output funtions *//
    private function yesno($value) {
	if ($value == 1) {
	    $value = lang("yes");
	}
	else {
	    $value = lang("no");
	}

	return $value;  
    }



    public function build()
    {

	// Create actions box
	$output = "<div style='position:relative;'><div id='actions'><ul><li onclick=\"actions('" . $this->id . "','check');\">Alle</li><li onclick=\"actions('" . $this->id . "','uncheck');\">Ingen</li><br />\n";
	for ($i=0;$i < count($this->action); $i++) {
	    $output .= "<li><a href=\"" . $this->action[$i][1] . "\">" . $this->action[$i][0] . "</a></li>\n";
	}
	$output .= "</ul></div></div>";


	// Create outer grid
	$output .= "<table class='grid'>\n";


	// Create header
	if($this->header) {

		$output .= "<tr>\n";

		for ($i=0;$i < count($this->header); $i++) {
		    $output .= "<td class='header'>" . $this->header[$i] . "</td>\n";
		}

		$output .= "<td class='header actions' style='text-align:right;width:1px;'>\n";

		// Insert action image
		if ($this->action) {
		    $output .= "<div><img onclick='show_actions();' src='/application/assets/graphics/admin/icon_action.png'></div>";
		}
		$output .= "</td></tr>\n";
	}


	// Create inner grid
	$output .= "<tbody class='tbody box4' id='" . $this->id . "'>\n";


	// Insert rows
	$cnt_highlight = 1;
	foreach($this->data as $object) {

		$cnt_cell = 0;
		$row_id = 0;
		//$output_links_row = $output_links;

		$class_highlight = "";
		if ($cnt_highlight == 2) {
			$class_highlight = " highlight";
			$cnt_highlight = 0;
		}

		$row_id =  $object->get_id();
		$output .= "<tr class=' row" . $class_highlight . "'>";


		foreach($this->field as $field) {

		    $getter = "get_".$field[0];
		    $value = $object->$getter();

		    // Customize field output using one of the functions above
		    if (count($field) == 2) {
			$value = $this->$field[1]($value);
		    }
		    
		    $output .= "<td class='object_" . $this->object . "_" . $row_id . "_" . $field[0] . "'>" . $value. "</td>";

		    $cnt_cell++;
		}

		$output = mb_ereg_replace("{id}",$object->get_id(),$output);

		$cnt_cell++;


		// Insert sort function
		$sort = "";
		//if (in_array("sort",$functions))
		//    $sort = "<div style='float:left;margin-left:30px;'><a href=\"javascript:ajax('" . site_url("admin/process/change_sort/module_offer_option_join/decrease/" . $row_id . "") . "','','','ActionReload');\"><img src='/assets/graphics/admin/arrow_up.png'></a><br /><br /><a href=\"javascript:ajax('" . site_url("admin/process/change_sort/module_offer_option_join/increase/" . $row_id . "") . "','','','ActionReload');\"><img src='/assets/graphics/admin/arrow_down.png'></a></div>";

		// Insert checkbox
		$checkbox = "";
		if ($this->action)
		    $checkbox = "<input type='checkbox' name='checkbox_grid_" . $this->id . "' value='" . $row_id . "'>\n";

		$output .= "<td class='last_col' style='text-align:right;'>$sort$checkbox</td>\n";


		// End row
		$output .= "</tr>\n";
		$cnt_highlight++;

	}

	# If no data exists
	if (!$this->data) {
	    $output .= "<tr><td colspan='" . $i . "'>" . lang("admin_no_data") . "</td></tr>\n";
	}

	// Finish grid creation
	$output .= "</tbody>\n</table>\n";

	
	return $output;
	
    }


}

/* End of file Grid.php */