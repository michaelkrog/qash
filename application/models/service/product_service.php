<?php

class Product_service extends Abstract_service {


    function get($id = 0) {

	$product = $this->doctrine->em->find('entities\Product', $id);

	return $product;
    }

}