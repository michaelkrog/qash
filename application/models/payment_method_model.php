<?php
class Payment_method_model extends CI_Model {

    function __construct()
    {
        parent::__construct();
    }


    function get_listing() {

	$website = $this->Service_layer->get_website();
	$data = $this->Service_layer->get_payment_method_list($website->id);

	$this->load->library('grid');

	$this->grid->set_header(lang("name"));
	$this->grid->set_header(lang("description"));
	//$this->grid->set_header(lang("active"));
	
	$this->grid->set_field(array("name"));
	$this->grid->set_field(array("description"));
	//$this->grid->set_field(array("active","yesno"));
	
	//$this->grid->set_action(array(lang("delete"),"http://action1"));

	$this->grid->set_link(array(lang("edit"), "http://", "_self"));
	
	$this->grid->set_data($data);

	return $this->grid->build();

    }

}