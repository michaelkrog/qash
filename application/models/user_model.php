<?php

class User_model extends CI_Model {

    function __construct()
    {
        parent::__construct();
    }


    function get_list() {

	$website = $this->website_service->get();
        $org = $website->get_organisation();
	$data = $this->user_service->get_list($org->get_id());

	$this->load->library('grid');

	$this->grid->set_object("user");

	$this->grid->set_header(lang("company"));
	$this->grid->set_header(lang("name"));
	$this->grid->set_header(lang("address"));
	$this->grid->set_header(lang("zip"));
	$this->grid->set_header(lang("city"));
	$this->grid->set_header(lang("telephone"));
	//$this->grid->set_header(lang("email"));
	//$this->grid->set_header(lang("newsletter"));

	$this->grid->set_field(array("company"));
	$this->grid->set_field(array("name"));
	$this->grid->set_field(array("address"));
	$this->grid->set_field(array("zip"));
	$this->grid->set_field(array("city"));
	$this->grid->set_field(array("telephone"));
	//$this->grid->set_field(array("email"));
	//$this->grid->set_field(array("newsletter","yesno"));
	
	$this->grid->set_action(array(lang("edit"), link_action_dialog("/index.php/admin/user/edit/")));
	$this->grid->set_action(array(lang("delete"), link_action_delete("/index.php/admin/user/delete/")));
	//$this->grid->set_action(array(lang("send_password"), "http://"));
	//$this->grid->set_action(array(lang("send_email"), "mailto:{email}"));

	$this->grid->set_data($data);

	return $this->grid->build();

    }


     function get_edit($id) {

	$this->load->model("service/user_group_service");
	$this->load->model("service/country_service");

	$website = $this->website_service->get();
	$data = $this->user_service->get($id);
	
	$this->load->library('form');

	$this->form->set_object("user");
	$this->form->set_id($id);
	$this->form->set_header(lang("edit_user"));

	$this->form->set_field(array(lang("company"), "", "company", "text"));
	//$this->form->set_field(array(lang("company_reg"), "", "company_reg", "text"));
	$this->form->set_field(array(lang("name"), "", "name", "text"));
	$this->form->set_field(array(lang("address"), "", "address", "text"));
	$this->form->set_field(array(lang("address"), "", "address2", "text"));
	$this->form->set_field(array(lang("zip"), "", "zip", "text"));
	$this->form->set_field(array(lang("city"), "", "city", "text"));
	$this->form->set_field(array(lang("country"), "", "country", "select", $this->country_service->get_list() ));
	$this->form->set_field(array(lang("email"), "", "email", "email"));
	$this->form->set_field(array(lang("telephone"), "", "telephone", "text"));
	$this->form->set_field(array(lang("website"), "", "website_url", "text"));
	$this->form->set_field(array(lang("newsletter"), "", "newsletter", "checkbox"));
	$this->form->set_field(array(lang("user_group"), "", "user_group", "select", $this->user_group_service->get_list($website->get_id()) ));

	$this->form->set_data($data);

	return $this->form->build();
     }

}