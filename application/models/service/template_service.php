<?php

class Template_service extends Abstract_service {


    function get($id = 0) {

	$template = $this->doctrine->em->find('entities\Template', $id);

	return $template;
    }

}