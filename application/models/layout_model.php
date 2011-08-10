<?php
class Layout_model extends CI_Model {
    //public $config_website;

    function Layout_model()
    {
	
	// Load global data into public array, for use throughout the modules
	//
        // Call the Model constructor
        parent::__construct();

	$this->load->model("service/Template_service");
	$this->load->model("service/Module_service");
	$this->load->model("service/User_service");
	$this->load->model("service/Page_service");
	
/*
	// Get domain
	$website_domain = str_replace("www.","",$_SERVER["HTTP_HOST"]);

	// Load global website config
	$query = $this->db->query("SELECT * FROM website INNER JOIN website_domain ON website.id_website = website_domain.id_website WHERE deleted = 0 AND website_domain = '" . $website_domain . "' LIMIT 1");

	// Redirect to ShoppinNet if website was not found
	if ($query->num_rows() == 0) {
	    header("Location: http://www.shoppinnet.com");
	    die();
	}

	$data[0]["website_language"] = 1;

	// Load user data
	$query2 = $this->db->query("SELECT name as user_name FROM user LIMIT 1");
	
	// Save data to public variable
	$this->config_website = array_merge($query->row_array(), $query2->row_array(), $data);
*/
    }


    // Prepare layout data. Require website object.
     function get_layout($website) {

	// Load template
	$template = $this->Template_service->get($website->get_template());

	// Load modules
	$modules = $this->Module_service->get_list($template->get_id()); // $this->Website_model->get_layout_modules()->result_array();

	// Load language for modules
	$this->lang->load('module', 'danish');
	$this->load->helper('language');

	// Load user
	//$user = $this->User_service->get();

	// Load module views into their respective sections
	$data["section_1"] = "";
	$data["section_2"] = "";
	$data["section_3"] = "";
	$data["section_4"] = "";
	$data["section_5"] = "";
	$data["section_6"] = "";
	$data["section_7"] = "";
	//$data["js"] = "";

	for($i=0; $i<=count($modules)-1; $i++) {

	    $module = $modules[$i];
	   
	    //$data_module["website"] = $website;
	    //$data_module["user"] = $user;
	    //$data_module["body"] = $module->get_body();
	    //$data_module["hash"] = $module->get_hash();
	    
	    $data["section_" . $module->get_section()] .= "<div id=\"ajaxmodule_" . $module->get_id() . "\"></div>"; //$this->load->view("module", $data_module, true);

	    //$data["js"] .= $module->get_js();
	}

	// Create data
	$data["charset"] = $this->config->item("charset");
	//$data["template"] = $template->get_body();

	$data["website_name"] = $website->get_name();
	$data["website_logo"] = "";

	$data["page_title"] = "";
	$data["page_description"] = "";
	$data["page_keywords"] = "";

	return $data;
    }


    function get_page($website, $id) {
        $page = $this->Page_service->get($website, $id);
	return $page;

    }


    function get_module($id) {
	$module = $this->Module_service->get($id);
	return $module;
    }





    // Get payment methods associated to this website
    // Specific payment method, if optional $id is defined
    function get_website_payment_method_data($id = 0) {
	$sql = "";
	
	if ($id > 0)
	    $sql = " AND payment_module.id_payment_module = " . db_int_fix($id);

	$query = $this->db->query("SELECT *,payment_module.id_payment_module as id, CONCAT(payment_module.name,' (',payment_module_provider.name,')') AS name FROM payment_module INNER JOIN website_payment ON payment_module.id_payment_module = website_payment.id_payment_module INNER JOIN payment_module_provider ON payment_module.id_payment_module_provider = payment_module_provider.id WHERE id_website = " . $this->config_website['id_website'] . $sql . " ORDER BY payment_url,  payment_module.name");
   	// website_payment.id_website_payment as id
	return $query;
    }


    // Get all payment methods
    function get_payment_method_data($id = 0) {
	$sql = "";

	if ($id > 0)
	    $sql = " WHERE payment_module.id_payment_module = " . db_int_fix($id);

	$query = $this->db->query("SELECT *, id_payment_module AS id, CONCAT(payment_module.name,' (',payment_module_provider.name,')') AS name FROM payment_module INNER JOIN payment_module_provider ON payment_module.id_payment_module_provider = payment_module_provider.id " . $sql . " ORDER BY id_payment_module_provider");
	return $query;
    }
    
    
    function get_website_domain($id = 0) {
	$sql = "";

	if ($id > 0)
	    $sql = " AND id_website_domain = " . db_int_fix($id);

	$query = $this->db->query("SELECT *, id_website_domain AS id, primary_domain AS yesno_primary_domain FROM website_domain WHERE id_website =  " . $this->config_website['id_website'] . $sql . " ORDER BY primary_domain DESC");
	return $query;
    }


    function get_website_freight($id = 0) {
	$sql = "";

	if ($id > 0)
	    $sql = " AND id_freight = " . db_int_fix($id);

	$query = $this->db->query("SELECT *, id_freight AS id FROM freight WHERE id_website = " . $this->config_website['id_website'] . $sql . " ORDER BY kg_from");
	return $query;
    }

}
?>
