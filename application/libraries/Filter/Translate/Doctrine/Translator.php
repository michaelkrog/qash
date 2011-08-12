<?php
namespace Filter\Translate\Doctrine;
require_once str_replace('//','/',dirname(__FILE__).'/') . 'WhereClause.php';
require_once str_replace('//','/',dirname(__FILE__).'/') . 'CompareFilterInterpreter.php';
require_once str_replace('//','/',dirname(__FILE__).'/') . 'IsNullFilterInterpreter.php';

use Filter\Translate\Doctrine\WhereClause;
/**
 * Description of Translator
 *
 * @author michael
 */
class Translator {

    private $interpreters = array();

    public function __construct() {
        array_push($this->interpreters, new CompareFilterInterpreter());
        array_push($this->interpreters, new IsNullFilterInterpreter());
    }

    public function register_interpreter(Interpreter $interpreter) {
        array_push($interpreters, $interpreter);
    }

    public function translate($em, $entityClass, $filter,  $sorter,  $limit, $fields = null) {
                
        $whereClause = new WhereClause();
        $clauseAppended = $this->buildWhereClause($whereClause, $filter);

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

        if (!is_null($sorter) && count($sorter->get_sorter_entries()) <> 0) {
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

        echo "Query: " . $ql;
        $q = $em->createQuery($ql);

        foreach (array_keys($whereClause->get_parameters()) as $key) {
            $params = $whereClause->get_parameters();
            $q->setParameter($key, $params[$key]);
        }

        if (!is_null($limit)) {
            $q->setFirstResult($limit->get_offset());
            $q->setMaxResults($limit->get_count());
        }

        return $q;
    }

    private function buildWhereClause($clause, $filter) {

        
        if (is_null($filter)) {
            echo "filter was null\n";
            return false;
        }

        //First try an interpreter
        foreach ($this->interpreters as $interpreter) {
            if ($interpreter->canInterpret($filter)) {
                return $interpreter->interpret($clause, $filter);
            }
        }


        //..else try our special handlers for filtercontainers.
        if ($filter instanceof AndFilter) {
            echo "filter was and\n";
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
