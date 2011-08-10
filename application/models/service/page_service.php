<?php

class Page_service extends Abstract_service {


    function get($id = 0) {

	$page = $this->doctrine->em->find('entities\Page', $id);

	return $page;
    }

}