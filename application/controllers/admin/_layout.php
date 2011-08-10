<?php

/*
|--------------------------------------------------------------------------
| Layout
|--------------------------------------------------------------------------
|
| Overall admin layout
| Everything else is loaded by ajax
|
*/

class Layout extends Controller {

	function Layout()
	{
		parent::Controller();	
	}
	
	function index()
	{
		$data['charset'] = $this->config->item('charset');
		$data['website_partner_export_allow'] = 0;
		$data['website_auction_bot'] = 0;
		$data['thisdomain'] = "www.shoppinnet.com";

		$this->load->view('admin/layout', $data);

		//$this->output->enable_profiler(TRUE);
	}
}