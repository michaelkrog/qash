<?php
 
/*
|--------------------------------------------------------------------------
| View
|--------------------------------------------------------------------------
|
| Overall page setup
|
*/

class View extends CI_Controller {

	function View()
	{
		parent::__construct();

		$this->load->model("Layout_model");
		$this->load->model("service/Website_service");

		//$this->output->cache(1);
		//$this->output->enable_profiler(TRUE);
	}

	function index() {
	    $website = $this->Website_service->get();
	    $layout = $this->Layout_model->get_layout($website);

	    //Page deactivated, until plan is more clear. A page consists of module blocks, not a body
	    $layout["content"] = ""; //$this->Layout_model->get_page($website, 0);
	    
	    $this->load->view("layout", $layout);
	}

	function module($id) {
	    $website = $this->Website_service->get();
	    $module = $this->Layout_model->get_module($id);

	    $data["body"] = $module->get_body();
	    $data["js"] = $module->get_js();

	    $this->load->view("module", $data);
	}


	function orgtest($id) {
	    $this->load->model("service/Organisation_service");
	    $org = $this->Organisation_service->get($id);
            if(is_null($org)) {
                show_404();
            }
	    echo("Org:" . $org->get_name());
	}



	 
	// Below here is old and before doctrine arrived


	function category($language,$id) {
	    $website = $this->Service_layer->get_website();
	    $layout = $this->Layout_model->get_layout($website);

	    $layout["content"] = $this->load->view("module/productlist", "", true);
	    $layout["content"] .= $this->Layout_model->get_page($website, "productdetails");

	    $this->load->view("layout", $layout);
	}

	function product($language,$id) {    
	    $website = $this->Service_layer->get_website();
	    $layout = $this->Layout_model->get_layout($website);

	    $layout["content"] = $this->load->view("module/productdetails", "", true);
	    $layout["content"] .= $this->load->view("module/productrelated", "", true);
	    $layout["content"] .= $this->Layout_model->get_page($website, "productdetails");

	    $this->load->view("layout", $layout);
	}

	function cart($language,$id = 0) {
	    $website = $this->Service_layer->get_website();
	    $layout = $this->Layout_model->get_layout($website);
	    
	    $layout["content"] = $this->load->view("module/cart", "", true);
	    $layout["content"] .= $this->Layout_model->get_page($website, "cart");

	    $this->load->view("layout", $layout);
	}

	function login($language) {
	    $website = $this->Service_layer->get_website();
	    $layout = $this->Layout_model->get_layout($website);

	    $layout["content"] = $this->load->view("module/login", "", true);
	    $layout["content"] .= $this->Layout_model->get_page($website, "login");

	    $this->load->view("layout", $layout);
	}

	function page($language,$id) {
	 
	    $website = $this->Service_layer->get_website();
	    $layout = $this->Layout_model->get_layout($website);
	    $layout["content"] = $this->Layout_model->get_page($website, $id);

	    if (!$layout["content"])
		header("Location: /index.php");

	    $this->load->view("layout", $layout);
	}

}