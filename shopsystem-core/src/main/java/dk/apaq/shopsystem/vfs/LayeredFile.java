package dk.apaq.shopsystem.vfs;

import dk.apaq.vfs.File;
import dk.apaq.vfs.FileSystem;
import dk.apaq.vfs.Node;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author michael
 */
public class LayeredFile extends LayeredNode implements File {

    public LayeredFile(FileSystem fileSystem, Node wrappedNode) {
        super(fileSystem, wrappedNode);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getLength() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
