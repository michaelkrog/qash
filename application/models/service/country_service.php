<?php

//namespace service;

class Country_service extends abstract_service {


    function get($id) {
	
	$obj = $this->doctrine->em->find('entities\Country', $id);

	return $obj;
    }


    function get_list() {
	$obj = $this->doctrine->em->createQuery("SELECT c FROM entities\Country c")->getResult();
	return $obj;
    }

}