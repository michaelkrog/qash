<?php
class Model_offer extends Model {

    function Model_offer() {
        // Call the Model constructor
        parent::Model();
    }

    function get_fields() {
	 // Get options
        $query = $this->db->query('SELECT * FROM module_offer_option WHERE module_offer_option.id_website = ' . $this->Website->config_website['id_website']);
	return $query;
    }

    function get_options_primary() {
	 // Get options, but not those in select boxes
        $query = $this->db->query('SELECT *, module_offer_option.id AS option_id, module_offer_option_join.id AS option_join_id FROM module_offer_option_join INNER JOIN module_offer_option ON module_offer_option.id = module_offer_option_join.option_id WHERE module_offer_option_join.option_type_id NOT IN(5) AND module_offer_option_join.id_website = ' . $this->Website->config_website['id_website'] . ' ORDER BY  module_offer_option_join.sort');
	return $query;
    }

    function get_option($id = 0) {
	 // Get a single option
        $query = $this->db->query("SELECT * FROM module_offer_option WHERE id = " . db_int_fix($id). " AND id_website = " . $this->Website->config_website['id_website']);
	return $query;
    }

    function get_option_join($id = 0) {
	 // Get a single option_join
	$sql = "";
	if ($id > 0)
	    $sql = " AND id = " . db_int_fix($id);
	
        $query = $this->db->query("SELECT * FROM module_offer_option_join WHERE id_website = " . $this->Website->config_website['id_website'] . $sql . " ORDER BY sort");
	return $query;
    }

    function get_options_type() {
	 // Get a single option_join
        $query = $this->db->query("SELECT * FROM module_offer_option_type");
	return $query;
    }

    function get_options_all($id = 0) {
	 // Get all options
        $query = $this->db->query("SELECT module_offer_option.id AS option_id, module_offer_option_join.*, module_offer_option.description AS option_description, CONCAT(module_offer_option.name,'(',module_offer_option_join.name,')') AS option_name_full, module_offer_option.name AS option_name, module_offer_option_type.name AS option_type_name, module_offer_option.id AS option_id, (SELECT name FROM module_offer_option_join AS module_offer_option_join2 WHERE module_offer_option_join2.id = module_offer_option_join.option_parent_id) AS depends_name FROM module_offer_option_join INNER JOIN module_offer_option ON module_offer_option.id = module_offer_option_join.option_id INNER JOIN module_offer_option_type ON module_offer_option_join.option_type_id = module_offer_option_type.id WHERE module_offer_option_join.id_website = " . $this->Website->config_website['id_website'] . " ORDER BY sort");
	
	return $query;
    }

    function get_childs_select($id = 0) {
	 // Get options for a specific selectbox only
        $options = $this->db->query("SELECT *, module_offer_option.id AS option_id, module_offer_option.name as option_name FROM module_offer_option_join INNER JOIN module_offer_option ON module_offer_option.id = module_offer_option_join.option_id WHERE module_offer_option_join.option_parent_id = " . db_int_fix($id) . " AND module_offer_option_join.id_website = " . $this->Website->config_website['id_website'] . " ORDER BY sort")->result_array();
	
	$options_string = "";
	foreach($options as $row) {
	    $options_string .= $this->load->view('module/offer_option', $row, true);
	}

	return $options_string;
    }
}
?>
