<?php

require_once str_replace('//','/',dirname(__FILE__).'/') . 'AbstractCrud.php';

use entities\Organisation;

class Organisation_service extends AbstractCrud{

    public function __construct() {
        parent::__construct("entities\Organisation");
    }

    /**
     * Creates a new instance and returns the id of the instance.
     */
    public function create(){
        $org = new Organisation();
        $org = $this->update($org);
        return $org->get_id();
    }


}