<?php

/*
|--------------------------------------------------------------------------
| Api
|--------------------------------------------------------------------------
|
| An api serving website as json
|
*/

class Website extends CI_Controller {

	function Website()
	{
		parent::__construct();
	}

	function get()
	{
	    $this->load->model("service/Website_service");
	    echo(json_encode($this->Website_service->get()));
	}

}
