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

class Settings extends Controller {

	function _output($output)
	{
	    echo($output . 'pageload:ok');
	}

	
	function edit()
	{
		// *** Create header *** //
		$data = "";
		$data['id'] = '';
		$data['header'] = 'Rediger indstillinger';
		$data['use_close'] = true;
		$data['buttons'] = array();

		$this->load->view('admin/header', $data);
		

		// *** Create form *** //
		$data = "";
		$data['id'] = 'settings_edit';
		$data['header'] = '';
		$data['actions'] = 'ActionAlert';
		$data['mode'] = 'admin/process';
		$data['target'] = $this->encrypt->encode('Update_website');
		$data['target_id_names'] = $this->encrypt->encode('');
		$data['target_id_values'] = $this->encrypt->encode(''); //$this->Website->config_website['id_website']

		// Load data into the form
		$row = $this->Website->get_website()->row();

		$data['setup_array'] = array(
		    array("Kort kan vedlægges ved køb","","checkbox","0","",$row->user_gift,"int_user_gift",""),
		    array("Email kopi til dig ved køb","","checkbox","0","",$row->admin_order_email,"int_admin_order_email",""),
		    array("Email til 3. part ved betaling","F.eks. til leverandør som godkendelse af afsendelse","input","100","",$row->supplier_order_email,"str_supplier_order_email",""),
		    array("Velkomst rabat til nye kunder","Et beløb første gang kunden handler","input","100","",$row->show_ship_card,"int_show_ship_card","")
		);
		
		$this->load->view('admin/form', $data);

		//$this->output->enable_profiler(TRUE);
	}
}