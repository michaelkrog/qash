<?php
namespace entities;

/**
 * @Entity
 **/
class User_group {

    /**
    * @Id @GeneratedValue @Column(type="integer")
    * @var integer
    */
    private $id = 0;

    /**
    * @ManyToOne(targetEntity="Website", inversedBy="user_groups")
    */
    private $website = 0;

    /**
    * @OneToMany(targetEntity="User", mappedBy="user_group")
    */
    private $users = array();

    /**
    * @Column(type="datetime")
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

    public function get_is_default() {
	return $this->is_default;
    }

    public function set_is_default($is_default) {
	$this->is_default = $is_default;
    }

}

