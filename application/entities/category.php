<?php
namespace entities;

/**
 * @Entity
 **/
class Category {

    /**
     * @Id @GeneratedValue @Column(type="integer")
     * @var integer
     */
    private $id = 0;

    /**
    * @ManyToOne(targetEntity="Website", inversedBy="categories")
    */
    private $website = 0;

    /**
    * @OneToMany(targetEntity="Product", mappedBy="category")
    */
    private $products = array();


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

    public function get_description() {
	return $this->description;
    }

    public function set_description($description) {
	$this->description = $description;
    }


}