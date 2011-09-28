package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author michael
 */
@Entity
public class ComponentInformation implements Serializable {
    
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
    protected String id;
    private String moduleName;
    private String componentName;
    private String placeholderName;
    
    @ElementCollection(fetch= FetchType.EAGER)
    private Map<String, ComponentParameter> parameterMap = new HashMap<String, ComponentParameter>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getPlaceholderName() {
        return placeholderName;
    }

    public void setPlaceholderName(String placeholderName) {
        this.placeholderName = placeholderName;
    }

    public Map<String, ComponentParameter> getParameterMap() {
        return parameterMap;
    }
}
