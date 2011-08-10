<?php
class Data_layer extends CI_Model {

    function Data_layer() {
        // Call the Model constructor
        parent::__construct();

	// Load domain classes
	//$this->load->model('domains/Language_domain');
	//$this->load->model('domains/User_domain');
    }


    function get_website($id) {
	if ($id == 0)
	    $sql = "(SELECT id_website FROM website_domain WHERE website_domain = '" . str_replace("www.","",$_SERVER["HTTP_HOST"]) . "' LIMIT 1) = website.id_website";
	else
    	    $sql = "id_website = " . $id;

        $query = $this->db->query("SELECT * FROM website WHERE " . $sql . " limit 1")->row();

	$obj = false;
	if ($query) {
	    $obj = $this->load->object('Website_domain');
	    $obj->set_id($query->id_website);
	    $obj->set_md5($query->id_website_crypt);
	    $obj->set_name($query->website_name);
	    $obj->set_template($query->id_template);
	}

	return $obj;
    }


    function get_template($id) {
        $query = $this->db->query("SELECT * FROM template_module WHERE id_module = 13 AND id_template = $id")->row();

	$obj = false;
	if ($query) {
	    $obj = $this->load->object('Template_domain');
	    $obj->set_id($query->id_template);
	    $obj->set_website($query->id_website);

	    // Get styles from old template format, and wipe out html
	    if (preg_match("@<style[^>]*?>.*?</style>@siu", $query->html, $matches))
		$obj->set_body(strtolower(preg_replace("@<[^>]*?>@siu", "", $matches[0])));
	    else
		$obj->set_body(strtolower($query->html));
	}

	return $obj;
    }


   function get_module($id) {
        $query = $this->db->query("SELECT template_module.*, module.module_url FROM template_module INNER JOIN module ON template_module.id_module = module.id_module WHERE id_template_module = $id")->row();

	$obj = false;
	if ($query) {
	    $obj = $this->load->object('Module_domain');
	    $obj->set_id($query->id_template_module);
	    $obj->set_template($query->id_template);
	    $obj->set_body($query->html);
	    $obj->set_section($query->section_id);
	    $obj->set_sort($query->sort);
	    $obj->set_url($query->module_url);
	}

	return $obj;
    }


    function get_module_list($template) {
        $query = $this->db->query("SELECT template_module.id_template_module FROM template_module INNER JOIN module ON template_module.id_module = module.id_module WHERE section_id IS NOT NULL AND active = 1 AND id_module_type <> 13 AND id_template = $template ORDER BY sort")->result_array();

	$obj = false;
	$obj_array = false;
	if ($query) {
	    for($i=0; $i<=count($query)-1; $i++) {
		$obj_array[$i] = $this->get_module($query[$i]["id_template_module"]);
	    }
	}

	return $obj_array;
    }


    function get_page($website,$id) {
	$sql = "";
	
	if ($website != 0)
	    $sql .= "AND id_website = $website ";

	if ($id == "")
	    $sql .= "AND id_page_crypt = 'main' ";
	else
	    $sql .= "AND id_page_crypt = '$id' ";

	// Remove first AND
	$sql = substr($sql,3,strlen($sql));
	
        $query = $this->db->query("SELECT * FROM sites WHERE $sql limit 1")->row();
	//die("SELECT * FROM sites WHERE $sql limit 1");

	$obj = false;
	if ($query) {
	    // Remove old {{xxx}} tags from body
	    $body = preg_replace("/{(.*?)}}/", "", $query->body);

	    $obj = $this->load->object('Page_domain');
	    $obj->set_id($query->SiteID);
	    $obj->set_website($query->id_website);
	    $obj->set_title($query->title);
	    $obj->set_description($query->description);
	    $obj->set_keywords($query->keywords);
	    $obj->set_body($body);
	}

	return $obj;
    }


    function get_language($id) {
        $query = $this->db->query('SELECT * FROM language WHERE id_language = 1 limit 1')->row();

	$obj = false;
	if ($query) {
	    $obj = $this->load->object('Language_domain');
	    $obj->set_id($query->id_language);
	    $obj->set_name($query->language);
	}
	
	return $obj;
    }


    function get_user($id) {
        $query = $this->db->query('SELECT * FROM user WHERE id_user = ' . $id . ' limit 1')->row();

	$obj = false;
	if ($query) {
	    $obj = $this->load->object('User_domain');
	    $obj->set_id($query->id_user);
	    $obj->set_company($query->company);
	    //$obj->set_company_reg($query->company_reg);
	    $obj->set_name($query->name);
	    $obj->set_address($query->address);
	    $obj->set_address2($query->address2);
	    $obj->set_zip($query->zip);
	    $obj->set_city($query->city);
	    $obj->set_country($query->id_country);
	    $obj->set_email($query->email);
	    $obj->set_telephone($query->telephone);
	    $obj->set_website($query->website_url);
	    $obj->set_newsletter($query->newsletter);
	}
	
	return $obj;
    }


     function get_user_list($website_id) {
        $query = $this->db->query("SELECT id_user FROM user WHERE id_website = " . $website_id . " ORDER BY id_user DESC")->result_array();

	$obj_array = false;
	if ($query) {
	    for($i=0; $i<=count($query)-1; $i++) {
		$obj_array[$i] = $this->get_user($query[$i]["id_user"]);
	    }
	}

	return $obj_array;
    }


    function persist_user($user) {
	echo(json_encode($user));
	return $user;
    }


    function get_payment_method($id) {
	$query = $this->db->query('SELECT * FROM payment_module INNER JOIN website_payment ON payment_module.id_payment_module = website_payment.id_payment_module WHERE payment_module.id_payment_module = ' . $id . ' limit 1')->row();

	$obj = false;
	if ($query) {
	    $obj = $this->load->object('Payment_method_domain');
	    $obj->set_id($query->id_payment_module);
	    $obj->set_name($query->name);
	    $obj->set_description($query->description);
	    
	    if ($query->logo)
		$obj->set_image_url("/_grafik/paylogo_" . $query->id_payment_module . ".gif");
	    else
		$obj->set_image_url(false);
	}

	return $obj;
    }


    function get_payment_method_list($website_id) {
        $query = $this->db->query("SELECT id_payment_module FROM website_payment WHERE id_website = " . $website_id . " ORDER BY sort")->result_array();

	$obj_array = false;
	if ($query) {
	    for($i=0; $i<=count($query)-1; $i++) {
		$obj_array[$i] = $this->get_payment_method($query[$i]["id_payment_module"]);
	    }
	}

	return $obj_array;
    }


}