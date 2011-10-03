package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 */
@Entity
public class Page implements Serializable, WebContentEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateChanged = new Date();

    private String name;
    private String title;
    private String description;
    private String keywords;
    private String themeName;
    private String templateName;
    
    @OneToMany(cascade= CascadeType.ALL,fetch= FetchType.EAGER)
    private List<ComponentInformation> placeholderList = new ArrayList<ComponentInformation>();
    
    @ManyToOne
    private Website website;
    
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date date) {
        this.dateCreated = date;
    }

    public Date getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    
    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public void addComponentInformation(ComponentInformation ci) {
        placeholderList.add(ci);
    }
    
    public List<String> getPlaceholderIds() {
        List<String> ids = new ArrayList<String>();
        for(ComponentInformation c : placeholderList) {
            if(!ids.contains(c.getPlaceholderName())) {
                ids.add(c.getPlaceholderName());
            }
        }
        return Collections.unmodifiableList(ids);
    }
    
    public List<ComponentInformation> getComponentInformations(){
        return Collections.unmodifiableList(placeholderList);
    }
    
    public List<ComponentInformation> getComponentInformations(String placeholderId){
        List<ComponentInformation> list = new ArrayList<ComponentInformation>();
        for(ComponentInformation c : placeholderList) {
            if(placeholderId.equals(c.getPlaceholderName())) {
                list.add(c);
            }
        }
        return Collections.unmodifiableList(list);
    }
    
    public void removeComponentInformation(String componentInformationId) {
        Iterator<ComponentInformation> it = placeholderList.iterator();
        while(it.hasNext()) {
            ComponentInformation c = it.next();
            if(componentInformationId.equals(c.getId())) {
                it.remove();
                return;
            }
        }
    }
    
    public void removePlaceholderId(String placeholderId) {
        Iterator<ComponentInformation> it = placeholderList.iterator();
        while(it.hasNext()) {
            ComponentInformation c = it.next();
            if(placeholderId.equals(c.getPlaceholderName())) {
                it.remove();
            }
        }
    }
    
    public void clearPlaceholderInformation() {
        placeholderList.clear();
    }
}
