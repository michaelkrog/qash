package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.rendering.simplescript.SimpleScriptComponent;
import dk.apaq.shopsystem.entity.Theme;
import dk.apaq.shopsystem.entity.Template;
import dk.apaq.shopsystem.entity.Module;
import dk.apaq.shopsystem.entity.Component;
import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.context.DataContext;
import dk.apaq.shopsystem.entity.ComponentInformation;
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
public class CmsPage extends WebPage implements IMarkupCacheKeyProvider, IMarkupResourceStreamProvider{

    private final Template template;
    private final Theme theme;
    private final OrganisationService organisationService;
    private final Page page;
    
    public CmsPage(OrganisationService organisationService, Page page, PageParameters pageParameters) {
        this.page = page;
        this.organisationService = organisationService;
        
        String themeName = page.getThemeName();
        String templateName = page.getTemplateName();
        
        OrganisationService service = DataContext.getService();
        
        theme = service.getThemes().read(themeName);
        template = theme.getTemplate(templateName);
        
        for(String availablePlaceHolder : template.getPlaceHolderIds()) {
            List<ComponentInformation> infolist = page.getComponentInformations(availablePlaceHolder);
            
            if(infolist==null) {
                continue;
            }
            
            for(ComponentInformation info : infolist) {
                Module module = service.getModules().read(info.getModuleName());
                if(module==null) {
                    continue;
                }
                
                Component component = module.getComponent(info.getComponentName());
                if(component==null) {
                    continue;
                }
                
                SimpleScriptComponent customWicketComponent = new SimpleScriptComponent(availablePlaceHolder, component, info.getParameterMap());
                add(customWicketComponent);
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

    public OrganisationService getOrganisationService() {
        return organisationService;
    }

    @Override
    public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
        return new VfsResourceStream(template.getFile());
    }

    @Override
    public String getCacheKey(MarkupContainer container, Class<?> containerClass) {
        return page.getName();
    }





}
