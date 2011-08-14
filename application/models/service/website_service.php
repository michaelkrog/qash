<?php
require_once str_replace('//','/',dirname(__FILE__).'/') . 'AbstractCrud.php';

use entities\Website;

class Website_service extends AbstractCrud {

    public function __construct() {
        parent::__construct("entities\Website");
    }

    public function create() {
        $entity = new entities\Website();
        $entity = $this->update($entity);
        return $entity->get_id();
    }

    function get($id = 0) {
	if ($id == 0) {

	    return $this->getFromDomain(str_replace("www.","",$_SERVER["HTTP_HOST"]));
	}
	else {
            // Get website from ID
	    return $this->read($id);
	}

    }

    function getFromDomain($domain) {
        // Lookup domain and get the corresponding website
        $result = $this->doctrine->em->createQuery("SELECT w FROM entities\Website w JOIN w.domains d WHERE d.name = '" . $domain . "' ")->getResult();
        if(count($result)==0) {
            throw new Exception("No website found.");
        }
        return $result[0];
    }

}