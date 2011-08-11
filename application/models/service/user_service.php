<?php

//namespace service;

class User_service extends abstract_service {


    function get($id = 0) {

	// If no user, create one and return it.
	if ($id == 0) {
	    $this->load->model("service/Organisation_service");
	    $this->load->model("service/User_group_service");
	    $this->load->model("service/Country_service");
	    $this->load->model("service/Website_service");

            $website = $this->Website_service->get(0);
	    $user_group = $this->doctrine->em->createQuery("SELECT u FROM entities\User_group u WHERE u.is_default = 1 AND u.website = " . $website->get_id())->getResult();

	    $obj = new entities\User;
	    $obj->set_created(time());
	    $obj->set_organisation($website->get_organisation());
	    $obj->set_user_group($user_group[0]);
	    $obj->set_country($this->Country_service->get(1)); // Danish as default
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