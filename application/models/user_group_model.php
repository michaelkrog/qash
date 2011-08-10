<?php

class User_group_model extends CI_Model {

    function __construct()
    {
        parent::__construct();

	$this->load->model("service/user_group_service");
    }


    function get_list() {

	$website = $this->website_service->get();
	$data = $this->user_group_service->get_list($website->get_id());

	$this->load->library('grid');

	$this->grid->set_object("user_group");

	$this->grid->set_header(lang("name"));
	$this->grid->set_header(lang("default"));

	$this->grid->set_field(array("name"));
	$this->grid->set_field(array("is_default"),"yesno");

	$this->grid->set_action(array(lang("delete"), link_action_delete("/index.php/admin/user_group/delete/")));
	$this->grid->set_action(array(lang("edit"), link_action_dialog("/index.php/admin/user_group/edit/")));

	$this->grid->set_data($data);

	return $this->grid->build();

    }


     function get_edit($id) {

	//$website = $this->website_service->get();
	$data = $this->user_group_service->get($id);

	$this->load->library('form');

	$this->form->set_object("user_group");
	$this->form->set_id($id);
	$this->form->set_header(lang("edit_user_group"));

	$this->form->set_field(array(lang("name"), "", "name", "text"));
	$this->form->set_field(array(lang("default"), "", "is_default", "checkbox"));

	$this->form->set_data($data);

	return $this->form->build();
     }

}