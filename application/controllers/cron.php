<?php

/*
|--------------------------------------------------------------------------
| Cron jobs
|--------------------------------------------------------------------------
|
| Automated server jobs
|
*/

class Cron extends Controller {

	function Cron()
	{
		parent::Controller();
	}

	function index()
	{
	    echo("Intet her!");
	}

	function build_language_files()
	{
		// Load models
		$this->load->model('Language');
		
		// Build files
		$this->Language->build_files_modules();

	}
}