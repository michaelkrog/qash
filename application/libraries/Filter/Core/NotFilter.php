<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
namespace Filter\Core;
/**
 * Description of NotFilter
 *
 * @author michael
 */
class NotFilter implements Filter {
    private $filter;

    function __constructor(Filter $element) {
        $this->filter = $element;
    }
    
    public function get_filter_element() {
        return $filter;
    }
}

?>
