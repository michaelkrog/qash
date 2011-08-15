<?php
require_once str_replace('//','/',dirname(__FILE__).'/') . 'AbstractCrud.php';

use entities\Module;

class Module_service extends AbstractCrud {

    public function __construct() {
        parent::__construct("entities\Module");
    }

    /**
     * Creates a new instance and returns the id of the instance.
     */
    public function create(){
        $e = new Module();
        $e = $this->update($e);
        return $e->get_id();
    }
    
    /**
     * @deprecated
     */
    function get($id = 0) {
        return $this->read($id);
    }


    /**
     * @deprecated
     */
    function get_list($website_id) {
	$obj = $this->doctrine->em->createQuery("SELECT m FROM entities\Module m WHERE m.website = $website_id")->getResult();
	return $obj;
    }

}