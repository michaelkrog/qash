<?php

class Organisation extends CI_Controller {

    
	function edit($id = 0)
	{
	    $this->load->model("Organisation_model");
	    $this->output->set_output($this->Organisation_model->get_edit($id));
	}

}