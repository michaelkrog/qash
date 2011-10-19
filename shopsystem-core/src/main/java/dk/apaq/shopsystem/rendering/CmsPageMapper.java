package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Page;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.handler.PageProvider;
import org.apache.wicket.request.handler.RenderPageRequestHandler;

/**
 *
 * @author michael
 */
public class CmsPageMapper implements IRequestMapper {

    private final SystemService service;

    public CmsPageMapper(SystemService service) {
        this.service = service;
    }

    @Override
    public IRequestHandler mapRequest(Request request) {
        Page page = null;
        Website site = null;
        Url url = request.getUrl();
        
        site = CmsUtil.getWebsite(service, request);
        if(site==null) {
            return null;
        }
        
        String name = null;
        boolean systemRequest = CmsUtil.isSystemRequest(request);
        
        if (systemRequest) {
            name = url.getSegments().size()<=4 ? "ROOT" : url.getSegments().get(4);
        } else {
            name = url.getSegments().isEmpty() ? "ROOT" : url.getSegments().get(0);
        }
        
        page = CmsUtil.getPage(service, site, name);

        if (page == null) {
            return null;
        }

        OrganisationService organisationService = service.getOrganisationService(site.getOrganisation());
        CmsPage wp = new CmsPage(organisationService, page, null);
        return new CmsRenderPageRequestHandler(site, page, systemRequest, new PageProvider(wp));
    }

    @Override
    public int getCompatibilityScore(Request request) {
        return 1;
    }

    @Override
    public Url mapHandler(IRequestHandler requestHandler) {
        if (requestHandler instanceof CmsRenderPageRequestHandler) {
            CmsRenderPageRequestHandler cmsPageHandler = (CmsRenderPageRequestHandler) requestHandler;
            Website site = cmsPageHandler.getWebsite();
            Page pageData = cmsPageHandler.getPageData();
            Url url = new Url();
            
            if(cmsPageHandler.isRenderedViaApi()) {
                url.getSegments().add("_api");
                url.getSegments().add(site.getOrganisation().getId());
                url.getSegments().add("sites");
                url.getSegments().add(site.getId());
                
            }
                
            url.getSegments().add(pageData.getName());
            
            return url;

        }
        return null;
    }

}
