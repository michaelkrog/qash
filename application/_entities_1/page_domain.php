<?php
class Page_domain {
      
    public $id = null;
    public $website = null;
    public $title = null;
    public $description = null;
    public $keywords = null;
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

    public function get_title() {
	return $this->title;
    }

    public function set_title($title) {
	$this->title = $title;
    }

    public function get_description() {
	return $this->description;
    }

    public function set_description($description) {
	$this->description = $description;
    }

    public function get_keywords() {
	return $this->keywords;
    }

    public function set_keywords($keywords) {
	$this->keywords = $keywords;
    }

    public function get_body() {
	return $this->body;
    }

    public function set_body($body) {
	$this->body = $body;
    }

}