<?php

namespace Filter\Translate\Doctrine;

/**
 * Description of WhereClause
 *
 * @author michael
 */
class WhereClause {
    private $statement;
    private $parameters = array();

    public function append_statement(String $ql) {
        $statement = $statement + $ql;
    }

    public function insert_statement($index, String $ql) {
        $tmp = substr($statement, 0, $index) + $ql + substr($statement, $index);
        $statement = $tmp;
    }

    public function append_parameter(String $parameter, $value) {
        $parameters[$parameter] = $value;
    }

    public function get_statement_length() {
        return strlen($this->statement);
    }


    public function get_statement() {
        return $statement;
    }

    public function get_parameters() {
        return $parameters;
    }
}

?>
