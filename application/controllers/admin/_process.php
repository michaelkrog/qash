<?php

/*
|--------------------------------------------------------------------------
| index
|--------------------------------------------------------------------------
|
| Processes form posts etc.
| pageload:ok is for ajax to know loading successed
|
|--------------------------------------------------------------------------
| delete
|--------------------------------------------------------------------------
|
| Deletes rows from a table, if it's not able to mark "deleted = 1" in table
|
*/

class Process extends Controller {

	function _output($output)
	{
	    echo($output . 'pageload:ok');
	}

	
	function index()
	{
	    // Get default target info
	    $target = $this->encrypt->decode($this->input->post("target"));
	    $target_id_names = $this->encrypt->decode($this->input->post("target_id_names"));
	    $target_id_values = $this->encrypt->decode($this->input->post("target_id_values"));

	    $target_array = explode("_",$target);
	    $target_mode = $target_array[0];
	    $target = mb_ereg_replace($target_mode."_","",$target);

	    // Create sql target snippet
	    $target_id_names_array = explode("|",$target_id_names);
	    $target_id_values_array = explode("|",$target_id_values);

	    $i = 0;
	    $sql_target_id = "";
	    foreach($target_id_names_array AS $name) {

		    if ($name != "") {
			    $name_cleaned = mb_substr($name, 4, mb_strlen($name)); // Remove str/int from name

			    if(mb_substr($name, 0, 3) == "str")
				    $sql_target_id .= db_str_fix($name_cleaned) . " = '" . db_str_fix($target_id_values_array[$i]) . "' AND ";
			    else
				    $sql_target_id .= db_str_fix($name_cleaned) . " = " . db_int_fix($target_id_values_array[$i]) . " AND ";
		    }
		    $i++;
	    }


	    // Create sql update snippet from all posted variables
	    $sql_update = "";
	    $sql_insert_part1 = "";
	    $sql_insert_part2 = "";

	    foreach($_POST as $name => $value) {

		    // Make sure queries are targeted at this website. Correct it for security reasons.
		    if($name == "int_id_website") {
			    $value = $this->Website->config_website['id_website'];
		    }

		    // $name = convert_secret($name,$secret_key)
		    if($name != "mode" && $name != "target" && $name != "target_id_names" && $name != "target_id_values") {

			    // Is value a string/integer?
			    $name_cleaned = mb_substr($name, 4, mb_strlen($name)); // Remove str/int from name
			    if(mb_substr($name, 0, 3) == "str") {
				    $sql_value= "'" . db_str_fix($value) . "'";
			    }
			    else {
				    $sql_value = db_int_fix($value);
			    }

			    // Build sql and replace spaces in name to avoid injection
			    $sql_update .= ", " . db_str_fix(str_replace(' ','_',$name_cleaned)) . " = " .$sql_value;
			    $sql_insert_part1 .= ", " . db_str_fix($name_cleaned);
			    $sql_insert_part2 .= ", " . $sql_value;


		    }
	    }

	    $sql_update = mb_substr($sql_update, 2, mb_strlen($sql_update));
	    $sql_insert = "(" . mb_substr($sql_insert_part1,2,mb_strlen($sql_insert_part1)) . ") VALUES (" . mb_substr($sql_insert_part2,2,mb_strlen($sql_insert_part2)) . ")";

	    // Save data
	    switch($target_mode) {
		    case "Update" :
			echo("UPDATE " . db_str_fix($target) . " SET " . $sql_update . " WHERE " . $sql_target_id . " id_website = " . $this->Website->config_website['id_website']);
			mysql_query("UPDATE " . db_str_fix($target) . " SET " . $sql_update . " WHERE " . $sql_target_id . " id_website = " . $this->Website->config_website['id_website']) or die(mysql_error());
			break;
		    case "DeleteInsert" :
			mysql_query("DELETE FROM " . db_str_fix($target) . " WHERE " . $sql_target_id . " id_website = " . $this->Website->config_website['id_website']) or die(mysql_error());
			echo("DELETE FROM " . db_str_fix($target) . " WHERE " . $sql_target_id . " id_website = " . $this->Website->config_website['id_website']);
		    case "Insert" :
			echo("INSERT INTO " . db_str_fix($target) . " " . $sql_insert);
			mysql_query("INSERT INTO " . db_str_fix($target) . " " . $sql_insert) or die(mysql_error());

			//Check if "sort" column exixts, and insert a new max value
			$sort = $this->db->query("Show columns from " . db_str_fix($target) . " like 'sort'")->result_array();
			if ($sort) {
			    $sort = $this->db->query("SELECT MAX(sort)+1 AS sort FROM " . db_str_fix($target) . " WHERE id_website = " . $this->Website->config_website['id_website'])->row();
			    mysql_query("UPDATE " . db_str_fix($target) . " SET sort = " . $sort->sort . " WHERE id = LAST_INSERT_ID()") or die(mysql_error());
			}
			break;
	    }

	    //echo("target_mode: " . $target_mode);
	    //die();
	}



