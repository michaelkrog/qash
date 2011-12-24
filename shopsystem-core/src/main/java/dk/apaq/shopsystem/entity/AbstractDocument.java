package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author michael
 */
@Entity
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractDocument implements Serializable {
    @Id
    @GeneratedValue(generator = "system-uuid")
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
    
    @OneToMany(orphanRemoval=true, cascade=CascadeType.ALL,fetch= FetchType.EAGER)
    private List<ComponentInformation> placeholderList = new ArrayList<ComponentInformation>();

    public void addComponentInformation(ComponentInformation ci) {
        placeholderList.add(ci);
    }

    public void clearPlaceholderInformation() {
        placeholderList.clear();
    }

    public List<ComponentInformation> getComponentInformations() {
        return Collections.unmodifiableList(placeholderList);
    }

    public List<ComponentInformation> getComponentInformations(String placeholderId) {
        List<ComponentInformation> list = new ArrayList<ComponentInformation>();
        for (ComponentInformation c : placeholderList) {
            if (placeholderId.equals(c.getPlaceholderName())) {
                list.add(c);
            }
        }
        return Collections.unmodifiableList(list);
    }

    public Date getDateChanged() {
        return dateChanged;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getName() {
        return name;
    }

    public List<String> getPlaceholderIds() {
        List<String> ids = new ArrayList<String>();
        for (ComponentInformation c : placeholderList) {
            if (!ids.contains(c.getPlaceholderName())) {
                ids.add(c.getPlaceholderName());
            }
        }
        return Collections.unmodifiableList(ids);
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getThemeName() {
        return themeName;
    }

    public String getTitle() {
        return title;
    }

    public void removeComponentInformation(String componentInformationId) {
        Iterator<ComponentInformation> it = placeholderList.iterator();
        while (it.hasNext()) {
            ComponentInformation c = it.next();
            if (componentInformationId.equals(c.getId())) {
                it.remove();
                return;
            }
        }
    }

    public void removePlaceholderId(String placeholderId) {
        Iterator<ComponentInformation> it = placeholderList.iterator();
        while (it.hasNext()) {
            ComponentInformation c = it.next();
            if (placeholderId.equals(c.getPlaceholderName())) {
                it.remove();
            }
        }
    }

    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    public void setDateCreated(Date date) {
        this.dateCreated = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
}
