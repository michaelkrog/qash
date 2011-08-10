<?php

class Object extends CI_Controller {

    	function save()
	{
	    $this->load->model("Object_model");
	    $this->Object_model->save();

	    //$this->output->set_output("");
	}

}