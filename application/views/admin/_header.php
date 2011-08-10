<?php
# Build header
$output = "";

//$output .= "<a class='button_header' href=\"javascript:show_messagebox(false,'box_popup','');\"><em></em><span>Luk</span><b></b></a>";
$image_corner = ""; //<img src='/assets/graphics/admin/heading_main_right.gif'>";
if(in_array("close",$functions)) {
    $image_corner = "<a class='button_header' href=\"javascript:show_messagebox(false,'box_popup','');\"><em></em><span>Luk</span><b></b></a>";
}

$output .= "<div class='pageheader_holder'>";
$output .= "<div id='" . $id . "' class='pageheader'>" . $header . "</div>";
$output .= "<div class='button_header_holder'>";

for ($i = 0; $i < count($buttons); $i++) {
    $output .= "<a class='button_header' onclick=\"" . $buttons[$i][1] . "\"><em></em><span>" . $buttons[$i][0] . "</span><b></b></a>";
}

$output .= $image_corner;

$output .= "</div>";
$output .= "</div>";

//echo(htmlentities($output));
echo $output;

?>
