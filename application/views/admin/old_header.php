<?php
# Build header
$output = "";

$image_corner = "<img src='/assets/graphics/admin/heading_main_right.gif'>";
if($use_close == true) {
    $image_corner = "<a href=\"javascript:show_messagebox(false,'box_popup','');\">" . $image_corner . "</a>";
}

$output .= "<div id='" . $id . "' class='boxheader_main'><div class='boxheader_main_left'><img src='/assets/graphics/admin/heading_main_left.gif'></div>";
$output .= "<div class='boxheader_main_text'>" . $header . "</div>";

//$output .= "<div class='boxheader_main_text'>" . $header . "</div>";

$output .= "<div class='boxheader_main_right'>" . $image_corner . "</div>";

$output .= "</div>";

$output .= "<div class='button_header_holder'>";

//if($buttons) {
	for ($i = 0; $i < count($buttons); $i++) {
	    $output .= "<a class='button_header' onclick=\"" . $buttons[$i][1] . "\"><em></em><span>" . $buttons[$i][0] . "</span><b></b></a>";
	}
//}

$output .= "</div>";

//echo(htmlentities($output));
echo $output;

?>
