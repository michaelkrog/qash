<?php

class Domain_service extends Abstract_service {


    function get($id = 0) {

	$domain = $this->doctrine->em->find('entities\Domain', $id);

	return $domain;
    }

}