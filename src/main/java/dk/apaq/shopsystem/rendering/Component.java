package dk.apaq.shopsystem.rendering;

import dk.apaq.vfs.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 *
 */
public class Component {

    private final String name;
    private final String description;
    private final File file;
    private final Map<String, ComponentParameter> paramMap;

    public class ComponentParameter {
        private Class type;
        private String optionalText;

        public Class getType() {
            return type;
        }

        public String getOptionalText() {
            return optionalText;
        }

    }

    public Component(String name, String description, File file, Map<String, ComponentParameter> paramMap) {
        this.name = name;
        this.description = description;
        this.file = file;
        this.paramMap = paramMap;
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

    public Map<String, ComponentParameter> getParamMap() {
        return paramMap;
    }
    
}
