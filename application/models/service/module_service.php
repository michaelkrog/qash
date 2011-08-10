<?php

class Module_service extends Abstract_service {


    function get($id = 0) {

	$module = $this->doctrine->em->find('entities\Module', $id);

	return $module;
    }


    function get_list($website_id) {
	$obj = $this->doctrine->em->createQuery("SELECT m FROM entities\Module m WHERE m.website = $website_id")->getResult();
	return $obj;
    }

}