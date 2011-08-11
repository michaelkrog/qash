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
     * Creates a Limit instance. Defaults offset to 0.
     */
    function __construct($count) {
        $this->offset = 0;
        $this->count = $count;
        
        this(0, $count);
    }

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
        return $offset;
    }

    /**
     * Retrieves the count.
     * @return The count.
     */
    public function get_count() {
        return $count;
    }

}

?>
