package dk.apaq.shopsystem.service;

import dk.apaq.vfs.FileSystem;
import java.io.Serializable;

/**
 *
 * @author michael
 */
public interface FileSystemPopulator extends Serializable {
    void populate(FileSystem fileSystem);
}
