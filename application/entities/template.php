<?php
namespace entities;

/**
 * @Entity
 **/
class Template {

    //...
    /**
    * @Id @GeneratedValue @Column(type="integer")
    * @var integer
    */
    private $id = 0;
    //...

    /**
    * @ManyToOne(targetEntity="Website", inversedBy="templates")
    */
    private $website = 0;

    /**
    * @Column(type="string")
    * @var string
    */
    private $hash = "";

    /**
    * @Column(type="string")
    * @var string
    */
    private $created = "";

    /**
    * @Column(type="integer")
    * @var integer
    */
    private $is_active = 0;

    /**
    * @Column(type="string")
    * @var string
    */
    private $name = "";

    /**
    * @Column(type="string", length=3000)
    * @var string
    */
    private $styles = "";





    public function get_id() {
	return $this->id;
    }

    public function set_id($id) {
	$this->id = $id;
    }

    public function get_users() {
	return $this->users;
    }

    public function set_users($users) {
	$this->users = $users;
    }

    public function get_hash() {
	return $this->hash;
    }

    public function set_hash($hash) {
	$this->hash = $hash;
    }

    public function get_created() {
	return $this->created;
    }

    public function set_created($created) {
	$this->created = $created;
    }

    public function get_is_active() {
	return $this->is_active;
    }

    public function set_is_active($is_active) {
	$this->is_active = $is_active;
    }

    public function get_name() {
	return $this->name;
    }

    public function set_name($name) {
	$this->name = $name;
    }

    public function get_styles() {
	return $this->styles;
    }

    public function set_styles($styles) {
	$this->styles = $styles;
    }


}
