<?php

class User_group extends CI_Controller {


	function listing()
	{
	    // Load services
	    $this->load->model("user_group_model");
	    $this->output->set_output($this->user_group_model->get_list());
	}


	function edit($id = 0)
	{
	    $this->load->model("User_group_model");
	    $this->output->set_output($this->User_group_model->get_edit($id));
	}

}