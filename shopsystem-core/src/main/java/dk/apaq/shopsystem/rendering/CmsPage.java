package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.entity.AbstractDocument;
import dk.apaq.shopsystem.entity.Theme;
import dk.apaq.shopsystem.entity.Template;
import dk.apaq.shopsystem.entity.ComponentInformation;
import dk.apaq.shopsystem.entity.Placeholder;
import dk.apaq.shopsystem.rendering.module.CmsModule;
import dk.apaq.shopsystem.service.OrganisationService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.IHeaderContributor;
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
    private final AbstractDocument page;
    private boolean failed;
    private String error;
    
    public CmsPage(OrganisationService organisationService, String contextId, AbstractDocument page, PageParameters pageParameters) {
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
            
            List<org.apache.wicket.Component> components = new ArrayList<org.apache.wicket.Component>();
            
            if (infolist == null || infolist.isEmpty()) {
                
                LOG.debug("No ComponentInformation found for placeholder [placeholder={}]", availablePlaceHolder);
                
                //Add a dummy component
                Label lbl = new Label("placeholder", "Missing component");
                components.add(lbl);
            } else {

                //Compile list of custom components
                for (ComponentInformation info : infolist) {
                    LOG.debug("Retrieving module for ComponentInformation [moduleName={}]", info.getModuleName());
                    CmsModule module = CmsModule.create("placeholder", info.getModuleName());
                    module.setParameters(info.getParameterMap());
                    module.setContextId(contextId);
                    module.add(new ModuleMarkerBehavior(info));
                    module.compose();

                    components.add(module);
                }
            }

            LOG.debug("Adding components to placeholder. [placeholder={};noOfComponents={}]", new Object[]{availablePlaceHolder, components.size()});
            
            //Add list of components to placeholder
            add(new ListView<org.apache.wicket.Component>(availablePlaceHolder.getId(), components) {
                
                @Override
                protected void populateItem(ListItem<org.apache.wicket.Component> item) {
                    item.add(item.getModelObject());
                }
            });
            
        }
        
    }
    
    public AbstractDocument getPageData() {
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
