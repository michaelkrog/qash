package dk.apaq.shopsystem.rendering;

import org.apache.wicket.request.resource.AbstractResource;

/**
 * Retrieves a rendered output of a specific page. The page will be rendered as 
 * png.
 * @author michael
 */
public class RenderedPageResource extends AbstractResource {

    @Override
    protected ResourceResponse newResourceResponse(Attributes attributes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
