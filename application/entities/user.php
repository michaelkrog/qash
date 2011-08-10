<?php
namespace entities;

/**
 * @Entity
 **/
class User {
    
    /**
     * @Id @GeneratedValue @Column(type="integer")
     * @var integer
     */
    private $id = 0;

    /**
    * @ManyToOne(targetEntity="Organisation", inversedBy="users")
    */
    private $organisation = 0;

    /**
    * @ManyToOne(targetEntity="User_group", inversedBy="users")
    */
    private $user_group = 0;

    /**
    * @ManyToOne(targetEntity="Country", inversedBy="users")
    */
    private $country = 1;

    /**
    * @Column(type="integer")
    * @var integer
    * @actAs:Timestampable
    */
    public $created = 0;

    /**
    * @Column(type="string")
    * @var string
    */
    public $company = "";

    /**
    * @Column(type="string")
    * @var string
    */
    public $company_reg = "";

    /**
    * @Column(type="string")
    * @var string
    */
    public $name = "";

    /**
    * @Column(type="string")
    * @var string
    */
    public $address = "";

    /**
    * @Column(type="string")
    * @var string
    */
    public $address2 = "";

    /**
    * @Column(type="string")
    * @var string
    */
    public $zip = "";

    /**
    * @Column(type="string")
    * @var string
    */
    public $city = "";
    
    /**
    * @Column(type="string")
    * @var string
    */
    public $email = "";

    /**
    * @Column(type="string")
    * @var string
    */
    public $telephone = "";

    /**
    * @Column(type="string")
    * @var string
    */
    public $website_url = "";

    /**
    * @Column(type="integer")
    * @var integer
    */
    public $newsletter = 1;



    
    public function __construct()
    {
        //$this->user = new ArrayCollection();
    }

    public function get_id() {
	return $this->id;
    }

    public function set_id($id) {
	$this->id = $id;
    }

    public function get_organisation() {
	return $this->organisation;
    }

    public function set_organisation($organisation) {
	$this->organisation = $organisation;
    }

    public function get_user_group() {
	return $this->user_group;
    }

    public function set_user_group($user_group) {
	$this->user_group = $user_group;
    }    

    public function get_created() {
	return $this->created;
    }

    public function set_created($created) {
	$this->created = $created;
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

    public function get_name() {
	return $this->name;
    }

    public function set_name($name) {
	$this->name = $name;
    }

    public function get_address() {
	return $this->address;
    }

    public function set_address($address) {
	$this->address = $address;
    }

    public function get_address2() {
	return $this->address2;
    }

    public function set_address2($address2) {
	$this->address2 = $address2;
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

    public function get_country() {
	return $this->country;
    }

    public function set_country($country) {
	$this->country = $country;
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

    public function get_website_url() {
	return $this->website_url;
    }

    public function set_website_url($website_url) {
	$this->website_url = $website_url;
    }

    public function get_newsletter() {
	return $this->newsletter;
    }

    public function set_newsletter($newsletter) {
	$this->newsletter = $newsletter;
    }

}