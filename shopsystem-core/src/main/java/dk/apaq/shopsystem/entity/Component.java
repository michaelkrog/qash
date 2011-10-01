package dk.apaq.shopsystem.entity;

import dk.apaq.vfs.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

/**
 *
 */
public class Component {

    private final String name;
    private final String description;
    private final File codeFile;
    private final File markupFile;
    private final Map<String, ParameterDefinition> paramMap;

    public Component(String name, String description, File codeFile, File markupFile, Map<String, ParameterDefinition> paramMap) {
        this.name = name;
        this.description = description;
        this.codeFile = codeFile;
        this.markupFile = markupFile;
        this.paramMap = paramMap;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public File getCodeFile() {
        return codeFile;
    }

    public File getMarkupFile() {
        return markupFile;
    }

    public Map<String, ParameterDefinition> getParameterMap() {
        return Collections.unmodifiableMap(paramMap);
    }

    
    
}
