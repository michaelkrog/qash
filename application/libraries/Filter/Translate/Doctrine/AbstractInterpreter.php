<?php
namespace Filter\Translate\Doctrine;

require_once str_replace('//','/',dirname(__FILE__).'/') . 'Interpreter.php';

use Filter\Translate\Doctrine\Interpreter;

/**
 * Description of AbstractInterpreter
 *
 * @author michael
 */
abstract class AbstractInterpreter implements Interpreter {
    
    private static $IDCOUNTER=0;

    protected function getParamId() {
        $this->IDCOUNTER++;
        return $this->IDCOUNTER;
    }

    protected function getParamName() {
        return "param_" . $this->getParamId();
    }
    
    //abstract function canInterpret($filter);
    //abstract function interpret($clause, $filter);
}

?>
