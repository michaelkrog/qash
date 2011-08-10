<?php

/*
|--------------------------------------------------------------------------
| View
|--------------------------------------------------------------------------
|
| Overall page setup
|
*/

class Module extends CI_Controller {

	function Module()
	{
		parent::__construct();

		$this->load->model("Layout_model");
		$this->load->model("service/Website_service");

		//$this->output->cache(1);
		//$this->output->enable_profiler(TRUE);
	}

	function index() {
	    $website = $this->Website_service->get();
	    //die("Test: ".$website->get_id());
	   
	    $layout = $this->Layout_model->get_module($website);

	    //Page deactivated, until plan is more clear. A page consists of module blocks, not a body
	    $layout["content"] = ""; //$this->Layout_model->get_page($website, 0);
	    
	    $this->load->view("layout", $layout);
	}

}