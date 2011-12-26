package dk.apaq.shopsystem.rendering.module;

import dk.apaq.shopsystem.entity.ComponentParameter;
import java.util.Map;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author michael
 */
public abstract class CmsModule extends Panel {

    protected String contextId;
    protected Map<String, ComponentParameter> parameters;
    
    public CmsModule(String id) {
        super(id);
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
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

