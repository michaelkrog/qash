package dk.apaq.shopsystem.rendering.simplescript;

import dk.apaq.shopsystem.entity.ParameterDefinition;
import java.util.Map;

/**
 *
 * @author michael
 */
public interface SimpleScript {
 
    public String getDescription();
    
    public void ParameterDefinitions(Map<String, ParameterDefinition> map);
    
    void render();
}
