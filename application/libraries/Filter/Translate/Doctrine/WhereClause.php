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

    public function append_statement($ql) {
        $this->statement = $this->statement . $ql;
    }

    public function insert_statement($index, $ql) {
        $tmp = substr($this->statement, 0, $index) + $ql + substr($this->statement, $index);
        $this->statement = $tmp;
    }

    public function append_parameter($parameter, $value) {
        $this->parameters[$parameter] = $value;
    }

    public function get_statement_length() {
        return strlen($this->statement);
    }


    public function get_statement() {
        return $this->statement;
    }

    public function get_parameters() {
        return $this->parameters;
    }
}

?>
