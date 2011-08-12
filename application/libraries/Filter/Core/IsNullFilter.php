<?php
namespace Filter\Core;
require_once str_replace('//','/',dirname(__FILE__).'/') . 'AbstractFilterComponent.php';

/**
 * Description of IsNullFilter
 *
 * @author michael
 */
class IsNullFilter extends AbstractFilterComponent {
    function __construct(String $propertyId) {
        parent::__construct($propertyId);
    }
}

?>
