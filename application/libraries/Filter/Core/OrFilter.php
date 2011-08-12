<?php
namespace Filter\Core;
require_once str_replace('//','/',dirname(__FILE__).'/') . 'AbstractFilterContainer.php';

/**
 * Description of AndFilter
 *
 * @author michael
 */
class OrFilter extends AbstractFilterContainer {

    

    function __construct($filters) {
        parent::__construct($filters);
    }

}

?>
