<?php

class User extends CI_Controller {

    
	function listing()
	{
	    // Load services
	    $this->load->model("user_model");
	    $this->output->set_output($this->user_model->get_list());

	    //echo(json_encode($data));
	}


	function edit($id = 0)
	{
	    $this->load->model("User_model");
	    $this->output->set_output($this->User_model->get_edit($id));

	    //echo(json_encode($data));
	}

}