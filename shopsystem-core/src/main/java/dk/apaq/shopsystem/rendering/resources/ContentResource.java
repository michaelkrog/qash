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
import dk.apaq.vfs.mime.MimeType;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.util.time.Time;

/**
 *
 * @author michael
 */
public class ContentResource extends AbstractResource {

    private final SystemService service;

    public ContentResource(SystemService service) {
        this.service = service;
    }

    @Override
    protected ResourceResponse newResourceResponse(Attributes attributes) {
        File file = null;
        ResourceResponse rr = new ResourceResponse();
        Website site = CmsUtil.getWebsite(service, attributes.getRequest());
        if (site == null) {
            return null;
        }

        OrganisationService organisationService = service.getOrganisationService(site.getOrganisation());

        try {

            Directory dir = organisationService.getFileSystem().getRoot().getDirectory("Content", true);

            for (int i = 0; i < attributes.getParameters().getIndexedCount(); i++) {
                String nextSegment = attributes.getParameters().get(i).toString();
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

            rr.setContentType(MimeType.retrieve(file));
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
