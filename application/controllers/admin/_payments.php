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

class Payments extends Controller {

	function _output($output)
	{
	    echo($output . "pageload:ok");
	}


	function index()
	{
		// *** Create header *** //
		$data = "";
		$data["id"] = "payments_list_header";
		$data["header"] = "Betalings muligheder";
		$data["use_close"] = true;
		$data["buttons"] = array(
		    array("Tilføj ny", link_popup(site_url("admin/payments/edit")))
		);

		$this->load->view("admin/header", $data);


		// *** Create grid *** //
		$data = "";
		$data["id"] = "payments_list";
		$data["header"] = "";
		$data["subheader"] = array("Betalings mulighed","Gebyr beløb","Gebyr procent");

		$data["links"] = array(
		    array("Rediger", link_popup("admin/payments/edit/{id}"))
		);
		$data["actions"] = array(
		    array("Slet",site_url("admin/process/delete/website_payment/id_payment_module"))
		);

		// Load data into grid
		$data["data_array"] = $this->Website->get_website_payment_method_data()->result_array();
		$data["cells"] = array("name","price","percent");
		
		$this->load->view("admin/grid", $data);

	}

	
	function edit($id = 0)
	{
		// Create header
		$data = "";
		$data["id"] = "";
		$data["header"] = "Tilføj betalings mulighed";
		$data["use_close"] = true;
		$data["buttons"] = array();

		$this->load->view("admin/header", $data);


		// Create form data
		$form_data = $this->Website->get_website_payment_method_data($id)->row();
		$form_settings = $this->Website->get_payment_method_data($id)->row();
		$select_list = $this->Website->get_payment_method_data()->result_array();

		// Create form
		$data = "";
		$data["id"] = "informations_edit";
		$data["header"] = "";
		$data["actions"] = "ActionAlert|ActionReload";
		$data["mode"] = "admin/process";
		$data["target"] =$this->encrypt->encode("DeleteInsert_website_payment");
		$data["target_id_names"] = $this->encrypt->encode("int_id_payment_module");
		$data["target_id_values"] = $this->encrypt->encode($form_settings->id); //$this->Website->config_website["id_website"]

		$id_website_payment = "";
		$identification = "";
		$price = "";
		$percent = "";
		$secret_text = "";
		
		if ($form_data) {
			$id_website_payment = $form_data->id_website_payment;
			$identification = $form_data->identification;
			$price = $form_data->price;
			$percent = $form_data->percent;
			$secret_text = $form_data->secret_text;
		}

		// Check if secret key should be used for this option
		if ($form_settings->secret == 1) {
			$secret_field = "input";
			$secret_title = "Hemmelig nøgle";
		}
		else {
			$secret_field = "hidden";
			$secret_title = "";
		}

		// Check if identification field should be used for this option
		$identification_field = "input";
		if ($form_settings->identification_text == "") {
			$identification_field = "hidden";
		}

		// Setup form
		$data["setup_array"] = array(
			array("","","hidden","100","",$this->Website->config_website["id_website"],"int_id_website",""),
			array("","","hidden","100","",1,"int_active",""),
			array("Betalings mulighed","",array("select",site_url("admin/payments/edit")),"0","",$form_settings->id_payment_module,"int_id_payment_module",$select_list),
			array($secret_title,"",$secret_field,"100","",$secret_text,"str_secret_text",""),
			array($form_settings->identification_text,"",$identification_field,"100","required",$identification,"str_identification",""),
			array("Gebyr DKK","","input","100","required",$price,"int_price",""),
			array("Gebyr %","","input","100","required",$percent,"int_percent","")
		);

		$this->load->view("admin/form", $data);

		//$this->output->enable_profiler(TRUE);
	}
}