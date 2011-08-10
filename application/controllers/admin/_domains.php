<?php

/*
|--------------------------------------------------------------------------
| Informations_edit
|--------------------------------------------------------------------------
|
| Form for editing informations
| pageload:ok is for ajax to know loading successed
|
*/

class Domains extends Controller {

	function _output($output)
	{
	    echo($output . "pageload:ok");
	}


	function index()
	{
		// *** Create header *** //
		$data = "";
		$data["id"] = "Domains_list_header";
		$data["header"] = "Domæne navne";
		$data["use_close"] = false;
		$data["buttons"] = array(
		    array("Tilføj ny", link_popup("admin/domains/edit"))
		);

		$this->load->view("admin/header", $data);


		// *** Create grid *** //
		$data = "";
		$data["id"] = "domains_list";
		$data["header"] = "";
		$data["subheader"] = array("Domæne","Primært domæne");

		$data["links"] = array(
		    array("Rediger", link_popup("admin/domains/edit/{id}"))
		);
		$data["actions"] = array(
		    //array("Slet",site_url("admin/process/delete/website_payment/id_payment_module"))
		);

		// Load data into grid
		$data["data_array"] = $this->Website->get_website_domain()->result_array();
		$data["cells"] = array("website_domain","yesno_primary_domain");
		
		$this->load->view("admin/grid", $data);

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