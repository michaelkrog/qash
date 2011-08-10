<?php
class Module_domain {
      
    public $id = null;
    public $template = null;
    public $body = null;
    public $section = null;
    public $sort = null;
    public $url = null;

    public function get_id() {
	return $this->id;
    }

    public function set_id($id) {
	$this->id = $id;
    }

    public function get_template() {
	return $this->id;
    }

    public function set_template($id) {
	$this->id = $id;
    }

    public function get_body() {
	return $this->body;
    }

    public function set_body($body) {
	$this->body = $body;
    }

    public function get_section() {
	return $this->section;
    }

    public function set_section($section) {
	$this->section = $section;
    }

    public function get_sort() {
	return $this->sort;
    }

    public function set_sort($sort) {
	$this->sort = $sort;
    }

    public function get_url() {
	return $this->url;
    }

    public function set_url($url) {
	$this->url = $url;
    }

    public function get_php() {
	return $this->php;
    }

    public function set_php($php) {
	$this->php = $php;
    }
}