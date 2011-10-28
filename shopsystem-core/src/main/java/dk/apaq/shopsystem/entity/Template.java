package dk.apaq.shopsystem.entity;

import dk.apaq.vfs.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class Template implements Serializable {

    private final String name;
    private final List<Placeholder> placeHolders;
    private final File markupFile;
    private final File codeFile;
    
    public Template(String name, List<Placeholder> placeHolders, File markupFile) {
        this.name = name;
        this.markupFile = markupFile;
        this.placeHolders = placeHolders;
        this.codeFile = null;
    }

    public Template(String name, List<Placeholder> placeHolders, File markupFile, File codeFile) {
        this.name = name;
        this.placeHolders = placeHolders;
        this.markupFile = markupFile;
        this.codeFile = codeFile;
    }
    

    public String getName() {
        return name;
    }

    public File getMarkupFile() {
        return markupFile;
    }

    public File getCodeFile() {
        return codeFile;
    }

    public List<Placeholder> getPlaceHolders() {
        return Collections.unmodifiableList(placeHolders);
    }

    
    
}
