<?
// These form fields are included in the offer view
//echo("$option_type<br>");

if ($option_type_id == 1) {
    echo("<tr><td class='headertext2'>" . $option_name . "</td><td><select id='$option_join_id' class='input'>" . $option_childs_select . "</select></td></tr>");
}
elseif ($option_type_id == 2) {
    echo("<tr><td class='headertext2'>$option_name</td><td><input id='$option_join_id' class='input' type='text'></td></tr>");
}
elseif ($option_type_id == 3) {
    echo("<tr><td class='headertext2'>$option_name</td><td><input id='$option_join_id' type='checkbox'></td></tr>");
}

elseif ($option_type_id == 4) {
    echo("<tr><td colspan='2' class='headertext1'>$option_name</td></tr>");
}
elseif ($option_type_id == 5) {
    echo("<option value='$option_join_id'>$option_name</option>");
}
?>