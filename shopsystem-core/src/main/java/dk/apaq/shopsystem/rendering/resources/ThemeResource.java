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

/**
 *
 * @author michael
 */
public class ThemeResource extends AbstractResource {

    private final SystemService service;

    public ThemeResource(SystemService service) {
        this.service = service;
    }
    
    
    @Override
    protected ResourceResponse newResourceResponse(Attributes attributes) {
        File file = null;
        ResourceResponse rr = new ResourceResponse();
        Website site = CmsUtil.getWebsite(service, attributes.getRequest());
        if(site==null) {
            return null;
        }
        
        OrganisationService organisationService = service.getOrganisationService(site.getOrganisation());
        
        String themeName = attributes.getParameters().get("themename").toString();
        
        Theme theme = organisationService.getThemes().read(themeName);
        if(theme == null) {
            rr.setError(404, "Theme '"+themeName+"' not found.");
            return rr;
        }
        
        Directory dir = theme.getDirectory();
        
        for(int i=0;i<attributes.getParameters().getIndexedCount();i++) {
            String nextSegment = attributes.getParameters().get(i).toString();
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
