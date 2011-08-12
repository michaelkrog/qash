<?php
namespace Filter\Core;
require_once str_replace('//','/',dirname(__FILE__).'/') . '../FilterContainer.php';

use Filter\FilterContainer;
/**
 * Description of AbstractFilterContainer
 *
 * @author michael
 */
class AbstractFilterContainer implements FilterContainer {
    private $filters;

    function __constructor() {
        $this->filters = array();
    }

    function __constructor($filters) {
        $this->filters = $filters;
    }

    public function add_filter(Filter $filter) {
        array_push($filters, $filter);
    }

    public function remove_filter(Filter $filter) {
       // array_
    }

    public function get_filters() {
        return $filters;
    }

}

?>
