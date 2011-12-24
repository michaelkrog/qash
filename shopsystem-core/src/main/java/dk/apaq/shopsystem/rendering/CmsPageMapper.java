package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.entity.AbstractDocument;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.WebPage;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.util.List;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.PageProvider;
import org.apache.wicket.request.mapper.AbstractMapper;
import org.apache.wicket.request.mapper.parameter.IPageParametersEncoder;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.mapper.parameter.PageParametersEncoder;

/**
 *
 * @author michael
 */
public class CmsPageMapper extends AbstractMapper implements IRequestMapper {

    private final SystemService service;
    private final IPageParametersEncoder parametersEncoder = new PageParametersEncoder();

    public CmsPageMapper(SystemService service) {
        this.service = service;
    }

    @Override
    public IRequestHandler mapRequest(Request request) {
        AbstractDocument page = null;
        Website site = null;
        Url url = request.getUrl();
        String contextId = null;
        RenderContextType contextType = null;
        boolean systemRequest = CmsUtil.isSystemRequest(request);
        PageParameters pageParameters = null;

        site = CmsUtil.getWebsite(service, request);
        if (site != null) {

            String name = null;
            int pagePathSegmentCount;

            if (systemRequest) {
                if (url.getSegments().size() <= 4) {
                    name = "ROOT";
                    pagePathSegmentCount = url.getSegments().size();
                } else {
                    name = url.getSegments().get(4);
                    pagePathSegmentCount = 5;
                }
            } else {
                pagePathSegmentCount = url.getSegments().isEmpty() ? 0 : 1;
                name = pagePathSegmentCount == 0 ? "ROOT" : url.getSegments().get(0);
            }
            page = CmsUtil.getWebPage(service, site, name);

            if (page == null) {
                return null;
            }
            
            contextId = site.getId();

            pageParameters = extractPageParameters(request, pagePathSegmentCount, parametersEncoder);
            contextType = RenderContextType.WebPage;
            
        } else if (systemRequest) {
            page = CmsUtil.getDocument(service, request);

            if (page == null) {
                return null;
            }
            
            contextId = page.getId();

            pageParameters = extractPageParameters(request, 5, parametersEncoder);
            contextType = RenderContextType.WebPage;
        } else {
            return null;
        }
        
        OrganisationService organisationService = CmsUtil.getOrganisationService(service, request);
        CmsPage wp = new CmsPage(organisationService, contextId, page, pageParameters);
        return new CmsRenderPageRequestHandler(organisationService.readOrganisation(), contextType, contextId, page, systemRequest, new PageProvider(wp));
    }

    @Override
    public int getCompatibilityScore(Request request) {
        return 1;
    }

    @Override
    public Url mapHandler(IRequestHandler requestHandler) {
        if (requestHandler instanceof CmsRenderPageRequestHandler) {
            Request request = RequestCycle.get().getRequest();
            CmsRenderPageRequestHandler cmsPageHandler = (CmsRenderPageRequestHandler) requestHandler;
            Organisation organisation = cmsPageHandler.getOrganisation();
            AbstractDocument pageData = cmsPageHandler.getPageData();
            Url url = new Url();

            if (cmsPageHandler.isRenderedViaApi()) {
                url.getSegments().add(CmsApplication.SYSTEMSITE_PREFIX);
                url.getSegments().add(organisation.getId());
                url.getSegments().add(cmsPageHandler.getRenderContextType().getPathSegment());
                url.getSegments().add(cmsPageHandler.getContextId());
            }
            
            if(cmsPageHandler.getRenderContextType() == RenderContextType.WebPage) {
                url.getSegments().add(pageData.getName());
            } else {
                url.getSegments().add("index");
            }

            return encodePageParameters(url, cmsPageHandler.getPage().getPageParameters(), parametersEncoder);
        }
        return null;
    }

}
