<?php

class Organsiation_service extends Abstract_service {


    function get($id = 0) {
	return $this->doctrine->em->find('entities\Organisation', $id);
    }

}