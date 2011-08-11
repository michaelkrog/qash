<?php

require(APPPATH . '/libraries/REST_Controller.php');
/*
  |--------------------------------------------------------------------------
  | Api
  |--------------------------------------------------------------------------
  |
  | An api serving website as json
  |
 */

class Example extends REST_Controller {

    function Example() {
        parent::__construct();
        //$this->load->library('REST_Controller');
    }

    function organisation_get() {
        $this->load->model("service/Organisation_service");

        $id =$this->get('id');
        //echo "id: " . $id;

        if (!$id) {
            $this->response(NULL, 400);
        }

        $org = $this->Organisation_service->get($id);

        if ($org) {
            $this->response($org, 200); // 200 being the HTTP response code
        } else {
            $this->response(NULL, 404);
        }
    }

}
