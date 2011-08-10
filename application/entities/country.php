<?php
namespace entities;

/**
 * @Entity
 **/
class Country {

    //...
    /**
    * @Id @GeneratedValue @Column(type="integer")
    * @var integer
    */
    private $id = 0;
    //...

    /**
    * @OneToMany(targetEntity="User", mappedBy="country")
    */
    private $users = array();

    /**
    * @Column(type="string")
    * @var string
    */
    private $name = "";



    public function get_id() {
	return $this->id;
    }

    public function set_id($id) {
	$this->id = $id;
    }

    public function get_name() {
	return $this->name;
    }

    public function set_name($name) {
	$this->name = $name;
    }

}
