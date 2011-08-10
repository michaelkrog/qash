<?php
class Payment_method_domain extends CI_Model {

    function Payment_method_domain() {
        // Call the Model constructor
        parent::__construct();
    }

    public $id = null;
    public $name = null;
    public $description = null;
    public $image_url = null;

    public function get_id() {
	return $this->id;
    }

    public function set_id($id) {
	$this->id = $id;
    }

    public function get_name() {
	return $this->name;
    }

    public function set_name($name) {
	$this->name = $name;
    }

    public function get_description() {
	return $this->description;
    }

    public function set_description($description) {
	$this->description = $description;
    }

    public function get_image_url() {
	return $this->image_url;
    }

    public function set_image_url($image_url) {
	$this->image_url = $image_url;
    }
}