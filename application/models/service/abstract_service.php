<?php
//namespace service;

class Abstract_service extends CI_Model {

    function Abstract_service() {
        // Call the Model constructor
        //parent::__construct();

	//echo("abstract loaded...\n");
    }


    function persist($object) {
	$this->doctrine->em->persist($object);
	$this->doctrine->em->flush();
	return $object;
    }

}
