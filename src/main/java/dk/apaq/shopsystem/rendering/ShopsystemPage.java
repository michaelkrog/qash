package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.service.OrganisationService;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.IResourceStream;

/**
 *
 */
public class ShopsystemPage extends WebPage implements IMarkupCacheKeyProvider, IMarkupResourceStreamProvider{

    private Template template;

    public ShopsystemPage(PageParameters pageParameters) {

    }

    @Override
    public String getCacheKey(MarkupContainer container, Class<?> containerClass) {
        return "template:AutoPilot:FrontPage";
    }

    @Override
    public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
        OrganisationService service = ((WicketApplication)getApplication()).getService();
        Template template = service.getThemes().read(service.getThemes().listIds().get(0)).listTemplates().get(0);
        return new VfsResourceStream(template.getFile());
    }





}
