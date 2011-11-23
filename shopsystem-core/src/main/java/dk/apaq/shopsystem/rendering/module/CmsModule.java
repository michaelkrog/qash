package dk.apaq.shopsystem.rendering.module;

import dk.apaq.shopsystem.entity.ComponentParameter;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.OrganisationService;
import java.util.Map;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author michael
 */
public abstract class CmsModule extends Panel {

    protected OrganisationService organisationService;
    protected Website webSite;
    protected Map<String, ComponentParameter> parameters;
    
    public CmsModule(String id) {
        super(id);
    }

    public void setOrganisationService(OrganisationService organisationService) {
        this.organisationService = organisationService;
    }

    public void setWebSite(Website webSite) {
        this.webSite = webSite;
    }

    public void setParameters(Map<String, ComponentParameter> parameters) {
        this.parameters = parameters;
    }

    /**
     * Tells module to add its children.
     */
    public abstract void compose();
    
    public static CmsModule create(String markupId, String name) {
        
        if("label".equals(name)) {
            return new Label(markupId);
        }
        
        if("image".equals(name)) {
            return new Image(markupId);
        }
        
        throw new IllegalArgumentException("Module not found [name="+name+"]");
    }
}

