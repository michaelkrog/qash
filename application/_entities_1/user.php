<?php
namespace entities;

/**
 * @Entity
 **/

class User {
    
    /**
     * @Id @GeneratedValue @Column(type="integer")
     * @var string
     */
    private $id = null;

     /**
     * @Column(type="string")
     * @var string
     */
    private $name = null;

    /**
    * @Column(type="string")
    * @var string
    */
    private $email = null;

    
    private $company = null;


    private $company_reg = null;
    private $address = null;
    private $address2 = null;
    private $zip = null;
    private $city = null;
    private $country = null;
    private $telephone = null;
    private $newsletter = null;
    private $website = null;

    
    public function get_id() {
	return $this->id;
    }

    public function set_id($id) {
	$this->id = $id;
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

    public function get_newsletter() {
	return $this->newsletter;
    }

    public function set_newsletter($newsletter) {
	$this->newsletter = $newsletter;
    }

    public function get_website() {
	return $this->website;
    }

    public function set_website($website) {
	$this->website = $website;
    }
}