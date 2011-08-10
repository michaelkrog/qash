<?php
class Object_model extends CI_Model {

    function __construct()
    {
        parent::__construct();
    }


    function save() {

	// Prepare variables
	$id = $_POST['id'];
	$object = $_POST["object"];
	$service_layer = $object . "_service";
	//$get_function = "get_" .  $object;
	//$persist_function = "persist_" .  $object;

	// Load object data, as defined in the posted data
	$this->load->model('service/' . $service_layer);
	$data = $this->$service_layer->get($id);

	// Loop posted array, and update corresponding object data with the posted values
	foreach($_POST as $property=>$value) {
	    //echo("\n".$property." = ".$value."\n");

	    if ($property != "object" && $property != "id") {
		//echo($property." - ".$value."\n");

		// Check if it is an association, which should be passed as a complete loaded entity object
		if (strrpos($property,"_association") > 0) {
		    $property = str_replace("_association", "", $property);
		    $association_layer = $property . "_service";
		    $this->load->model('service/' . $association_layer);
		    $value = $this->$association_layer->get($value);
		}

		$set_function = "set_" . $property;
		$data->$set_function($value);
	    }

	}

	// Persist object data
	$this->$service_layer->persist($data);
    }

}