	function delete($target, $target_id) {

	    $target_id_value = str_replace(" ","",$this->input->post('target_id_value'));
	    $target = str_replace(" ","",$target);
	    $target_id = str_replace(" ","",$target_id);

	    if (mysql_query("UPDATE " . db_str_fix($target) . " SET deleted = 1 WHERE " . db_str_fix($target_id) . " IN(" . db_str_fix($target_id_value) . ") AND id_website = " . $this->Website->config_website['id_website']) == false) {
		mysql_query("DELETE FROM " . db_str_fix($target) . " WHERE " . db_int_fix($target_id) . " IN(" . db_str_fix($target_id_value) . ") AND id_website = " . $this->Website->config_website['id_website']) or die(mysql_error());
	    }

	}


	function delete_option() {

	    $target_id_value = str_replace(" ","",$this->input->post('target_id_value'));

	    mysql_query("DELETE FROM module_offer_option_join WHERE option_id IN(" . db_str_fix($target_id_value) . ") AND id_website = " . $this->Website->config_website['id_website']) or die(mysql_error());
	    mysql_query("DELETE FROM module_offer_option WHERE id IN(" . db_str_fix($target_id_value) . ") AND id_website = " . $this->Website->config_website['id_website']) or die(mysql_error());
	}


	function change_sort($target,$direction,$id) {
	    
	    // Secure values for sql string
	    $target = str_replace(" ","",$target);

	    // Get sort value before change
	    $sort_old = $this->db->query("SELECT id, sort FROM " . db_str_fix($target) . " WHERE id = " . db_int_fix($id) . " AND id_website = " . $this->Website->config_website['id_website'])->row();

	    // Skip sort change, if sort value is 1

	    // Build sql for sort direction change
	    if ($direction == "increase") {
		$sort_new = $this->db->query("SELECT id, sort FROM " . db_str_fix($target) . " WHERE sort > " . $sort_old->sort . " AND id_website = " . $this->Website->config_website['id_website'] . " ORDER BY id LIMIT 1")->row();
	    }
	    else {
		$sort_new = $this->db->query("SELECT id, sort FROM " . db_str_fix($target) . " WHERE sort < " . $sort_old->sort . " AND id_website = " . $this->Website->config_website['id_website'] . " ORDER BY id DESC LIMIT 1")->row();
	    }

	     if ($sort_new) {

		// Change sort values
		mysql_query("START TRANSACTION");
		mysql_query("UPDATE " . db_str_fix($target) . " SET sort = " . $sort_new->sort . " WHERE id = " . $sort_old->id . " AND id_website = " . $this->Website->config_website['id_website']) or die(mysql_error());
		mysql_query("UPDATE " . db_str_fix($target) . " SET sort = " . $sort_old->sort . " WHERE id = " . $sort_new->id . " AND id_website = " . $this->Website->config_website['id_website']) or die(mysql_error());
		mysql_query("COMMIT");
	     }

	}
}

?>