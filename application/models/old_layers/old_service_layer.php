<?php
class Service_layer extends CI_Model {

    function Service_layer() {
        // Call the Model constructor
        parent::__construct();

	//$this->load->model('layers/Data_layer');
    }


     function get_website($id = 0) {
	$obj = $this->doctrine->em->find('entities\Website', 1);
	return $obj;
    }

    function get_template($id) {
        //$obj = $this->Data_layer->get_template($id);
	return $obj;
    }

    function get_module($id) {
        //$obj = $this->Data_layer->get_module($id);
	return $obj;
    }

    function get_module_list($template) {
        //$obj = $this->Data_layer->get_module_list($template);
	return $obj;
    }

    function get_page($website_id, $id = "") {
        //$obj = $this->Data_layer->get_page($website_id, $id);
	return $obj;
    }

    function get_user($id = 0) {
        $obj = $this->em->get_user($id);
	return $obj;
    }

    function get_user_list($website_id) {
        //$obj = $this->Data_layer->get_user_list($website_id);
	return $obj;
    }

    function persist_user($user) {
	$obj = $this->Data_layer->persist_user($user);
	return $obj;
    }

    function get_payment_method($id) {
        $obj = $this->Data_layer->get_payment_method($id);
	return $obj;
    }

    function get_payment_method_list($website_id) {
        $obj = $this->Data_layer->get_payment_method_list($website_id);
	return $obj;
    }
}