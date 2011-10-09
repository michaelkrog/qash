package dk.apaq.shopsystem.rendering;

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
        Website site = CmsUtil.getWebsite(service, request);
        if(site==null) {
            return null;
        }
        
        OrganisationService organisationService = service.getOrganisationService(site.getOrganisation());
        
        Page page = CmsUtil.getPage(service, site, request);
        if(page==null) {
            return null;
        }
        
        CmsPage wp = new CmsPage(organisationService, page, null);
        return new RenderPageRequestHandler(new PageProvider(wp));
    }

    @Override
    public int getCompatibilityScore(Request request) {
        return 1;
    }

    @Override
    public Url mapHandler(IRequestHandler requestHandler) {
        if(requestHandler instanceof RenderPageRequestHandler) {
            IRequestablePage page = ((RenderPageRequestHandler)requestHandler).getPage();
            if(page instanceof CmsPage) {
                Page pageData = ((CmsPage)page).getPageData();
                Url url = new Url();
                url.getSegments().add(pageData.getName());
                return url;
            }
        }
        return null;
    }

    

}
