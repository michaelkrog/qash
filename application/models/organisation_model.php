<?php

class Organisation_model extends CI_Model {

    function __construct()
    {
        parent::__construct();
    }


     function get_edit() {

	$this->load->model('service/organisation_service');

	$website = $this->website_service->get();
	$data = $website->get_organisation();

	$this->load->library('form');

	$this->form->set_object("organisation");
	$this->form->set_id($data->get_id());
	$this->form->set_header(lang("edit_settings"));

	$this->form->set_field(array(lang("name"), "", "name", "text"));
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