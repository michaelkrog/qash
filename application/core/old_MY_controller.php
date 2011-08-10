<?php
class MY_Controller extends CI_Controller
{

    //Doctrine EntityManager
    //public $em;

    function __construct()
    {
        parent::__construct();

        //Not required if you autoload the library
        //$this->load->library('doctrine');
 
        //Instantiate a Doctrine Entity Manager
        //$this->em = $this->doctrine->em;
    }
}