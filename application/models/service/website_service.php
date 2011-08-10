<?php

class Website_service extends Abstract_service {


    function get($id = 0) {
	if ($id == 0) {

	    // Lookup domain and get the corresponding website
	    $result = $this->doctrine->em->createQuery("SELECT w FROM entities\Website w JOIN w.domains d WHERE d.name = '" . str_replace("www.","",$_SERVER["HTTP_HOST"]) . "' ")->getResult();
	    if(count($result)==0) {
                throw new Exception("No website found.");
            }
            $website = $result[0];
	}
	else {

	    // Get website from ID
	    $website = $this->doctrine->em->find('entities\Website', $id);
	}

	return $website;
    }

}