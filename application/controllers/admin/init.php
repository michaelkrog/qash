<?php

/*
|--------------------------------------------------------------------------
| View
|--------------------------------------------------------------------------
|
| Overall page setup
|
*/

//use service\Website;

class Init extends CI_Controller {

	function Init()
	{
		parent::__construct();

		//Check session via en helper

		//$this->output->cache(1);
		//$this->output->enable_profiler(TRUE);
	}


	function test()
	{
	    $website = $this->website_service->get_website();
            $org = $website->get_organisation();

	    $user = new entities\User;

	    $user->set_company('John Doe Company');
	    $user->set_name('John Doe');
	    $user->set_organisation($org);
	    
	    $this->doctrine->em->persist($user);
	    $this->doctrine->em->flush();

	    


	    //$this->load->model("user_model");
	    //echo(link_main("tester"));

	    //$website = $this->Website->get_website(1);
	    echo($org->get_name());

	}


	function index()
	{
	    // Collect data
	    $website = $this->website_service->get();

	    // Switch to shoppinnet if no website was available
	    if (!$website) {
		// Redirect to ShoppinNet because no website was found
		header("Location: http://www.shoppinnet.com");
	    }

	    // Load layout
	    $this->load->view("admin/layout", "");
	}


	function jslanguage()
	{
	    $jslang["server_failed"] = "Failed! (To be translated)"; // lang("admin_server_failed");

	    echo(json_encode($jslang));
	}
}