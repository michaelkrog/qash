<?php
namespace entities;

/**
 * @Entity
 **/
class Module {

    //...
    /**
    * @Id @GeneratedValue @Column(type="integer")
    * @var integer
    */
    private $id = 0;
    //...

    /**
    * @ManyToOne(targetEntity="Website", inversedBy="modules")
    */
    private $website = 0;

    /**
    * @Column(type="string")
    * @var string
    */
    private $hash = "";

    /**
    * @Column(type="integer")
    * @var integer
    * @actAs:Timestampable
    */
    private $created = 0;

    /**
    * @Column(type="string")
    * @var string
    */
    private $name = "";

    /**
    * @Column(type="string")
    * @var string
    */
    private $description = "";

    /**
    * @Column(type="string", length=3000)
    * @var string
    */
    private $body = "";

    /**
    * @Column(type="string", length=3000)
    * @var string
    */
    private $css = "";

    /**
    * @Column(type="string", length=3000)
    * @var string
    */
    private $js = "";

    /**
    * @Column(type="decimal", precision=2)
    * @var integer
    */
    private $price = 0;

    /**
    * @Column(type="integer")
    * @var integer
    */
    private $section = 0;





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

    public function get_name() {
	return $this->name;
    }

    public function set_name($name) {
	$this->name = $name;
    }

    public function get_body() {
	return $this->body;
    }

    public function set_body($body) {
	$this->body = $body;
    }

    public function get_css() {
	return $this->css;
    }

    public function set_css($css) {
	$this->css = $css;
    }

    public function get_js() {
	return $this->js;
    }

    public function set_js($js) {
	$this->js = $js;
    }

    public function get_price() {
	return $this->price;
    }

    public function set_price($price) {
	$this->price = $price;
    }

    public function get_section() {
	return $this->section;
    }

    public function set_section($section) {
	$this->section = $section;
    }

}
