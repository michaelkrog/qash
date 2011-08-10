<?php

class Category_service extends Abstract_service {


    function get($id = 0) {

	$category = $this->doctrine->em->find('entities\Category', $id);

	return $category;
    }

}