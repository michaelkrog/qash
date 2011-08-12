<?php
namespace Filter\Sorter;
/**
 * Description of SorterEntry
 *
 * @author michael
 */
class SorterEntry {
    private $propertyId;
    private $direction;

    /**
     * Creates a new SorterEntry.
     * @param propertyId The id for sorting.
     * @param direction The direction of sorting.
     */
    function __construct($propertyId, $direction) {
        /*if(propertyId == null) {
            throw new IllegalArgumentException("propertyid cannot be null.");
        }

        if(direction == null) {
            throw new IllegalArgumentException("direction cannot be null.");
        }*/

        $this->propertyId = $propertyId;
        $this->direction = $direction;
    }

    /**
     * Retrieves the direction.
     * @return The direction
     */
    public function get_direction() {
        return $direction;
    }

    /**
     * Retrieves the propertyid to use for sorting.
     * @return The propertyid.
     */
    public function get_property_id() {
        return $propertyId;
    }

}

?>
