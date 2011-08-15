<?php
require_once str_replace('//','/',dirname(__FILE__).'/') . 'AbstractCrud.php';

use entities\Country;

class Country_service extends AbstractCrud {

    public function __construct() {
        parent::__construct("entities\Country");
    }

    /**
     * Creates a new instance and returns the id of the instance.
     */
    public function create(){
        $e = new Country();
        $e = $this->update($e);
        return $e->get_id();
    }

    /**
     * @deprecated
     */
    function get($id) {
	return $this->read($id);
    }


    /**
     * @deprecated
     */
    function get_list() {
	$obj = $this->doctrine->em->createQuery("SELECT c FROM entities\Country c")->getResult();
	return $obj;
    }

}