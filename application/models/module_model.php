<?php
class Module_model extends CI_Model {

    function Module_model()
    {
        // Call the Model constructor
        parent::__construct();
    }


     function get_module_list($website) {

	if($payment_method) {
	    foreach($payment_method as $object) {
		if ($object->image_url) {
			echo("<img src=\"$object->image_url\">");
		}
	    }
	}

     }

}