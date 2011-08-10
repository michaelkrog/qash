<?php
class Template_domain {
      
    public $id = null;
    public $website = null;
    public $body = null;

    public function get_id() {
	return $this->id;
    }

    public function set_id($id) {
	$this->id = $id;
    }

    public function get_website() {
	return $this->website;
    }

    public function set_website($website) {
	$this->website = $website;
    }

    public function get_body() {
	return $this->body;
    }

    public function set_body($body) {
	$this->body = $body;
    }

}