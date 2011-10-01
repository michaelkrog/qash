package dk.apaq.shopsystem.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.apaq.shopsystem.util.ScriptAnnotation;
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import dk.apaq.vfs.Node;
import dk.apaq.vfs.NodeFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
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
    //private ClassDeserializer classDeserializer = new ClassDeserializer();
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    private CodeNodeFilter codeNodeFilter = new CodeNodeFilter();

    
    private class CodeNodeFilter implements NodeFilter {

        @Override
        public boolean accept(Node node) {
            return "code".equals(node.getSuffix());
        }
        
    }
    
    private static class ModuleInfo {

        private String version;
        private Date releaseDate;
        private SellerInfo seller;
        
        public String getVersion() {
            return version;
        }

        public Date getReleaseDate() {
            return releaseDate;
        }

        public SellerInfo getSellerInfo() {
            return seller;
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

    public Component getComponent(String name) {
        for(Component component : listComponents()) {
            if(name.equals(component.getName())) {
                return component;
            }
        }
        return null;
    }
    
    public List<Component> listComponents() {
        if (componentList == null) {
            List<Component> newList = new ArrayList<Component>();
            for(File codeFile : dir.getFiles(codeNodeFilter)) {
                String name = codeFile.getBaseName();
                try {
                    String description = null;
                    Map<String, ParameterDefinition> paramMap = new HashMap<String, ParameterDefinition>();
                    File markupFile = dir.getFile(name + ".html");
                    
                    List<ScriptAnnotation.Annotation> annotations = ScriptAnnotation.read(codeFile.getInputStream());
                    for(ScriptAnnotation.Annotation annotation : annotations) {
                        if("Description".equals(annotation.getName())) {
                            description = annotation.getMap().get("value");
                        }
                        
                        if("Parameter".equals(annotation.getName())) {
                            String paramName = annotation.getMap().get("name");
                            String paramType = annotation.getMap().get("type");
                            String paramDefault = annotation.getMap().get("default");
                            String paramOptText = annotation.getMap().get("optionalText");
                            if(paramName!=null && paramType!=null) {
                                paramMap.put(paramName, new ParameterDefinition(paramType, paramDefault, paramOptText));
                            }
                        }
                    }
                    
                    newList.add(new Component(name, description, codeFile, markupFile, paramMap));
                } catch (IOException ex) {
                    //Cannot find file - warn about it but otherwise ignore it.
                    LOG.warn("Component registered in module is not found. [Module=" + getName() + ";Component=" + name + "]");
                }
            }

            componentList = Collections.unmodifiableList(newList);
        }
        return componentList;
    }

    
    
}
