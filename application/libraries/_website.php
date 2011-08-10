<?php
//namespace service;
//use service\Abstract_service;

class Website {

    public function __construct() {
        // Call the Model constructor
        //parent::__construct();

	echo("loaded service...\n");
    }


     function get_website($id = 0) {
	//$obj = $this->doctrine->em->find('entities\Website', $id);
	//return $obj;
	 echo("Works!\n");
    }

}