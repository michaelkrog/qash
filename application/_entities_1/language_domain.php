<?php
class Language_domain extends CI_Model {
    
    function Language_domain() {
        // Call the Model constructor
        parent::__construct();
    }
    
    public $id = null;
    public $name = null;

    public function get_id() {
	return $this->$id;
    }

    public function set_id($id) {
	$this->$id = $id;
    }
      
    public function get_name() {
	return $this->$name;
    }

    public function set_name($name) {
	$this->$name = $name;
    }

}