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

class Offer extends Controller {

    	function _output($output)
	{
	    echo($output . "pageload:ok");
	}


	function options()
	{
		// Load necessary classes
		$this->load->model('module/Model_offer');

		// *** Create header *** //
		$data = "";
		$data["id"] = "offer_options_list_header";
		$data["header"] = "Felter";
		$data["functions"] = array();
		$data["buttons"] = array(
		    array("Tilføj nyt felt", link_popup(site_url("admin/offer/options_edit")))
		);

		$this->load->view("admin/header", $data);


		// *** Create grid *** //
		$data = "";
		$data["id"] = "offer_options_list";
		$data["header"] = "Et felt der tilføjes, kan bruges flere gange under felt opsætning.";
		$data["subheader"] = array("Felt","Beskrivelse");
		$data["functions"] = array();

		$data["links"] = array(
		    array("Rediger", link_popup(site_url("admin/offer/options_edit/{id}")))
		);
		$data["actions"] = array(
		    array("Slet",site_url("admin/process/delete_option"))
		);

		// Load data into grid
		$data["data_array"] = $this->Model_offer->get_fields()->result_array();
		$data["cells"] = array("name","description");
		
		$this->load->view("admin/grid", $data);

	}


	function options_edit($id = 0)
	{
		// Load necessary classes
		$this->load->model('module/Model_offer');
		
		// Create header
		$data = "";
		$data["id"] = "";
		$data["header"] = "Rediger felt";
		$data["functions"] = array("close");
		$data["buttons"] = array();

		$this->load->view("admin/header", $data);


		// Create form data
		$form_data = $this->Model_offer->get_option($id)->row();

		// Create form
		$data = "";
		$data["id"] = "options_edit";
		$data["header"] = "";
		$data["actions"] = "ActionAlert|ActionReload";
		$data["mode"] = "admin/process";

		if ($id == 0)
		    $dbmode = "Insert";
		else
		    $dbmode = "Update";

		$data["target"] = $this->encrypt->encode($dbmode . "_module_offer_option");
		$data["target_id_names"] = $this->encrypt->encode("int_id");
		$data["target_id_values"] = $this->encrypt->encode($id); //$this->Website->config_website["id_website"]

		$name = "";
		$description = "";
		if ($form_data) {
		    $name = $form_data->name;
		    $description = $form_data->description;

		}
		// Setup form
		$data["setup_array"] = array(
			array("","","hidden","0","",$this->Website->config_website["id_website"],"int_id_website",""),
			array("Felt navn","","input","100","",$name,"str_name",""),
			array("Felt beskrivelse","","input","100","",$description,"str_description","")
		);

		$this->load->view("admin/form", $data);

		//$this->output->enable_profiler(TRUE);
	}


	function setup()
	{
		// Load necessary classes
		$this->load->model('module/Model_offer');

		// *** Create header *** //
		$data = "";
		$data["id"] = "offer_setup_list_header";
		$data["header"] = "Felt opsætning";
		$data["functions"] = array();
		$data["buttons"] = array(
		    array("Tilføj ny valgmulighed", link_popup(site_url("admin/offer/setup_edit")))
		);

		$this->load->view("admin/header", $data);


		// *** Create grid *** //
		$data = "";
		$data["id"] = "offer_setup_list";
		$data["header"] = "Før du kan opsætte en valgmulighed, skal du forud have tilføjet det felt du vil opsætte.";
		$data["subheader"] = array("Valgmulighed","Type","Pris","Procent","Afhængig af");
		$data["functions"] = array("sort");

		$data["links"] = array(
		    array("Rediger", link_popup(site_url("admin/offer/setup_edit/{id}")))
		);
		$data["actions"] = array(
		    array("Slet",site_url("admin/process/delete/module_offer_option_join/id"))
		);

		// Load data into grid
		$data["data_array"] = $this->Model_offer->get_options_all()->result_array();
		$data["cells"] = array("option_name_full","option_type_name","price","yesno_percent_calculation","depends_name");

		$this->load->view("admin/grid", $data);

	}

	
	function setup_edit($id = 0)
	{
		// Load necessary classes
		$this->load->model('module/Model_offer');

		// Create header
		$data = "";
		$data["id"] = "";
		$data["header"] = "Rediger valgmulighed";
		$data["functions"] = array("close");
		$data["buttons"] = array();

		$this->load->view("admin/header", $data);

		// Create form data
		$form_data = $this->Model_offer->get_option_join($id)->row();
		$option_data = $this->Model_offer->get_fields()->result_array();
		$option_type_data = $this->Model_offer->get_options_type()->result_array();
		$option_join_data = $this->Model_offer->get_option_join()->result_array();

		// Create form
		$data = "";
		$data["id"] = "setup_edit";
		$data["header"] = "";
		$data["actions"] = "ActionAlert|ActionReload";
		$data["mode"] = "admin/process";

		$data["target"] = $this->encrypt->encode("DeleteInsert_module_offer_option_join");
		$data["target_id_names"] = $this->encrypt->encode("int_id");
		$data["target_id_values"] = $this->encrypt->encode($id); //$this->Website->config_website["id_website"]

		$option_id = "";
		$option_type_id = "";
		$option_parent_id = "";
		$name = "";
		$price = "";
		$percent_calculation = "";
		$sort = "";
		if ($id > 0) {
		    $option_id = $form_data->id;
		    $option_type_id = $form_data->option_type_id;
		    $option_parent_id = $form_data->option_parent_id;
		    $name = $form_data->name;
		    $price = $form_data->price;
		    $percent_calculation = $form_data->percent_calculation;
		    $sort = $form_data->sort;
		}

		// Setup form
		$data["setup_array"] = array(
			array("","","hidden","0","",$this->Website->config_website["id_website"],"int_id_website",""),
			//array("","","hidden","0","",$sort,"int_sort",""),
			array("Kendenavn","Et internt navn for et bedre overblik","input","50","",$name,"str_name",""),
			array("Felt","","select","0","",$option_id,"int_option_id",$option_data),
			array("Felt type","","select","0","",$option_type_id,"int_option_type_id",$option_type_data),
			array("Afhængig af","Et andet felt der styrer, om dette felt skal vises","select","0","",$option_parent_id,"int_option_parent_id",$option_join_data),

			array("Pris","Pr. stk, hvis der kan indtastes flere end 1","input","10","",$price,"int_price",""),
			array("Procent beregning","","checkbox","0","",$percent_calculation,"int_percent_calculation","")

		);

		$this->load->view("admin/form", $data);

		//$this->output->enable_profiler(TRUE);
	}
}