package dk.apaq.shopsystem.rendering;

import dk.apaq.vfs.File;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class Template {

    private final String name;
    private final String description;
    private final File file;

    public Template(String name, String description, File file) {
        this.name = name;
        this.description = description;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public InputStream getContent() throws IOException {
        return file.getInputStream();
    }
}
