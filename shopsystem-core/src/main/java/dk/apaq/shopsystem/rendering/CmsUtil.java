package dk.apaq.shopsystem.rendering;

import dk.apaq.crud.Crud;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.entity.Domain;
import dk.apaq.shopsystem.entity.Page;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.util.List;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;

/**
 *
 * @author michael
 */
public class CmsUtil {
 
    public static Website getWebsite(SystemService service, Request request) {
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
    
    public static Page getPage(SystemService service, Website site, Request request) {
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
