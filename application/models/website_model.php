<?php

class Website_model extends CI_Model {

    function __construct()
    {
        parent::__construct();
    }


     function get_edit($id) {

	$data = $this->website_service->get();

	$this->load->library('form');

	$this->form->set_object("website");
	$this->form->set_id($id);
	$this->form->set_header(lang("edit_settings"));

	$this->form->set_field(array(lang("name"), "", "name", "text"));
	$this->form->set_field(array(lang("company"), "", "company", "text"));
	$this->form->set_field(array(lang("company_reg"), "", "company_reg", "text"));
	$this->form->set_field(array(lang("address"), "", "address", "text"));
	$this->form->set_field(array(lang("zip"), "", "zip", "text"));
	$this->form->set_field(array(lang("city"), "", "city", "text"));
	//$this->form->set_field(array(lang("country"), "", "country", "text"));
	$this->form->set_field(array(lang("email"), "", "email", "text"));
	$this->form->set_field(array(lang("telephone"), "", "telephone", "text"));

	$this->form->set_data($data);

	return $this->form->build();
     }

}