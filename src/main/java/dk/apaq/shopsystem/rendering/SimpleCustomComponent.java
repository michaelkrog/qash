package dk.apaq.shopsystem.rendering;

import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 */
public class SimpleCustomComponent extends Panel {

    
    public SimpleCustomComponent(String id, Reader scriptReader){
        super(id);
    
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        engine.put("parent", new SimpleCustomComponentContainerWrapper(this));
        try {
            engine.eval(scriptReader);
        } catch (ScriptException ex) {
            Logger.getLogger(SimpleCustomComponent.class.getName()).log(Level.SEVERE, null, ex);
        }

        Invocable inv = (Invocable) engine;
        SimpleCustomComponentScript sc = inv.getInterface(SimpleCustomComponentScript.class);
        sc.init();

    }


}
