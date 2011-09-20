package dk.apaq.shopsystem.rendering;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dk.apaq.shopsystem.rendering.Component.ComponentParameter;
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Specifies a module for a web page.
 */
public class Module implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Module.class);
    private final Directory dir;
    private final File infoFile;
    private ModuleInfo info;
    private List<Component> componentList;
    private ClassDeserializer classDeserializer = new ClassDeserializer();
    private Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, classDeserializer).setDateFormat("yyyy-MM-dd").create();

    private class ClassDeserializer implements JsonDeserializer<Class> {

        @Override
        public Class deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String text = json.getAsJsonPrimitive().getAsString();
            if("String".equalsIgnoreCase(text)) {
                return String.class;
            }

            if("Integer".equalsIgnoreCase(text)) {
                return Integer.class;
            }

            if("Double".equalsIgnoreCase(text)) {
                return Double.class;
            }

            if("Date".equalsIgnoreCase(text)) {
                return Date.class;
            }

            throw new JsonParseException("Unable to parse class [text="+text+"]");
        }

    }

    private static class ComponentInfo {

        private String description;
        private Map<String, Component.ComponentParameter> parameters;

        public String getDescription() {
            return description;
        }

        public Map<String, ComponentParameter> getParameters() {
            return parameters;
        }

    }

    private static class ModuleInfo {

        private String version;
        private Date releaseDate;
        private SellerInfo seller;
        private Map<String, ComponentInfo> components = new HashMap<String, ComponentInfo>();

        public String getVersion() {
            return version;
        }

        public Date getReleaseDate() {
            return releaseDate;
        }

        public SellerInfo getSellerInfo() {
            return seller;
        }

        public Map<String, ComponentInfo> getComponentMap() {
            return components;
        }
    }

    public Module(Directory dir) throws IOException {
        if (!dir.isBundle() || !"module".equals(dir.getSuffix())) {
            throw new IllegalArgumentException("The directory is not a module bundle.");
        }
        this.dir = dir;
        this.infoFile = dir.getFile("module.info");

        info = gson.fromJson(new InputStreamReader(infoFile.getInputStream()), ModuleInfo.class);
    }

public String getVersion() {
        return info.getVersion();
    }

    public Date getReleaseDate() {
        return info.getReleaseDate();
    }

    public SellerInfo getSellerInfo() {
        return info.getSellerInfo();
    }

    public String getName() {
        return dir.getBaseName();
    }

    public List<Component> listComponents() {
        if (componentList == null) {
            List<Component> newList = new ArrayList<Component>();
            for (Map.Entry<String, ComponentInfo> entry : info.getComponentMap().entrySet()) {
                try {
                    File file = dir.getFile(entry.getKey() + ".code");
                    newList.add(new Component(entry.getKey(), entry.getValue().getDescription(), file, entry.getValue().getParameters()));
                } catch (FileNotFoundException ex) {
                    //Cannot find file - warn about it but otherwise ignore it.
                    LOG.warn("Component registered in module is not found. [Module=" + getName() + ";Component=" + entry.getKey() + "]");
                }
            }
            componentList = Collections.unmodifiableList(newList);
        }
        return componentList;
    }

    
}
