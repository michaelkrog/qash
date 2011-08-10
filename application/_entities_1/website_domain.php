<?php
class Website_domain {
      
    public $id = null;
    public $md5 = null;
    public $name = null;
    public $template = null;
    

    public function get_id() {
	return $this->id;
    }

    public function set_id($id) {
	$this->id = $id;
    }

    public function get_md5() {
	return $this->md5;
    }

    public function set_md5($md5) {
	$this->md5 = $md5;
    }

    public function get_name() {
	return $this->name;
    }

    public function set_name($name) {
	$this->name = $name;
    }

    public function get_template() {
	return $this->template;
    }

    public function set_template($template) {
	$this->template = $template;
    }
}