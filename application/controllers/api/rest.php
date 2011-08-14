<?php

/*
  |--------------------------------------------------------------------------
  | Api
  |--------------------------------------------------------------------------
  |
  | An api serving website as json
  |
 */

class Rest extends CI_Controller {

    function Rest() {
        parent::__construct();
    }

    function organisations($id = null) {
        $this->load->model("service/Organisation_service");

        $method = $_SERVER['REQUEST_METHOD'];

        if ("GET" === $method) {
            if (is_null($id)) {
                //List organisations
                $ids = $this->Organisation_service->listIds();

                echo(json_encode($ids));
            } else {
                //Get specific organisation
                echo(json_encode($this->Organisation_service->read($id)));
            }
        }

        if ("POST" === $method) {
            if(!is_null($id)) {
                throw new InvalidArgumentException("Cannot post to a specific id. POST will create a new instance.");
            }

            echo(json_encode($this->Organisation_service->create()));
        }

        if ("PUT" === $method) {
            if(is_null($id)) {
                throw new InvalidArgumentException("Can only put to a specific id. PUT will update existing instance.");
            }

            // decode to instance object somehow
            $this->Organisation_service->update($entity);
        }

        if ("DELETE" === $method) {
            if(is_null($id)) {
                throw new InvalidArgumentException("Can only delete a specific id.");
            }

            $this->Organisation_service->delete($id);
        }
    }

}
