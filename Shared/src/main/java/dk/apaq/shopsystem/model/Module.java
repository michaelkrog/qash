package dk.apaq.shopsystem.model;

import javax.persistence.Entity;

/**
 *
 * @author michaelzachariassenkrog
 */
@Entity
public class Module extends AbstractOrganisationEntity {

    private String name;
    private String description;
    private String body;
    private String css;
    private String js;
    private double price;
    //private ?? section;


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJs() {
        return js;
    }

    public void setJs(String js) {
        this.js = js;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


}
