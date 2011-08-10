<?php

/*
|--------------------------------------------------------------------------
| Offer
|--------------------------------------------------------------------------
|
| Builds offer page for calculation
| pageload:ok is for ajax to know loading successed
|
*/

class Offer extends CI_Controller {

	function _output($output)
	{
	    echo($output . "pageload:ok");
	}


	function index()
	{
        	$data = "";
		$data["options"] = "";
		$options = "";
		$selectbox_started = false;

		// Load options data
		$this->load->model('module/Model_offer');
		$options_array = $this->Model_offer->get_options_primary()->result_array();

		foreach($options_array as $row) {
		    $options["option_type_id"] = $row['option_type_id'];
		    $options["option_join_id"] = $row['option_join_id'];
		    $options["option_name"] = $row['name'];
		    $options["option_description"] = $row['description'];

		    $options["option_childs_select"] = "";
		    
		    // Append dropdown option with dropdown options view
		    if ($row['option_type_id'] == 1) {
			$options["option_childs_select"] = $this->Model_offer->get_childs_select($row['option_join_id']);
		    }
		   
		    $data["options"] .= $this->load->view('module/offer_option', $options, true);
		}

		// Load entire offer-module view
		$this->load->view("module/offer", $data);

	}

	
}