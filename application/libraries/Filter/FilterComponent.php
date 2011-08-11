<?php

namespace Filter;
/**
 *
 * @author michael
 */
interface FilterComponent {
    /**
     * Retrieves the property which the Filter works on.
     * @return The propertyId.
     */
    public function get_property_id();
}

?>
