<?php
namespace Filter;
/**
 * Interface for a Filter that contains subfilters. This is used in filter like
 * AndFilter, OrFilter etc. which generates its result based on other filters result.
 */

interface FilterContainer {
    public function add_filter(Filter $filter);
    public function remove_filter(Filter $filter);
    public function get_filters();
}

?>
