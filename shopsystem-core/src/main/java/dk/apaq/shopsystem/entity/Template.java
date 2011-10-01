package dk.apaq.shopsystem.entity;

import dk.apaq.vfs.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class Template {

    private final String name;
    private final File file;
    private final List<String> placeHolderIds;

    public Template(String name, File file, List<String> placeHolderIds) {
        this.name = name;
        this.file = file;
        this.placeHolderIds = placeHolderIds;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public List<String> getPlaceHolderIds() {
        return Collections.unmodifiableList(placeHolderIds);
    }

    
    
}
