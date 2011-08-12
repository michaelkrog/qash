<?php
namespace Filter\Core;
require_once str_replace('//','/',dirname(__FILE__).'/') . '../FilterComponent.php';

use Filter\FilterComponent;
/**
 * Description of AbstractFilterComponent
 *
 * @author michael
 */
class AbstractFilterComponent implements FilterComponent {
    private $propertyId;

    function __construct($propertyId) {
        $this->propertyId = $propertyId;
    }

    public function get_property_id() {
        return $this->propertyId;
    }
}

?>
