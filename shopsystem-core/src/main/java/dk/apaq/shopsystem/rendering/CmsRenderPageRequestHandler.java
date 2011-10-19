package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.entity.Page;
import dk.apaq.shopsystem.entity.Website;
import org.apache.wicket.request.handler.IPageProvider;
import org.apache.wicket.request.handler.RenderPageRequestHandler;

/**
 *
 * @author michael
 */
public class CmsRenderPageRequestHandler extends RenderPageRequestHandler {

    private final Website website;
    private final Page pageData;
    private boolean renderedViaApi;

    public CmsRenderPageRequestHandler(Website website, Page pageData, boolean renderedViaApi, IPageProvider pageProvider) {
        super(pageProvider);
        this.website = website;
        this.pageData = pageData;
        this.renderedViaApi = renderedViaApi;
    }

    public CmsRenderPageRequestHandler(Website website, Page pageData, boolean renderedViaApi, IPageProvider pageProvider, RedirectPolicy redirectPolicy) {
        super(pageProvider, redirectPolicy);
        this.website = website;
        this.pageData = pageData;
        this.renderedViaApi = renderedViaApi;
    }

    public Page getPageData() {
        return pageData;
    }

    public Website getWebsite() {
        return website;
    }

    public boolean isRenderedViaApi() {
        return renderedViaApi;
    }
    
    
}
