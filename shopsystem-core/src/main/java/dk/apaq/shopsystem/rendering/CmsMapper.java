package dk.apaq.shopsystem.rendering;

import dk.apaq.crud.Crud;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.context.DataContext;
import dk.apaq.shopsystem.entity.Domain;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Page;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.util.List;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.IPageProvider;
import org.apache.wicket.request.handler.PageProvider;
import org.apache.wicket.request.handler.RenderPageRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author michael
 */
public class CmsMapper implements IRequestMapper {

    private final SystemService service;

    public CmsMapper(SystemService service) {
        this.service = service;
    }

    @Override
    public IRequestHandler mapRequest(Request request) {
        Website site = getWebsite(request);
        if(site==null) {
            return null;
        }
        
        OrganisationService organisationService = service.getOrganisationService(site.getOrganisation());
        
        Page page = getPage(site, request);
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

    private Website getWebsite(Request request) {
        String host = request.getClientUrl().getHost();
        if (host != null) {
            dk.apaq.filter.Filter filter = new CompareFilter("name", host, CompareFilter.CompareType.Equals);
            List<String> idlist = service.getDomains().listIds(filter, null);

            if (!idlist.isEmpty()) {
                Domain domain = service.getDomains().read(idlist.get(0));

                if (domain.getWebsite() != null) {
                    return domain.getWebsite();
                }
            }
        }
        return null;
    }
    
    private Page getPage(Website site, Request request) {
        OrganisationService orgService = service.getOrganisationService(site.getOrganisation());
        Url url = request.getUrl();
        
        // get page name
        String pageName = url.getSegments().isEmpty() ? null : url.getSegments().get(0);

        Crud.Complete<String, Page> pages = orgService.getPages(site);
        List<String> pageIds = pages.listIds(new CompareFilter("name", pageName, CompareFilter.CompareType.Equals), null);
        
        if(pageIds.isEmpty()) {
            return null;
        }
        
        return pages.read(pageIds.get(0));
        
    }
    

}
