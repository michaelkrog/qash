package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.entity.Component;
import dk.apaq.shopsystem.rendering.simplescript.SimpleScriptComponent;
import dk.apaq.shopsystem.entity.Theme;
import dk.apaq.shopsystem.entity.Template;
import dk.apaq.shopsystem.entity.Module;
import dk.apaq.shopsystem.entity.ComponentInformation;
import dk.apaq.shopsystem.entity.Page;
import dk.apaq.shopsystem.entity.Placeholder;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.rendering.simplescript.SimpleScriptInvoker;
import dk.apaq.shopsystem.rendering.simplescript.SimpleScriptPageRenderer;
import dk.apaq.shopsystem.service.OrganisationService;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.PrivilegedActionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.script.ScriptException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class CmsPage extends WebPage implements IMarkupCacheKeyProvider, IMarkupResourceStreamProvider, IHeaderContributor {
    
    private static final Logger LOG = LoggerFactory.getLogger(CmsPage.class);
    private final Template template;
    private final Theme theme;
    private final Page page;
    private boolean failed;
    private String error;
    
    public CmsPage(OrganisationService organisationService, Website site, Page page, PageParameters pageParameters) {
        super(pageParameters);
        this.page = page;
        
       
        
        String themeName = page.getThemeName();
        String templateName = page.getTemplateName();
        
        LOG.debug("Initializing page. [pageid={}, theme={};template{}]", new Object[]{page.getId(), themeName, templateName});
        
        theme = organisationService.getThemes().read(themeName);
        if (theme == null) {
            throw new WicketRuntimeException("Theme not found.[id=" + themeName + "]");
        }
        
        template = theme.getTemplate(templateName);
        if (theme == null) {
            throw new WicketRuntimeException("Template not found.[id=" + templateName + "]");
        }
        
        
        for (Placeholder availablePlaceHolder : template.getPlaceHolders()) {
            LOG.debug("Loading ComponentInformations for placeholder [placeholder={}]", availablePlaceHolder);
            List<ComponentInformation> infolist = page.getComponentInformations(availablePlaceHolder.getId());
            
            if (infolist == null || infolist.isEmpty()) {
                
                LOG.debug("No ComponentInformation found for placeholder [placeholder={}]", availablePlaceHolder);
                
                //Add a dummy component
                Label lbl = new Label(availablePlaceHolder.getId(), "Missing component");
                add(lbl);
                continue;
            }

            //Compile list of custom components
            List<SimpleScriptComponent> components = new ArrayList<SimpleScriptComponent>();
            for (ComponentInformation info : infolist) {
                LOG.debug("Retrieving module and component for ComponentInformation [moduleName={};componentname={}]", new Object[]{info.getModuleName(), info.getComponentName()});
                Module module = organisationService.getModules().read(info.getModuleName());
                if (module == null) {
                    LOG.debug("Module not found. [moduleName={}]", info.getModuleName());
                    continue;
                }
                
                Component component = module.getComponent(info.getComponentName());
                if (component == null) {
                    LOG.debug("Component not found. [componentName={}]", info.getComponentName());
                    continue;
                }
                
                
                SimpleScriptComponent customWicketComponent = new SimpleScriptComponent(organisationService, site, "placeholder", component, info);
                components.add(customWicketComponent);
            }

            LOG.debug("Adding components to placeholder. [placeholder={};noOfComponents={}]", new Object[]{availablePlaceHolder, components.size()});
            
            //Add list of components to placeholder
            add(new ListView<SimpleScriptComponent>(availablePlaceHolder.getId(), components) {
                
                @Override
                protected void populateItem(ListItem<SimpleScriptComponent> item) {
                    item.add(item.getModelObject());
                }
            });
            
        }
        
    }
    
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (template.getCodeFile() != null) {
            try {
                SimpleScriptPageRenderer renderer = SimpleScriptInvoker.invoke(SimpleScriptPageRenderer.class, new HashMap<String, Object>(), template.getCodeFile().getInputStream());
                renderer.renderHead(response);
            } catch (IOException ex) {
                failed = true;
                error = "Unable to read from component code file. " + ex.getMessage();
                LOG.error(error, ex);
            } catch (ScriptException ex) {
                failed = true;
                error = "Unable to evaluate script." + ex.getMessage();
                LOG.error(error, ex);
            } catch (UndeclaredThrowableException ex) {
                failed = true;
                Throwable t = ex.getUndeclaredThrowable();
                
                
                if (t instanceof PrivilegedActionException) {
                    PrivilegedActionException pae = (PrivilegedActionException) t;
                    Exception ex2 = pae.getException();
                    error = ex2.getMessage();
                } else {
                    error = "Unknown error in script. ";
                }
                LOG.error(error, ex);
            }
        }
    }
    
    public Page getPageData() {
        return page;
    }
    
    public Template getTemplate() {
        return template;
    }
    
    public Theme getTheme() {
        return theme;
    }
    
    @Override
    public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
        if (failed) {
            return new StringResourceStream("<html><body>Page failed to render: " + StringEscapeUtils.escapeHtml(error) + "</body></html>");
        } else {
            return new VfsResourceStream(template.getMarkupFile());
        }
        
    }
    
    @Override
    public String getCacheKey(MarkupContainer container, Class<?> containerClass) {
        return page.getName() + getPageParameters().toString();
    }
}
