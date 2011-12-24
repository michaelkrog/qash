package dk.apaq.shopsystem.rendering.resources;

import dk.apaq.shopsystem.entity.Theme;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.rendering.CmsUtil;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.util.StreamUtils;
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import dk.apaq.vfs.Node;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.util.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author michael
 */
public class ThemeResource extends AbstractResource {

    private static final Logger LOG = LoggerFactory.getLogger(ThemeResource.class);
    private final SystemService service;

    public ThemeResource(SystemService service) {
        this.service = service;
    }
    
    
    @Override
    protected ResourceResponse newResourceResponse(Attributes attributes) {
        File file = null;
        ResourceResponse rr = new ResourceResponse();
        OrganisationService organisationService = CmsUtil.getOrganisationService(service, attributes.getRequest());
        if (organisationService == null) {
            LOG.error("organisationservice not found.");
            return new ResourceResponse();
        }
        
        String themeName = attributes.getParameters().get("themename").toString();
        
        //Some may get the url wrong and end the themename with '.theme'. Just remove it if they have.
        if(themeName.endsWith(".theme")) {
            themeName = themeName.substring(0, themeName.length()-6);
        }
        
        Theme theme = organisationService.getThemes().read(themeName);
        if(theme == null) {
            rr.setError(404, "Theme '"+themeName+"' not found.");
            return rr;
        }
        
        Directory dir = theme.getDirectory();
        
        for(int i=0;i<attributes.getParameters().getIndexedCount();i++) {
            String nextSegment = attributes.getParameters().get(i).toString();
            if("".equals(nextSegment)) {
                continue;
            }
            
            if(!dir.hasChild(nextSegment)) {
                return null;
            }
            try {
                Node node = dir.getChild(nextSegment);
                if(node.isFile()) {
                    file = (File) node;
                    break;
                } else {
                    dir = (Directory) node;
                }
            } catch (FileNotFoundException ex) {
                throw new WicketRuntimeException(ex);
            }
            
        }
        
        if(file == null) {
            rr.setError(404, "File not found.");
            return rr;
            
        }
        
        //  http://www.apaq.dk/_themes/Basic/style.css
        try {
            rr.setContentLength(file.getLength());
            rr.setFileName(file.getName());
            rr.setLastModified(Time.valueOf(file.getLastModified()));
            rr.setWriteCallback(new ResourceWriter(file));
            
        } catch (IOException ex) {
            throw new WicketRuntimeException(ex);
        }
        return rr;
        
    }
    
}
