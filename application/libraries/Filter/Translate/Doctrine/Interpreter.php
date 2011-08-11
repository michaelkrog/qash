<?php

namespace Filter\Translate\Doctrine;

/**
 *
 * @author michael
 */
interface Interpreter {
    function canInterpret(Filter $filter);
    function interpret(WhereClause $clause, Filter $filter);
}

?>
