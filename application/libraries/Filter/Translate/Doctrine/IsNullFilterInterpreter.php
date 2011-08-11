<?php

namespace Filter\Translate\Doctrine;

/**
 * Description of IsNullFilterInterpreter
 *
 * @author michael
 */
class IsNullFilterInterpreter extends AbstractInterpreter {
    public function interpret(WhereClause $clause, IsNullFilter $filter) {
        $clause.appendStatement("e." . $filter.get_property_id() + " IS NULL");
        return true;
    }
}

?>
