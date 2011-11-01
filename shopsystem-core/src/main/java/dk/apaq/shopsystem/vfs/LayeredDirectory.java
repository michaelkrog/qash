package dk.apaq.shopsystem.vfs;

import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import dk.apaq.vfs.FileSystem;
import dk.apaq.vfs.Node;
import dk.apaq.vfs.NodeFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author michael
 */
public class LayeredDirectory extends LayeredNode implements Directory {

    public LayeredDirectory(FileSystem fileSystem, Node wrappedNode) {
        super(fileSystem, wrappedNode);
    }

    @Override
    public Directory createDirectory(String name) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public File createFile(String name) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRoot() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasChild(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasFile(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasDirectory(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Node getChild(String name) throws FileNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public File getFile(String name) throws FileNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Directory getDirectory(String name) throws FileNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public File getFile(String name, boolean createIfNeeded) throws FileNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Directory getDirectory(String name, boolean createIfNeeded) throws FileNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Node> getChildren() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Directory> getDirectories() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<File> getFiles() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Node> getChildren(NodeFilter filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Directory> getDirectories(NodeFilter filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<File> getFiles(NodeFilter filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(boolean recursive) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isBundle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
}
