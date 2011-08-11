<?php

namespace Filter\Translate\Doctrine;



/**
 * Description of CompareFilterInterpreter
 *
 * @author michael
 */
class CompareFilterInterpreter extends AbstractInterpreter {

    public function canInterpret($filter) {
        return ($filter instanceof \Filter\Core\CompareFilter);
    }
    
    public function interpret(WhereClause $clause, CompareFilter $filter) {
        $paramName = getParamName();
        $operator = null;

        switch ($filter->get_compare_type()) {
            case \Filter\Core\CompareType::Equals:
                $operator = "=";
                break;
            case \Filter\Core\CompareType::GreaterOrEqual:
                $operator = ">=";
                break;
            case \Filter\Core\CompareType::GreaterThan:
                $operator = ">";
                break;
            case \Filter\Core\CompareType::LessOrEqual:
                $operator = "<=";
                break;
            case \Filter\Core\CompareType::LessThan:
                $operator = "<";
                break;
        }

        $ql = "e." . $filter->get_property_id() . $operator . ":" . $paramName;
        $clause->append_statement($ql);
        $clause->append_parameter($paramName, $filter->get_value());
        return true;
    }
}

?>
