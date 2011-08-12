<?php

namespace Filter\Translate\Doctrine;

/**
 * Description of IsNullFilterInterpreter
 *
 * @author michael
 */
class IsNullFilterInterpreter extends AbstractInterpreter {
    public function canInterpret($filter) {
        return ($filter instanceof Filter\Core\IsNullFilter);
    }
    public function interpret($clause, $filter) {
        $clause.appendStatement("e." . $filter.get_property_id() + " IS NULL");
        return true;
    }
}

?>
