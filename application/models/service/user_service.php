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
	    $obj = new entities\User;
	    $obj->set_created(time());

	    // Don't know about which default organisation, so just set to 1 to prevent errors for now
	    $obj->set_organisation($this->Organisation_service->get(1));

	    $obj->set_user_group($this->User_group_service->get(1)); // Customer as default
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