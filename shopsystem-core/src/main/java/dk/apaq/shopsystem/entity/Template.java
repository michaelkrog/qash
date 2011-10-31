package dk.apaq.shopsystem.entity;

import dk.apaq.vfs.File;
import java.io.Serializable;
import java.util.ArrayList;
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
    private List<ComponentInformation> defaultComponentInformations = new ArrayList<ComponentInformation>();

    public Template(String name, List<Placeholder> placeHolders, List<ComponentInformation> defaultComponentInformations, File markupFile) {
        this.name = name;
        this.markupFile = markupFile;
        this.placeHolders = placeHolders;
        this.defaultComponentInformations = defaultComponentInformations;
        this.codeFile = null;
    }

    public Template(String name, List<Placeholder> placeHolders, List<ComponentInformation> defaultComponentInformations, File markupFile, File codeFile) {
        this.name = name;
        this.placeHolders = placeHolders;
        this.defaultComponentInformations = defaultComponentInformations;
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

    public List<ComponentInformation> getDefaultComponentInformations() {
        return Collections.unmodifiableList(defaultComponentInformations);
    }
    
    public void addDefaultComponentInformation(ComponentInformation componentInformation) {
        defaultComponentInformations.add(componentInformation);
    }
    
    public int getDefaultComponentInformationCount() {
        return defaultComponentInformations.size();
    }
    
    public ComponentInformation getDefaultComponentInformation(int index) {
        return defaultComponentInformations.get(index);
    }
    
    public void clearDefaultComponentInformations() {
        defaultComponentInformations.clear();
    }
}
