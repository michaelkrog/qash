package dk.apaq.shopsystem.rendering;

import dk.apaq.vfs.Directory;
import java.io.Serializable;

/**
 * Specifies a module for a web page.
 */
public class Module implements Serializable {

    private final Directory dir;

    public Module(Directory dir) {
        this.dir = dir;
    }

    public String getDescription() {
        return null;
    }

    public String getName() {
        return null;
    }

    
}
