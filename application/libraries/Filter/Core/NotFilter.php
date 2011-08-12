<?php
namespace Filter\Core;
require_once str_replace('//','/',dirname(__FILE__).'/') . 'AbstractFilterComponent.php';

/**
 * Description of NotFilter
 *
 * @author michael
 */
class NotFilter implements Filter {
    private $filter;

    function __construct($element) {
        $this->filter = $element;
    }
    
    public function get_filter_element() {
        return $filter;
    }
}

?>
