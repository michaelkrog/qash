package dk.apaq.shopsystem.vfs;

import dk.apaq.vfs.Directory;
import dk.apaq.vfs.FileSystem;
import dk.apaq.vfs.Node;
import dk.apaq.vfs.Path;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

/**
 *
 * @author michael
 */
public class LayeredNode implements Node {

    private final FileSystem fileSystem;
    private final Node wrappedNode;

    public LayeredNode(FileSystem fileSystem, Node wrappedNode) {
        this.fileSystem = fileSystem;
        this.wrappedNode = wrappedNode;
    }
    
    @Override
    public void moveTo(Directory newParent) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void moveTo(Directory newParent, String newName) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setName(String name) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getBaseName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSuffix() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Directory getParent() throws FileNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FileSystem getFileSystem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDirectory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isFile() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isHidden() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date getLastModified() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLastModified(Date date) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(Node node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public URI toUri() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean equals(Node node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Path getPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canRead() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canWrite() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(Object t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
