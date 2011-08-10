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

class Informations extends Controller {

	function _output($output)
	{
	    echo($output . 'pageload:ok');
	}

	
	function edit()
	{
		// *** Create header *** //
		$data = "";
		$data['id'] = '';
		$data['header'] = 'Rediger informationer';
		$data['use_close'] = true;
		$data['buttons'] = array();

		$this->load->view('admin/header', $data);
		

		// *** Create form *** //
		$data = "";
		$data['id'] = 'informations_edit';
		$data['header'] = '';
		$data['actions'] = 'ActionAlert';
		$data['mode'] = 'admin/process';
		$data['target'] = $this->encrypt->encode('Update_website');
		$data['target_id_names'] = $this->encrypt->encode('');
		$data['target_id_values'] = $this->encrypt->encode(''); //$this->Website->config_website['id_website']

		// Load data into the form
		$row = $this->Website->get_website()->row();

		$data['setup_array'] = array(
		    array("Butiks navn","","input","100","",$row->website_name,"str_website_name",""),
		    array("Virksomheds navn","","input","100","",$row->website_company,"str_website_company",""),
		    array("CVR nummer","","input","100","",$row->website_cvr,"str_website_cvr",""),
		    array("Adresse","","input","100","",$row->website_address,"str_website_address",""),
		    array("Postnr","","input","100","",$row->website_zip,"str_website_zip",""),
		    array("By","","input","100","",$row->website_city,"str_website_city",""),
		    array("Telefon","","input","100","",$row->website_telephone,"str_website_telephone",""),
		    array("Email","Emailen bruges også ved login i administrationen","input","100","required",$row->email,"str_email",""),
		);
		
		$this->load->view('admin/form', $data);

		//$this->output->enable_profiler(TRUE);
	}
}