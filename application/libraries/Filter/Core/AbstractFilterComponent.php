<?php
namespace Filter\Core;
/**
 * Description of AbstractFilterComponent
 *
 * @author michael
 */
class AbstractFilterComponent {
    private $propertyId;

    function __constructor(String $propertyId) {
        $this->propertyId = $propertyId;
    }

    public function get_property_id() {
        return $propertyId;
    }
}

?>
