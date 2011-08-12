<?php
namespace Filter\Limit;
/**
 * Description of Limit
 *
 * @author michael
 */
class Limit {
    private $offset;
    private $count;


    /**
     * Creates a Limit instance.
     */
    function __construct($offset, $count) {
        $this->offset = $offset;
        $this->count = $count;
    }

    /**
     * Retrieves the offset.
     * @return The offset.
     */
    public function get_offset() {
        return $this->offset;
    }

    /**
     * Retrieves the count.
     * @return The count.
     */
    public function get_count() {
        return $this->count;
    }

}

?>
