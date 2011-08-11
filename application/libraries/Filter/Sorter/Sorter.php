<?php

namespace Filter\Sorter;
/**
 * Description of Sorter
 *
 * @author michael
 */
class Sorter {
    private $elements;

    /**
     * Creates a new instance with the specified elements.
     * @param elements The sorterentires for this sorter.
     */
    function __constructor($elements) {
        $this->elements = $elements;
    }

    
    /**
     * Creates a new sorter that sorts on a single property. The sorting
     * direction will be ascending.
     * @param propertyId The propertyId to use for sorting.
     */
    function __constructor($propertyId) {
        $elements = array(new SorterEntry($propertyId, SortDirection::Ascending));
    }

    /**
     * Creates a new sorter that sorts on a single property.
     * @param propertyId The propertyId to use for sorting.
     * @param direction The direction for the sorting.
     */
    function __constructor($propertyId, $direction) {
        $elements = array(new SorterEntry($propertyId, direction));
    }

    /**
     * Retrieve Sorter entries.
     * @return The sorter entires.
     */
    public function get_sorter_entries() {
        return $elements;
    }

    
}

?>
