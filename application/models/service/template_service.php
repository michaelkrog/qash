<?php
require_once str_replace('//','/',dirname(__FILE__).'/') . 'AbstractCrud.php';

use entities\Template;

class Template_service extends AbstractCrud {

    public function __construct() {
        parent::__construct("entities\Template");
    }

    /**
     * Creates a new instance and returns the id of the instance.
     */
    public function create(){
        $e = new Template();
        $e = $this->update($e);
        return $e->get_id();
    }
    
    function get($id = 0) {
        $this->read($id);
    }

}