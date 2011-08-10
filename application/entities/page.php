<?php
namespace entities;

/**
 * @Entity
 **/
class Page {

    //...
    /**
    * @Id @GeneratedValue @Column(type="integer")
    * @var integer
    */
    private $id = 0;
    //...

    /**
    * @ManyToOne(targetEntity="Website", inversedBy="pages")
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
    * @Column(type="string")
    * @var string
    */
    private $name = "";

    /**
    * @Column(type="string")
    * @var string
    */
    private $title = "";

    /**
    * @Column(type="string")
    * @var string
    */
    private $description = "";

    /**
    * @Column(type="string")
    * @var string
    */
    private $keywords = "";




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

    public function get_title() {
	return $this->title;
    }

    public function set_title($title) {
	$this->title = $title;
    }

    public function get_description() {
	return $this->description;
    }

    public function set_description($description) {
	$this->description = $description;
    }

    public function get_keywords() {
	return $this->keywords;
    }

    public function set_keywords($keywords) {
	$this->keywords = $keywords;
    }

}