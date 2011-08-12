<?php

namespace Filter\Translate\Doctrine;

/**
 *
 * @author michael
 */
interface Interpreter {
    function canInterpret($filter);
    function interpret($clause, $filter);
}

?>
