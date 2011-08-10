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
    * @OneToMany(targetEntity="User", mappedBy="website")
    */
    private $users = array();

    /**
    * @OneToMany(targetEntity="User_group", mappedBy="website")
    */
    private $user_groups = array();

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
    * @OneToMany(targetEntity="Product", mappedBy="website")
    */
    private $products = array();

    /**
    * @OneToMany(targetEntity="Category", mappedBy="website")
    */
    private $categories = array();

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
    public $name = "";

    /**
    * @Column(type="string")
    * @var string
    */
    private $company = "";

    /**
    * @Column(type="string")
    * @var string
    */
    private $company_reg = "";

    /**
    * @Column(type="string")
    * @var string
    */
    public $address = "";

    /**
    * @Column(type="string")
    * @var string
    */
    private $zip = "";

    /**
    * @Column(type="string")
    * @var string
    */
    private $city = "";

    /**
    * @Column(type="string")
    * @var string
    */
    private $email = "";

    /**
    * @Column(type="string")
    * @var string
    */
    private $telephone = "";

    /**
    * @Column(type="string")
    * @var string
    */
    private $tracking_code = "";

    /**
    * @Column(type="string")
    * @var string
    */
    private $tracking_code_invoice = "";

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

    public function get_users() {
	return $this->users;
    }

    public function set_users($users) {
	$this->users = $users;
    }

    public function get_user_groups() {
	return $this->user_groups;
    }

    public function set_user_groups($user_groups) {
	$this->user_groups = $user_groups;
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

    public function get_products() {
	return $this->products;
    }

    public function set_products($products) {
	$this->products = $products;
    }

    public function get_categories() {
	return $this->categories;
    }

    public function set_categories($categories) {
	$this->categories = $categories;
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

    public function get_company() {
	return $this->company;
    }

    public function set_company($company) {
	$this->company = $company;
    }

    public function get_company_reg() {
	return $this->company_reg;
    }

    public function set_company_reg($company_reg) {
	$this->company_reg = $company_reg;
    }

    public function get_address() {
	return $this->address;
    }

    public function set_address($address) {
	$this->address = $address;
    }

    public function get_zip() {
	return $this->zip;
    }

    public function set_zip($zip) {
	$this->zip = $zip;
    }

    public function get_city() {
	return $this->city;
    }

    public function set_city($city) {
	$this->city = $city;
    }

    public function get_email() {
	return $this->email;
    }

    public function set_email($email) {
	$this->email = $email;
    }

    public function get_telephone() {
	return $this->telephone;
    }

    public function set_telephone($telephone) {
	$this->telephone = $telephone;
    }

    public function get_tracking_code() {
	return $this->tracking_code;
    }

    public function set_tracking_code($tracking_code) {
	$this->tracking_code = $tracking_code;
    }

    public function get_tracking_code_invoice() {
	return $this->tracking_code_invoice;
    }

    public function set_tracking_code_invoice($tracking_code_invoice) {
	$this->tracking_code_invoice = $tracking_code_invoice;
    }

    public function get_template() {
	return $this->template;
    }

    public function set_template($template) {
	$this->template = $template;
    }


}
