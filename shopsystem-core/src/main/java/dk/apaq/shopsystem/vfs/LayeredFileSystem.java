package dk.apaq.shopsystem.vfs;

import dk.apaq.vfs.Directory;
import dk.apaq.vfs.FileSystem;
import dk.apaq.vfs.Node;
import dk.apaq.vfs.Path;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author michael
 */
public class LayeredFileSystem implements FileSystem {

    private final FileSystem primaryFilesystem;
    private final FileSystem secondaryFilesystem;
    
    private class LayerEntry {
        private final Path primaryPath;
        private final Path secondaryPath;

        public LayerEntry(Path primaryPath, Path secondaryPath) {
            this.primaryPath = primaryPath;
            this.secondaryPath = secondaryPath;
        }

        public Path getPrimaryPath() {
            return primaryPath;
        }

        public Path getSecondaryPath() {
            return secondaryPath;
        }
        
    }

    public LayeredFileSystem(FileSystem primaryFilesystem, FileSystem secondaryFilesystem) {
        this.primaryFilesystem = primaryFilesystem;
        this.secondaryFilesystem = secondaryFilesystem;
    }
    
    @Override
    public String getName() {
        return "Layered filesystem between primary["+primaryFilesystem.getName()+"] and secondary["+secondaryFilesystem.getName()+"] filesystems.";
    }

    @Override
    public Map getInfo() {
        return primaryFilesystem.getInfo();
    }

    @Override
    public Directory getRoot() throws FileNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getSize() {
        return primaryFilesystem.getSize();
    }

    @Override
    public long getFreeSpace() {
        return primaryFilesystem.getFreeSpace();
    }

    @Override
    public Node getNode(String path) throws FileNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() throws IOException {
        primaryFilesystem.close();
        secondaryFilesystem.close();
    }
    
}
