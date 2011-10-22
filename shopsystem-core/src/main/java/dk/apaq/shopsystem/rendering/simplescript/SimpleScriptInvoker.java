package dk.apaq.shopsystem.rendering.simplescript;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author michael
 */
public class SimpleScriptInvoker {

    public static <T> T invoke(Class<T> implementationInterface, Map<String, Object> parameterMap, InputStream is) throws ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");

        Invocable inv = (Invocable) engine;
        for (Entry<String, Object> entry : parameterMap.entrySet()) {
            engine.put(entry.getKey(), entry.getValue());
        }
        engine.eval(new InputStreamReader(is));
        T script = inv.getInterface(implementationInterface);
        return script;
    }
}
