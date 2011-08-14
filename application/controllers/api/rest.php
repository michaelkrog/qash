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
    }

}
