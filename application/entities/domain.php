<?php
namespace entities;

/**
 * @Entity
 **/
class Domain {

    //...
    /**
    * @Id @GeneratedValue @Column(type="integer")
    * @var integer
    */
    private $id = 0;
    //...

    /**
    * @ManyToOne(targetEntity="Website", inversedBy="domains")
    */
    private $website = 0;

    /**
    * @Column(type="string")
    * @var string
    */
    private $created = "";

    /**
    * @Column(type="string")
    * @var string
    */
    private $name = "";

    /**
    * @Column(type="integer")
    * @var integer
    */
    private $is_default = 0;

    /**
    * @Column(type="integer")
    * @var integer
    */
    private $language = 0;

    /**
    * @Column(type="integer")
    * @var integer
    */
    private $currency = 0;




    public function get_id() {
	return $this->id;
    }

    public function set_id($id) {
	$this->id = $id;
    }

    public function get_website() {
	return $this->website;
    }

    public function set_website($website) {
	$this->website = $website;
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

    public function get_primary() {
	return $this->primary;
    }

    public function set_primary($primary) {
	$this->primary = $primary;
    }

    public function get_language() {
	return $this->language;
    }

    public function set_language($language) {
	$this->language = $language;
    }

    public function get_currency() {
	return $this->currency;
    }

    public function set_currency($currency) {
	$this->currency = $currency;
    }


}

    