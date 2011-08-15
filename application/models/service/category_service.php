<?php
require_once str_replace('//','/',dirname(__FILE__).'/') . 'AbstractCrud.php';

use entities\Category;

class Category_service extends AbstractCrud {

    public function __construct() {
        parent::__construct("entities\Category");
    }

    /**
     * Creates a new instance and returns the id of the instance.
     */
    public function create(){
        $e = new Category();
        $e = $this->update($e);
        return $e->get_id();
    }
    
    /**
     * @deprecated
     */
    function get($id = 0) {
        return $this->read($id);
    }

}