package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.entity.AbstractDocument;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.WebPage;
import dk.apaq.shopsystem.entity.Website;
import org.apache.wicket.request.handler.IPageProvider;
import org.apache.wicket.request.handler.RenderPageRequestHandler;

/**
 *
 * @author michael
 */
public class CmsRenderPageRequestHandler extends RenderPageRequestHandler {

    private final Organisation organisation;
    private RenderContextType renderContextType;
    private String contextId;
    private final AbstractDocument pageData;
    private boolean renderedViaApi;

    public CmsRenderPageRequestHandler(Organisation organisation, RenderContextType renderContextType, String contextId, AbstractDocument pageData, boolean renderedViaApi, IPageProvider pageProvider) {
        super(pageProvider);
        this.organisation = organisation;
        this.renderContextType = renderContextType;
        this.contextId = contextId;
        this.pageData = pageData;
        this.renderedViaApi = renderedViaApi;
    }

    public CmsRenderPageRequestHandler(Organisation organisation, RenderContextType renderContextType, String contextId, AbstractDocument pageData, boolean renderedViaApi, IPageProvider pageProvider, RedirectPolicy redirectPolicy) {
        super(pageProvider, redirectPolicy);
        this.organisation = organisation;
        this.renderContextType = renderContextType;
        this.contextId = contextId;
        this.pageData = pageData;
        this.renderedViaApi = renderedViaApi;
    }

    public AbstractDocument getPageData() {
        return pageData;
    }

    public Organisation getOrganisation() {
        return organisation;
    }
    
    public RenderContextType getRenderContextType() {
        return renderContextType;
    }

    public String getContextId() {
        return contextId;
    }

    public boolean isRenderedViaApi() {
        return renderedViaApi;
    }
    
    
}
