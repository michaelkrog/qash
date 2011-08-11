<?php
namespace Filter\Core;

/**
 * Description of CompareFilter
 *
 * @author michael
 */
class CompareFilter extends AbstractFilterComponent {

    private $compareType;
    private $value;

    function __constructor(String $propertyId, $value, $compareType) {
        parent::__construct($propertyId);
        $this->value = $value;
        $this->compareType = $compareType;
    }

    public function get_value() {
        return $value;
    }

    public function get_compare_type() {
        return $compareType;
    }

    
}

?>
