<?PHP
// Create grid
$output = "";

// Create header
if (!$header == "")
	$output .= "<div class='grid_header'>" . $header . "</div>";


$output .= "<table class='table'>";

// Create subheader
if($subheader) {
	
	$output .= "<tr>";

	for ($i=0;$i < count($subheader); $i++) {
	    $output .= "<td class='table_subheader'>" . $subheader[$i] . "</td>";
	}

	$output .= "<td class='table_subheader' style='text-align:right;width:1px;'>";

	// Create dropdown actions
	if ($actions) {
	    $output .= "<select class='input_small' id='action_grid_" . $id . "' onchange=\"actions('" . $id . "',this.value);\"><option value=''>Handling</option><option value='check'>Alle</option><option value='uncheck'>Ingen</option>";

	    for ($i=0;$i < count($actions); $i++) {
		$output .= "<option value='" . $actions[$i][1] . "'>" . $actions[$i][0] . "</option>";
	    }
	    
	   $output .= "</select>";
	}
	$output .= "</td></tr>";
}
//$test = count($subheader);
//$test ++;

// Create grid
//$output .= "<div class='box_wrapper'><table id='" . $id . "' class='box'>";
$output .= "<tbody class='tbody box4' id='" . $id . "'>";
//$output .= "<tr class='box_wrapper'><td colspan='" . $test . "'>tester</td></tr>";


// Prepare links
$output_links = "";
for ($i=0;$i < count($links); $i++) {
    $output_links .= " - <a href=\"" . $links[$i][1] . "\">" . $links[$i][0] . "</a>";
}

// Insert rows
$cnt_highlight = 1;
foreach($data_array as $row) {

	$cnt_cell = 0;
	$row_id = 0;
	$output_links_row = $output_links;

	$class_highligt = "";
	if ($cnt_highlight == 2) {
		$class_highligt = " highlight";
		$cnt_highlight = 0;
	}

	//foreach($row as $column=>$cell) {
	foreach($cells as $column) {
		if (strrpos($column,"yesno") !== false) {
		    $column = str_replace("yesno_","",$column);
		    $cell = yesno($row[$column]);
		}
		else {
		    $cell = $row[$column];
		}

		if ($cnt_cell == 0) {
			$row_id = $row['id'];
			$output .= "<tr class='" . $column . " " . $row_id . " row" . $class_highligt . "'>";
		}

		//if (in_array($column, $cells)) {
		    $cell_temp = $cell;

		    

			$output .= "<td id='" . $column . "'>" . $cell_temp . "</td>";
			

		//}

		// Replace special tags in links
		foreach($row as $rowcolumn=>$rowcell) {
		    $output_links_row = mb_ereg_replace("{" . $rowcolumn . "}",$rowcell,$output_links_row);
		}
		
		$cnt_cell++;
	}
	$cnt_cell++;
	
	// Finish links string by removing first instance of "-"
	$output_links_row = substr($output_links_row,3,mb_strlen($output_links_row));

	// Insert sort function
	$sort = "";
	if (in_array("sort",$functions))
	    $sort = "<div style='float:left;margin-left:30px;'><a href=\"javascript:ajax('" . site_url("admin/process/change_sort/module_offer_option_join/decrease/" . $row_id . "") . "','','','ActionReload');\"><img src='/assets/graphics/admin/arrow_up.png'></a><br /><br /><a href=\"javascript:ajax('" . site_url("admin/process/change_sort/module_offer_option_join/increase/" . $row_id . "") . "','','','ActionReload');\"><img src='/assets/graphics/admin/arrow_down.png'></a></div>";

	// Insert checkbox
	$checkbox = "";
	if ($actions)
	    $checkbox = "<input type='checkbox' name='checkbox_grid_" . $id . "' value='" . $row_id . "'>";

	$output .= "<td style='text-align:right;'>$sort$checkbox</td>";


	// End row
	$output .= "</tr>";

	// Insert links block if present
	if ($links)
	    $output .= "<tr class='" . $row_id . " row" . $class_highligt . "'><td class='links' colspan='" . $cnt_cell . "'>" . $output_links_row . "</td></tr>";

	$cnt_highlight++;

}


# If no data exists
if (!$data_array)
	$output .= "<tr><td colspan='" . $i . "'>Ingen data</td></tr>";


// Finish grid creation
$output .= "</tbody></table>";
//$output .= "</div>";
//echo(htmlentities($output));

// Return grid
echo($output);
?>