package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 * Defines a Website.
 */
@Entity
public class Website extends AbstractContentEntity implements Serializable {

    @OneToMany
    private List<Domain> domains = new ArrayList<Domain>();
    
    /*@OneToMany
    private List<Template> templates = new ArrayList();
    @OneToMany
    private List<Module> modules = new ArrayList<Module>();*/
    
    @OneToMany
    private List<Page> pages = new ArrayList<Page>();
    private String name;
    private String tracking_code;
    private String tracking_code_invoice;

    public List<Domain> getDomains() {
        return domains;
    }

    public void setDomains(List<Domain> domains) {
        this.domains = domains;
    }

    /*public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    /*public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }*/

    public String getTracking_code() {
        return tracking_code;
    }

    public void setTracking_code(String tracking_code) {
        this.tracking_code = tracking_code;
    }

    public String getTracking_code_invoice() {
        return tracking_code_invoice;
    }

    public void setTracking_code_invoice(String tracking_code_invoice) {
        this.tracking_code_invoice = tracking_code_invoice;
    }
}
