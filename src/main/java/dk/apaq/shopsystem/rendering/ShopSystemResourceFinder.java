package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.vfs.File;
import dk.apaq.vfs.FileSystem;
import dk.apaq.vfs.Node;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.util.file.IResourceFinder;
import org.apache.wicket.util.resource.IResourceStream;

/**
 *
 */
public class ShopSystemResourceFinder implements IResourceFinder {

    private final FileSystem systemwideFs;
    private final FileSystem organisationwideFs;

    public ShopSystemResourceFinder(FileSystem systemwideFs, FileSystem organisationwideFs) {
        this.systemwideFs = systemwideFs;
        this.organisationwideFs = organisationwideFs;
    }
    
    @Override
    public IResourceStream find(Class<?> clazz, String pathname) {
        try {
            Node node = organisationwideFs.getNode(pathname);
            if(!node.isFile()) {
                return null;
            }

            File file = (File)node;
            return new VfsResourceStream(file);
        } catch (FileNotFoundException ex) {
            return null;
        }

    }

}
