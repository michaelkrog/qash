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

    private final List<Directory> layers;
    
    public LayeredDirectory(LayeredFileSystem fileSystem, LayeredDirectory parent, Directory wrappedNode) {
        super(fileSystem, parent, wrappedNode);
        layers = fileSystem.getLayers(getPath());
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
        return parent==null;
    }

    @Override
    public boolean hasChild(String name) {
        if(((Directory)wrappedNode).hasChild(name)) {
            return true;
        }
        
        for(Directory dir : layers) {
            if(dir.hasChild(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasFile(String name) {
        if(((Directory)wrappedNode).hasFile(name)) {
            return true;
        }
        
        for(Directory dir : layers) {
            if(dir.hasFile(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasDirectory(String name) {
        if(((Directory)wrappedNode).hasDirectory(name)) {
            return true;
        }
        
        for(Directory dir : layers) {
            if(dir.hasDirectory(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Node getChild(String name) throws FileNotFoundException {
        if(((Directory)wrappedNode).hasChild(name)) {
            return ((Directory)wrappedNode).getChild(name);
        }
        
        for(Directory dir : layers) {
            if(dir.hasChild(name)) {
                return dir.getChild(name);
            }
        }
        throw new FileNotFoundException("Node not found. [parent="+this.getPath()+"; name="+name+"]");
    }

    @Override
    public File getFile(String name) throws FileNotFoundException {
        if(((Directory)wrappedNode).hasFile(name)) {
            return ((Directory)wrappedNode).getFile(name);
        }
        
        for(Directory dir : layers) {
            if(dir.hasFile(name)) {
                return dir.getFile(name);
            }
        }
        throw new FileNotFoundException("File not found. [parent="+this.getPath()+"; name="+name+"]");
    }

    @Override
    public Directory getDirectory(String name) throws FileNotFoundException {
        if(((Directory)wrappedNode).hasDirectory(name)) {
            return ((Directory)wrappedNode).getDirectory(name);
        }
        
        for(Directory dir : layers) {
            if(dir.hasDirectory(name)) {
                return dir.getDirectory(name);
            }
        }
        throw new FileNotFoundException("Directory not found. [parent="+this.getPath()+"; name="+name+"]");
    }

    @Override
    public File getFile(String name, boolean createIfNeeded) throws FileNotFoundException, IOException {
        try{
            return getFile(name);
        } catch(FileNotFoundException ex) {
            if(createIfNeeded) {
                return createFile(name);
            } else {
                throw ex;
            }
        }
    }

    @Override
    public Directory getDirectory(String name, boolean createIfNeeded) throws FileNotFoundException, IOException {
        try{
            return getDirectory(name);
        } catch(FileNotFoundException ex) {
            if(createIfNeeded) {
                return createDirectory(name);
            } else {
                throw ex;
            }
        }
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
        wrappedNode.delete();
    }

    @Override
    public boolean isBundle() {
        return ((Directory)wrappedNode).isBundle();
    }
    
    
    
}
