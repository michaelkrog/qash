package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.entity.Page;
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
        Page page = null;
        Website site = null;
        Url url = request.getUrl();

        site = CmsUtil.getWebsite(service, request);
        if (site == null) {
            return null;
        }

        String name = null;
        boolean systemRequest = CmsUtil.isSystemRequest(request);
        int pagePathSegmentCount;

        if (systemRequest) {
            if(url.getSegments().size() <= 4) {
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
        
        page = CmsUtil.getPage(service, site, name);

        if (page == null) {
            return null;
        }

        PageParameters pageParameters = extractPageParameters(request, pagePathSegmentCount, parametersEncoder);
        OrganisationService organisationService = service.getOrganisationService(site.getOrganisation());
        CmsPage wp = new CmsPage(organisationService, page, pageParameters);
        return new CmsRenderPageRequestHandler(site, page, systemRequest, new PageProvider(wp));
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
            Website site = cmsPageHandler.getWebsite();
            Page pageData = cmsPageHandler.getPageData();
            Url url = new Url();

            if (cmsPageHandler.isRenderedViaApi()) {
                url.getSegments().add("_api");
                url.getSegments().add(site.getOrganisation().getId());
                url.getSegments().add("sites");
                url.getSegments().add(site.getId());
            }

            url.getSegments().add(pageData.getName());

            return encodePageParameters(url, cmsPageHandler.getPage().getPageParameters(), parametersEncoder);
        }
        return null;
    }
}
