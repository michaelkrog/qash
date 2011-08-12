<?php
namespace Filter\Core;
require_once str_replace('//','/',dirname(__FILE__).'/') . 'AbstractFilterComponent.php';


/**
 * Description of CompareFilter
 *
 * @author michael
 */
class CompareFilter extends AbstractFilterComponent {

    private $compareType;
    private $value;

    function __construct($propertyId, $value, $compareType) {
        parent::__construct($propertyId);
        $this->value = $value;
        $this->compareType = $compareType;
    }

    public function get_value() {
        return $this->value;
    }

    public function get_compare_type() {
        return $this->compareType;
    }

    
}

?>
