package dk.apaq.shopsystem.rendering.resources;

import dk.apaq.shopsystem.service.SystemService;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

/**
 *
 * @author michael
 */
public class ContentResourceReference extends ResourceReference {

    private final SystemService service;
    
    public ContentResourceReference(SystemService service) {
        super("ContentResource");
        this.service = service;
    }

    
    @Override
    public IResource getResource() {
        return new ResolvingOrganisationContentResource(service);
    }
    
}
