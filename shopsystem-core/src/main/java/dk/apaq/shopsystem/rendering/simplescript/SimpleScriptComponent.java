package dk.apaq.shopsystem.rendering.simplescript;

import dk.apaq.shopsystem.context.DataContext;
import dk.apaq.shopsystem.rendering.simplescript.SimpleScriptContainerWrapper;
import dk.apaq.shopsystem.rendering.simplescript.SimpleScript;
import dk.apaq.shopsystem.entity.Component;
import dk.apaq.shopsystem.rendering.VfsResourceStream;
import dk.apaq.shopsystem.rendering.WicketApplication;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.LoggerFactory;



/**
 *
 * @author michael
 */
public class SimpleScriptComponent extends Panel implements IMarkupResourceStreamProvider {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(SimpleScriptComponent.class);
    
    private final Component component;
    
    public SimpleScriptComponent(String id, Component component) {
        super(id);
        this.component = component;
        try {
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            
            Invocable inv = (Invocable) engine;
            engine.put("service", DataContext.getService());
            engine.put("parent", new SimpleScriptContainerWrapper(this));
            engine.eval(new InputStreamReader(component.getCodeFile().getInputStream()));
            SimpleScript componentScript = inv.getInterface(SimpleScript.class);
            
                
            componentScript.render();
        } catch (IOException ex) {
            LOG.error("Unable to read from component code file", ex);
        } catch (ScriptException ex) {
            LOG.error("Unable to evaluate script.", ex);
        }
    }

    @Override
    public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
        return new VfsResourceStream(component.getMarkupFile());
    }


    
}
