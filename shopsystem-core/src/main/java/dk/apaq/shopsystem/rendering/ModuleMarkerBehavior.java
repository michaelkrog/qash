package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.entity.ComponentInformation;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;

/**
 *
 * @author michael
 */
public class ModuleMarkerBehavior extends Behavior {

    private final ComponentInformation ci;

    public ModuleMarkerBehavior(ComponentInformation ci) {
        this.ci = ci;
    }
    
    @Override
    public void onComponentTag(Component component, ComponentTag tag) {
        tag.put("location", ci.getId());
    }
 
    
}
