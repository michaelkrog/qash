package dk.apaq.shopsystem.entity;

import dk.apaq.shopsystem.util.ScriptAnnotation;
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Specifies a module for a web page.
 */
public class Module extends AbstractModule implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Module.class);
    private List<Component> componentList;
    private SuffixNodeFilter nodeFilter = new SuffixNodeFilter("code");
   

    public Module(Directory dir) throws IOException {
        super(dir);
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
            for(File codeFile : dir.getFiles(nodeFilter)) {
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

    @Override
    protected String getBundleSuffix() {
        return "module";
    }

    
    
}
