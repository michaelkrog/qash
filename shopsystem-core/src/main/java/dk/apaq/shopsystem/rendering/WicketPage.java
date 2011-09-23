package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.rendering.simplescript.SimpleScriptComponent;
import dk.apaq.shopsystem.entity.Theme;
import dk.apaq.shopsystem.entity.Template;
import dk.apaq.shopsystem.entity.Module;
import dk.apaq.shopsystem.entity.Component;
import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.context.DataContext;
import dk.apaq.shopsystem.entity.Page;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.crud.ThemeCrud;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.IResourceStream;

/**
 *
 */
public class WicketPage extends WebPage implements IMarkupCacheKeyProvider, IMarkupResourceStreamProvider{

    private Template template;

    public WicketPage(PageParameters pageParameters) {
        Page page = RequestCycle.get().getMetaData(WicketRequestMapper2.PAGE);
        
        OrganisationService service = DataContext.getService();
        Crud<String, Module> modules = service.getModules();
        
        List<String> ids = modules.listIds();
        String id = ids.get(0);
        
        Module module = modules.read(id);
        Component component = module.listComponents().get(0);
        
        SimpleScriptComponent customWicketComponent = new SimpleScriptComponent("placeholder_1", component);
        add(customWicketComponent);
    }

    @Override
    public String getCacheKey(MarkupContainer container, Class<?> containerClass) {
        return "template:AutoPilot:FrontPage";
    }

    @Override
    public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
        OrganisationService service = DataContext.getService();
        Crud<String, Theme> themes = service.getThemes();
        
        List<String> ids = themes.listIds();
        String id = ids.get(0);
        
        Theme theme = themes.read(id);
        
        Template template = theme.listTemplates().get(0);
        return new VfsResourceStream(template.getFile());
    }





}
