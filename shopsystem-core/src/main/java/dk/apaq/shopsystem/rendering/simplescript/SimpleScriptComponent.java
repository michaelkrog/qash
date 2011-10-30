package dk.apaq.shopsystem.rendering.simplescript;

import dk.apaq.shopsystem.entity.Component;
import dk.apaq.shopsystem.entity.ComponentParameter;
import dk.apaq.shopsystem.rendering.VfsResourceStream;
import dk.apaq.shopsystem.service.OrganisationService;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.PrivilegedActionException;
import java.util.HashMap;
import java.util.Map;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 * @author michael
 */
public class SimpleScriptComponent extends Panel implements IMarkupCacheKeyProvider, IMarkupResourceStreamProvider {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleScriptComponent.class);
    
    private final Component component;
    private boolean failed = false;
    private String error;
    
    public SimpleScriptComponent(OrganisationService organisationService, String id, Component component, Map<String, ComponentParameter> paramMap) {
        super(id);
        this.component = component;
        try {
            Map<String, Object> scriptParams = new HashMap<String, Object>();
            scriptParams.put("service", organisationService);
            scriptParams.put("parent", new SimpleScriptContainerWrapper(this));
            scriptParams.put("parameters", paramMap);
            SimpleScriptComponentRenderer componentScript = SimpleScriptInvoker.invoke(SimpleScriptComponentRenderer.class, 
                                                                                        scriptParams, 
                                                                                        component.getCodeFile().getInputStream());
            if(componentScript==null) {
                failed=true;
                error = "Component does not implement the SimpleScript interface";
                return;
            }
                
            componentScript.render();
        } catch (IOException ex) {
            failed=true;
            error = "Unable to read from component code file. " + ex.getMessage();
            LOG.error(error, ex);
        } catch (ScriptException ex) {
            failed=true;
            error = "Unable to evaluate script." + ex.getMessage();
            LOG.error(error, ex);
        } catch (UndeclaredThrowableException ex) {
            failed=true;
            Throwable t = ex.getUndeclaredThrowable();
            
            
            if(t instanceof PrivilegedActionException) {
                PrivilegedActionException pae = (PrivilegedActionException) t;
                Exception ex2 = pae.getException();
                error = ex2.getMessage();
            } else {
                error = "Unknown error in script. " ;
            }
            LOG.error(error, ex);
        }
    }

    @Override
    public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
        if(failed) {
            LOG.warn("Component failed. Return markup for failure. [componentname={}]", new Object[]{component.getName()});
            return new StringResourceStream("<wicket:panel><div>Component failed to render: "+StringEscapeUtils.escapeHtml(error)+"</div></wicket:panel>");
        } else {
            LOG.debug("Creating resource for component [componentname={}]", new Object[]{component.getName()});
            return new VfsResourceStream(component.getMarkupFile());
        }
    }

    @Override
    public String getCacheKey(MarkupContainer container, Class<?> containerClass) {
        return component.getCodeFile().getPath().toString();
    }


    
}
