package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.entity.Theme;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.util.StreamUtils;
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import dk.apaq.vfs.Node;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.velocity.texen.util.FileUtil;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.util.time.Time;

/**
 *
 * @author michael
 */
public class ThemeResource extends AbstractResource {

    private final SystemService service;
    
    private class ResponseOutputStream extends OutputStream {
    
        private final Response response;

        public ResponseOutputStream(Response response) {
            this.response = response;
        }

        @Override
        public void write(int i) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void write(byte[] bytes) throws IOException {
            response.write(bytes);
        }

        @Override
        public void write(byte[] bytes, int i, int i1) throws IOException {
            write(Arrays.copyOfRange(bytes, i, i1));
        }
        
        
        
        
        
        
    }
    
    private class ThemeResourceWriter extends WriteCallback {

        private final File file;

        public ThemeResourceWriter(File file) {
            this.file = file;
        }
        
        
        @Override
        public void writeData(Attributes attributes) {
            try {
                StreamUtils.copy(file.getInputStream(), new ResponseOutputStream(attributes.getResponse()));
            } catch (IOException ex) {
                throw new WicketRuntimeException(ex);
            }
        }
    
    }

    public ThemeResource(SystemService service) {
        this.service = service;
    }
    
    
    @Override
    protected ResourceResponse newResourceResponse(Attributes attributes) {
        File file = null;
        Website site = CmsUtil.getWebsite(service, attributes.getRequest());
        if(site==null) {
            return null;
        }
        
        OrganisationService organisationService = service.getOrganisationService(site.getOrganisation());
        
        String themeName = attributes.getParameters().get("themename").toString();
        
        Theme theme = organisationService.getThemes().read(themeName);
        if(theme == null) {
            return null;
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
            return null;
        }
        
        //  http://www.apaq.dk/_themes/Basic/style.css
        ResourceResponse rr = new ResourceResponse();
        try {
            rr.setContentLength(file.getLength());
            rr.setFileName(file.getName());
            rr.setLastModified(Time.valueOf(file.getLastModified()));
            rr.setWriteCallback(new ThemeResourceWriter(file));
        } catch (IOException ex) {
            throw new WicketRuntimeException(ex);
        }
        return rr;
        
    }
    
}
