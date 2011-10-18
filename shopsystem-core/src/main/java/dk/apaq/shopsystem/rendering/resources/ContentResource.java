package dk.apaq.shopsystem.rendering.resources;

import dk.apaq.vfs.File;
import dk.apaq.vfs.mime.MimeType;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.util.time.Time;

/**
 *
 * @author michael
 */
public class ContentResource extends AbstractResource {

    private final File file;

    public ContentResource(File file) {
        this.file = file;
    }

    @Override
    protected ResourceResponse newResourceResponse(Attributes attributes) {
        ResourceResponse rr = new ResourceResponse();
        try {
            rr.setContentType(MimeType.retrieve(file));
            rr.setContentLength(file.getLength());
            rr.setFileName(file.getName());
            rr.setLastModified(Time.valueOf(file.getLastModified()));
            rr.setWriteCallback(new ResourceWriter(file));
            return rr;
        } catch (IOException ex) {
            rr.setError(500, ex.getMessage());
            return rr;
        }
    }
}
