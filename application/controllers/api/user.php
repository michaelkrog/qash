<?php

/*
|--------------------------------------------------------------------------
| Api
|--------------------------------------------------------------------------
|
| An api serving website as json
|
*/

class User extends CI_Controller {

	function User()
	{
		parent::__construct();
	}

	function get($id)
	{
	    $this->load->model("service/User_service");
	    echo(json_encode($this->User_service->get($id)));
	}

}
