<?php
require_once '../../../application/libraries/Filter/Core/CompareFilter.php';
require_once '../../../application/libraries/Filter/Core/CompareType.php';
require_once '../../../application/libraries/Filter/Sorter/Sorter.php';
require_once '../../../application/libraries/Filter/Limit/Limit.php';
require_once '../../../application/libraries/Filter/Translate/Doctrine/Translator.php';
require_once '../../../application/libraries/doctrine.php';

use Filter\Core\CompareFilter,
 Filter\Sorter\Sorter,
 Filter\Limit\Limit,
 Filter\Translate\Doctrine\Translator,
 Filter\Core\CompareType;       


class TranslatorTest extends PHPUnit_Framework_TestCase
{
    public function testTranslate()
    {
        define('BASEPATH', '../../../system/');
        define('APPPATH', '../../../application/');
        $doctrine = new Doctrine();
        $em = $doctrine->em;
        $filter = new CompareFilter("name", "test", CompareType::Equals);
        $sorter = new Sorter("name");
        $limit = new Limit(0,10);
        $translator = new Translator();
        $translator->translate($em, "entities\Test", $filter, $sorter, $limit);
        
        //$this->assertEquals(0, $filter);

    }
 
   
}
?>