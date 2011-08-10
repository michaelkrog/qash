<?php

class Payment_method extends CI_Controller {

    
	function index()
	{

	}


	function listing()
	{
	    $this->load->model("Payment_method_model");
	    $this->output->set_output($this->Payment_method_model->get_listing());

	    //echo(json_encode($data));
	}

}