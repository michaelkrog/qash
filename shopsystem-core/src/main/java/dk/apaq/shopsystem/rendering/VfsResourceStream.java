package dk.apaq.shopsystem.rendering;

import dk.apaq.vfs.File;
import dk.apaq.vfs.mime.MimeType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class VfsResourceStream extends AbstractResourceStream {

    private static final Logger LOG = LoggerFactory.getLogger(VfsResourceStream.class);
    private final File file;

    public VfsResourceStream(File file) {
        LOG.debug("VfsResource created [File={}]", file);
        this.file = file;
    }
    
    @Override
    public String getContentType() {
        return MimeType.retrieve(file);
    }

    @Override
    public Bytes length() {
        try {
            return Bytes.bytes(file.getLength());
        } catch (IOException ex) {
            return Bytes.bytes(0);
        }
    }

    @Override
    public InputStream getInputStream() throws ResourceStreamNotFoundException {
        try {
            LOG.debug("Returning input stream for file.");
            return file.getInputStream();
        } catch (IOException ex) {
            LOG.error("Error while getting input stream for file.", ex);
            throw new ResourceStreamNotFoundException(ex);
        }
    }

    @Override
    public void close() throws IOException {

    }

    
    @Override
    public Time lastModifiedTime() {
        return Time.valueOf(file.getLastModified());
    }

    @Override
    public String toString() {
        //TODO
        return file.getPath().toString();
    }
    
   
}
