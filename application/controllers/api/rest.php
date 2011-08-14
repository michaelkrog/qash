<?php
require_once str_replace('//','/',dirname(__FILE__).'/') . '../../libraries/Filter/Core/CompareFilter.php';
require_once str_replace('//','/',dirname(__FILE__).'/') . '../../libraries/Filter/Core/CompareType.php';

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
                //check filter
                $filter = isset($_GET['filter']) ? $_GET['filter'] : null;
                $count = isset($_GET['count']) ? $_GET['count'] : null;
                $offset = isset($_GET['offset']) ? $_GET['offset'] : null;
                $orderBy = isset($_GET['orderBy']) ? $_GET['orderBy'] : null;
                
                $limit = null;
                
                if(!is_null($filter)) {
                    $filter = new Filter\Core\CompareFilter("name", $filter, \Filter\Core\CompareType::Equals);
                }
                
                if(!is_null($orderBy)) {
                    $orderBy = new \Filter\Sorter\Sorter($orderBy);
                }
                
                if(!is_null($offset) || !is_null($count)) {
                    $offset = 0;
                    if(!is_null($offset)) {
                        $offset = int($offset);
                    }
                    
                    $count = 10000;
                    if(!is_null($count)) {
                        $count = int($count);
                    }
                    
                    $limit = new Filter\Limit\Limit($offset, $count);
                }

                //List organisations
                $ids = $this->Organisation_service->listIds($filter, $orderBy, $limit);

                echo(json_encode($ids));
            } else {
                //Get specific organisation
                echo($this->Organisation_service->read($id)->encodeJSON());
            }
        }

        if ("POST" === $method) {
            if(!is_null($id)) {
                header('HTTP/1.0 405');
                echo("Cannot post to a specific id. POST will create a new instance.");
                return;
            }

            echo(json_encode($this->Organisation_service->create()));
        }

        if ("PUT" === $method) {
            if(is_null($id)) {
                header('HTTP/1.0 405');
                echo("Can only put to a specific id. PUT will update existing instance.");
                return;
            }

            // decode to instance object somehow
            $entity = $this->Organisation_service->read($id);
            $json = file_get_contents("php://input");
            $entity->decodeJSON($json);
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
