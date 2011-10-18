package dk.apaq.shopsystem.rendering.resources;

import dk.apaq.shopsystem.service.SystemService;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

/**
 *
 * @author michael
 */
public class ThemeResourceReference extends ResourceReference {

    private final SystemService service;

    public ThemeResourceReference(SystemService service) {
        super("ThemeResource");
        this.service = service;
    }

    
    @Override
    public IResource getResource() {
        return new ThemeResource(service);
    }
    
}
