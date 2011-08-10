<?php

//namespace service;

class User_service extends abstract_service {


    function get($id = 0) {

	if ($id == 0) {
	    $this->load->model("service/Website_service");
            //$this->load->model("service/Organisation_service");
	    $this->load->model("service/User_group_service");

            $website = $this->Website_service->get(0);
            $org = $website->get_organisation();
	    $obj = new entities\User;
	    $obj->set_created(time());
	    $obj->set_organisation($org);
	    $obj->set_user_group($this->User_group_service->get(2)); // Customer
	}
	else {
	    $obj = $this->doctrine->em->find('entities\User', $id);
	}
	
	return $obj;
    }


    function get_list($org_id) {
	$obj = $this->doctrine->em->createQuery("SELECT u FROM entities\User u WHERE u.organisation = $org_id")->getResult();
	return $obj;
    }
    
}