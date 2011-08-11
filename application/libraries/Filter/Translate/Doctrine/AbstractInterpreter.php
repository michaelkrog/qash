<?php

namespace Filter\Translate\Doctrine;

use Filter\Translate\Doctrine\Interpreter;

/**
 * Description of AbstractInterpreter
 *
 * @author michael
 */
class AbstractInterpreter implements Interpreter {
    
    private static $IDCOUNTER=0;

    protected function getParamId() {
        $IDCOUNTER++;
        return $IDCOUNTER;
    }

    protected function getParamName() {
        return "param_" + getParamId();
    }
}

?>
