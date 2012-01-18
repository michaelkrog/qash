package dk.apaq.shopsystem.rendering.resources;

import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.rendering.CmsUtil;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import dk.apaq.vfs.Node;
import dk.apaq.vfs.Path;
import dk.apaq.vfs.impl.layered.LayeredFileSystem;
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
public class ResolvingOrganisationContentResource extends AbstractResource {

    private static final Logger LOG = LoggerFactory.getLogger(ResolvingOrganisationContentResource.class);
    private final SystemService service;

    public ResolvingOrganisationContentResource(SystemService service) {
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
        
        try {

            //In order to support special handling of images we use layered filesystem.
            //Symbol requires images from both organisation and system
            
            LayeredFileSystem lfs = new LayeredFileSystem(organisationService.getFileSystem());
            Directory systemImagesDir = service.getFileSystem().getRoot().getDirectory("System",true).getDirectory("Content", true).getDirectory("Images", true);
            lfs.addLayer(new Path("/Content/Images"), systemImagesDir);
            Directory dir = lfs.getRoot().getDirectory("Content", true);

            for (int i = 0; i < attributes.getParameters().getIndexedCount(); i++) {
                String nextSegment = attributes.getParameters().get(i).toString();
                if("".equals(nextSegment)) {
                    continue;
                }
                
                if (!dir.hasChild(nextSegment)) {
                    break;
                }
                Node node = dir.getChild(nextSegment);
                if (node.isFile()) {
                    file = (File) node;
                    break;
                } else {
                    dir = (Directory) node;
                }

            }

            if (file == null) {
                rr.setError(404);
                return rr;
            }

            //rr.setContentType(MimeType.retrieve(file));
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
