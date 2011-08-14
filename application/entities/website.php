<?php
namespace entities;

/**
 * @Entity
 **/
class Website {

    //...
    /**
    * @Id @GeneratedValue @Column(type="integer")
    * @var integer
    */
    private $id = 0;
    //...

    /**
    * @OneToMany(targetEntity="Domain", mappedBy="website")
    */
    private $domains = array();
    
    /**
    * @OneToMany(targetEntity="Template", mappedBy="website")
    */
    private $templates = array();

    /**
    * @OneToMany(targetEntity="Module", mappedBy="website")
    */
    private $modules = array();

    /**
    * @OneToMany(targetEntity="Page", mappedBy="website")
    */
    private $pages = array();

    /**
    * @ManyToOne(targetEntity="Organisation", inversedBy="website")
    */
    private $organisation = 0;
    
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
    private $template = 0;



    
    public function get_id() {
	return $this->id;
    }

    public function set_id($id) {
	$this->id = $id;
    }

    public function get_domains() {
	return $this->domains;
    }

    public function set_domains($domains) {
	$this->domains = $domains;
    }

    public function get_templates() {
	return $this->templates;
    }

    public function set_templates($templates) {
	$this->templates = $templates;
    }

    public function get_modules() {
	return $this->modules;
    }

    public function set_modules($modules) {
	$this->modules = $modules;
    }

    public function get_pages() {
	return $this->pages;
    }

    public function set_pages($pages) {
	$this->pages = $pages;
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
    
    public function get_template() {
	return $this->template;
    }

    public function set_template($template) {
	$this->template = $template;
    }

    public function get_organisation() {
	return $this->organisation;
    }

    public function set_organisation($organisation) {
	$this->organisation = $organisation;
    }


}
