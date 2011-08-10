<?php

//namespace service;

class User_group_service extends abstract_service {


    function get($id) {
	
	if ($id == 0) {
	    $this->load->model("service/Website_service");

	    $obj = new entities\User_group();
	    $obj->set_website($this->Website_service->get(0));
	}
	else
	    $obj = $this->doctrine->em->find('entities\User_group', $id);

	return $obj;
    }


    function get_list($website_id) {
	$obj = $this->doctrine->em->createQuery("SELECT u FROM entities\User_group u WHERE u.website = $website_id OR u.website = 0")->getResult();
	return $obj;
    }

}