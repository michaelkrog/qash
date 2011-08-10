<?php

class Website extends CI_Controller {

    
	function edit($id = 0)
	{
	    $this->load->model("Website_model");

	    //die("test");
	    $this->output->set_output($this->Website_model->get_edit($id));
	}

}