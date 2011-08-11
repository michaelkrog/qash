<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Translator
 *
 * @author michael
 */
class Translator {

    private static $singleton;
    private $interpreters = array();

    public static function get_singleton() {
        if (is_null($singleton)) {
            $singleton = new Translator();
        }
    }

    private function __constructor() {
        //array_push($interpreters, );
    }

    public function register_interpreter(Interpreter $interpreter) {
        //interpreterMap.put(clazz, interpreter);
    }

    public function translate(EntityManager $em, String $entityClass, Filter $filter, Sorter $sorter, Limit $limit) {
        return translate(entityManager, null, entityClass, filter, sorter, limit);
    }

    public function translate(EntityManager $em, $fields, String $entityClass, Filter $filter, Sorter $sorter, Limit $limit) {

        $whereClause = new WhereClause();
        $clauseAppended = buildWhereClause($whereClause, $filter);

        $ql = "select ";
        if (is_null($fields == null) || count($fields) == 0) {
            $ql = $ql . "e";
        } else {
            $firstField = true;
            foreach ($fields as $field) {
                if (!$firstField) {
                    $ql = $ql . ", ";
                }
                $ql = $ql . "e.";
                $ql = $ql . $field;
                $firstField = false;
            }
        }

        $ql = $ql . " from ";
        $ql = $ql . $entityClass;
        $ql = $ql . " e";

        if ($clauseAppended) {
            $ql = $ql . " WHERE ";
            $ql = $ql . $whereClause->get_statement();
        }

        if (!is_null($sorter) && count($sorter->get_sorter_entries) <> 0) {
            $firstOrderClause = true;
            foreach ($sorter->get_sorter_entries() as $entry) {
                if ($firstOrderClause) {
                    $ql = $ql . " ORDER BY ";
                } else {
                    $ql = $ql . ", ";
                }
                $ql = $ql . $entry->get_property_id();

                $direction = " DESC";
                if ($entry->get_direction() == SortDirection::Ascending) {
                    $direction = " ASC";
                }
                $ql = $ql . $direction;
                $firstOrderClause = false;
            }
        }

        $q = $em->createQuery($ql);

        foreach (array_keys($whereClause->get_parameters()) as $key) {
            $params = $whereClause->get_parameters();
            $q->setParameter($key, $params[$key]);
        }

        if (!is_null($limit)) {
            $q->set_first_result($limit->get_offset());
            $q->set_max_results($limit->get_count());
        }

        return $q;
    }

    private function buildWhereClause(WhereClause $clause, Filter $filter) {

        if (is_null($filter)) {
            return false;
        }

        //First try an interpreter
        foreach ($interpreters as $interpreter) {
            if ($interpreter->canInterpret($filter)) {
                return $interpreter->interpret($clause, $filter);
            }
        }


        //..else try our special handlers for filtercontainers.
        if ($filter instanceof AndFilter) {
            $clause->appendStatement("(");

            $hasAddedFilter = false;
            foreach ($filter->get_filters() as $currentFilter) {
                $startIndex = $clause->get_statement_length();

                $statementAppended = buildWhereClause($clause, $currentFilter);

                if ($statementAppended) {
                    if ($hasAddedFilter) {
                        $clause->insert_statement($startIndex, " AND ");
                    }
                    $hasAddedFilter = true;
                }
            }

            $clause->append_statement(")");

            return $hasAddedFilter;
        }

        if ($filter instanceof OrFilter) {
            $hasAddedFilter = false;

            $clause->append_statement("(");

            foreach ($filter->get_filters() as $currentFilter) {
                $startIndex = $clause->get_statement_length();

                $statementAppended = buildWhereClause($clause, $currentFilter);

                if ($statementAppended) {
                    if ($hasAddedFilter) {
                        $clause->insert_statement($startIndex, " OR ");
                    }
                    $hasAddedFilter = true;
                }
            }

            $clause->appendStatement(")");

            return $hasAddedFilter;
        }

        if ($filter instanceof NotFilter) {

            //Special care of IsNull filter becauseof the special "IS [NOT] NULL" syntax
            if ($filter->get_filter_element() instanceof IsNullFilter) {
                $propId = $filter->get_property_id();
                $clause->append_statement("e." . $propId . " IS NOT NULL");
                return true;
            }

            $startIndex = $clause->get_statement_length();
            $statementAppended = buildWhereClause($clause, $filter->get_filter_element());
            if ($statementAppended) {
                $clause->insert_statement($startIndex, "NOT (");
                $clause->append_statement(")");
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

}

?>
