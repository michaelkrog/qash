package dk.apaq.shopsystem.rendering;

import dk.apaq.vfs.File;
import dk.apaq.vfs.mime.MimeType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;

/**
 *
 */
public class VfsResourceStream implements IResourceStream {

    private final File file;

    public VfsResourceStream(File file) {
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
            return file.getInputStream();
        } catch (IOException ex) {
            throw new ResourceStreamNotFoundException(ex);
        }
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public Locale getLocale() {
        return Locale.getDefault();
    }

    @Override
    public void setLocale(Locale locale) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getStyle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setStyle(String style) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getVariation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setVariation(String variation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Time lastModifiedTime() {
        return Time.valueOf(file.getLastModified());
    }

}
