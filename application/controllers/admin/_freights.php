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

class Freights extends Controller {

	function _output($output)
	{
	    echo($output . "pageload:ok");
	}


	function index()
	{
		// *** Create header *** //
		$data = "";
		$data["id"] = "freights_list_header";
		$data["header"] = "Fragt indstillinger";
		$data["use_close"] = false;
		$data["buttons"] = array(
		    array("Tilføj ny", link_popup(site_url("admin/freights/edit")))
		);

		$this->load->view("admin/header", $data);


		// *** Create grid *** //
		$data = "";
		$data["id"] = "freights_list";
		$data["header"] = "";
		$data["subheader"] = array("Fra kg","Til kg","Pris (DKK)");

		$data["links"] = array(
		    //array("Rediger", link_popup("admin/freights/edit/{id}"))
		);
		$data["actions"] = array(
		    array("Slet",site_url("admin/process/delete/freight/id_freight"))
		);

		// Load data into grid
		$data["data_array"] = $this->Website->get_website_freight()->result_array();
		$data["cells"] = array("kg_from","kg_to","price");
		
		$this->load->view("admin/grid", $data);

	}

	
	function edit($id = 0)
	{
		// Create header
		$data = "";
		$data["id"] = "";
		$data["header"] = "Tilføj fragt";
		$data["use_close"] = true;
		$data["buttons"] = array();

		$this->load->view("admin/header", $data);


		// Create form data
		//$form_data = $this->Website->get_website_freight($id)->row();

		// Create form
		$data = "";
		$data["id"] = "freights_edit";
		$data["header"] = "";
		$data["actions"] = "ActionAlert|ActionReload";
		$data["mode"] = "admin/process";
		$data["target"] =$this->encrypt->encode("Insert_freight");
		$data["target_id_names"] = $this->encrypt->encode("");
		$data["target_id_values"] = $this->encrypt->encode("");

		// Setup form
		$data["setup_array"] = array(
			array("","","hidden","100","",$this->Website->config_website["id_website"],"int_id_website",""),
			array("Pris (DKK)","","input","100","","","int_price",""),
			array("Kg fra","","input","100","","","int_kg_from",""),
			array("Kg til","","input","100","required","","int_kg_to","")
		);

		$this->load->view("admin/form", $data);

		//$this->output->enable_profiler(TRUE);
	}
}