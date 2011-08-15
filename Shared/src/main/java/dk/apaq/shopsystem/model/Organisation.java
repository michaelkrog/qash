package dk.apaq.shopsystem.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author michaelzachariassenkrog
 */
public class Organisation extends AbstractEntity {

    private List<Account> users = new ArrayList<Account>();

    private List<Product> products = new ArrayList<Product>();

    /**
     * @OneToMany(targetEntity="Category", mappedBy="organisation")
     */
    private List<Category> categories = new ArrayList<Category>();

    public String name;

    private String company_reg;

    private String address;

    private String zip;
    
    private String city;

    private String country = "US";
    
    private String email;
    
    private String telephone;

    private long initialOrdernumber=1;

    private long initialInvoiceNumber=1;

    private int paymentPeriodInDays=8;

    private String currency = "USD";

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getInitialInvoiceNumber() {
        return initialInvoiceNumber;
    }

    public void setInitialInvoiceNumber(long initialInvoiceNumber) {
        this.initialInvoiceNumber = initialInvoiceNumber;
    }

    public long getInitialOrdernumber() {
        return initialOrdernumber;
    }

    public void setInitialOrdernumber(long initialOrdernumber) {
        this.initialOrdernumber = initialOrdernumber;
    }

    public int getPaymentPeriodInDays() {
        return paymentPeriodInDays;
    }

    public void setPaymentPeriodInDays(int paymentPeriodInDays) {
        this.paymentPeriodInDays = paymentPeriodInDays;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany_reg() {
        return company_reg;
    }

    public void setCompany_reg(String company_reg) {
        this.company_reg = company_reg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }



    public List<Account> getUsers() {
        return users;
    }

    public void setUsers(List<Account> users) {
        this.users = users;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }


}
