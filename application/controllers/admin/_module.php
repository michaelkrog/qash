<?php

/*
|--------------------------------------------------------------------------
| Module controller
|--------------------------------------------------------------------------
|
*/

class Modules extends CI_Controller {

	/*function _output($output)
	{
	    echo($output . "pageload:ok");
	}*/


	function index()
	{

	    $website = $this->Service_layer->get_website();
	    $data = $this->Service_layer->get_payment_method_list($website->template);
	    $this->load->view("admin/grid", $data);
	    //$this->output->enable_profiler(TRUE);
	}


	function listing($id = 0)
	{
	    $website = $this->Service_layer->get_website();
	    $data = $this->Service_layer->get_payment_method_list($website->template);
	    echo(json_encode($data));
	}

	function edit($id = 0)
	{
		// Create header
		$data = "";
		$data["id"] = "";
		$data["header"] = "Rediger domæne";
		$data["use_close"] = true;
		$data["buttons"] = array();

		$this->load->view("admin/header", $data);


		// Create form data
		$form_data = $this->Website->get_website_domain($id)->row();

		// Create form
		$data = "";
		$data["id"] = "domains_edit";
		$data["header"] = "";
		$data["actions"] = "ActionAlert|ActionReload";
		$data["mode"] = "admin/process";

		$data["target"] = $this->encrypt->encode("Update_website_domain");
		$data["target_id_names"] = $this->encrypt->encode("int_id_website_domain");
		$data["target_id_values"] = $this->encrypt->encode($id); //$this->Website->config_website["id_website"]

		// Setup form
		$data["setup_array"] = array(
			array("Domæne","","input_disabled","0","",$form_data->website_domain,"",""),
			array("Primært domæne","","checkbox","1","",$form_data->primary_domain,"int_primary_domain","")
		);

		$this->load->view("admin/form", $data);

		//$this->output->enable_profiler(TRUE);
	}
}