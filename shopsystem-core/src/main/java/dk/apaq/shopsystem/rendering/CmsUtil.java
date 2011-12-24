package dk.apaq.shopsystem.rendering;

import dk.apaq.crud.Crud;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.entity.Document;
import dk.apaq.shopsystem.entity.Domain;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.WebPage;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.util.Arrays;
import java.util.List;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;

/**
 *
 * @author michael
 */
public class CmsUtil {

    //TODO Change this
    private static List<String> systemHosts = Arrays.asList(new String[]{"localhost", "79.125.119.30"});

    /*public static Website getWebsite(SystemService service, String orgId, String siteId) {
    
    Organisation org = service.getOrganisationCrud().read(orgId);
    if (org == null) {
    return null;
    }
    
    Website site = service.getOrganisationService(org).getWebsites().read(siteId);
    return site;
    
    }*/
    public static OrganisationService getOrganisationService(SystemService service, Request request) {
        String host = request.getClientUrl().getHost();
        if (host != null) {

            if (isSystemRequest(request)) {
                Url url = request.getUrl();
                // /_render/<orgid>
                if (url.getSegments().size() < 4) {
                    return null;
                }

                String section1 = url.getSegments().get(0);
                if (!CmsApplication.SYSTEMSITE_PREFIX.equals(section1)) {
                    return null;
                }

                String orgId = url.getSegments().get(1);

                Organisation organisation = service.getOrganisationCrud().read(orgId);
                return service.getOrganisationService(organisation);
            } else {
                dk.apaq.filter.Filter filter = new CompareFilter("name", host, CompareFilter.CompareType.Equals);
                List<String> idlist = service.getDomains().listIds(filter, null);

                if (!idlist.isEmpty()) {
                    Domain domain = service.getDomains().read(idlist.get(0));

                    if (domain.getWebsite() != null) {
                        return service.getOrganisationService(domain.getOrganisation());
                    }
                }
            }

        }
        return null;
    }

    public static Website getWebsite(SystemService service, Request request) {
        String host = request.getClientUrl().getHost();
        if (host != null) {

            if (isSystemRequest(request)) {
                Url url = request.getUrl();
                // /_render/<orgid>/sites/<siteid>
                if (url.getSegments().size() < 4) {
                    return null;
                }

                String section1 = url.getSegments().get(0);
                String section2 = url.getSegments().get(2);
                if (!CmsApplication.SYSTEMSITE_PREFIX.equals(section1) || !"sites".equals(section2)) {
                    return null;
                }

                String orgId = url.getSegments().get(1);
                String siteId = url.getSegments().get(3);

                Organisation organisation = service.getOrganisationCrud().read(orgId);
                Website site = service.getOrganisationService(organisation).getWebsites().read(siteId);
                return site;
            } else {
                dk.apaq.filter.Filter filter = new CompareFilter("name", host, CompareFilter.CompareType.Equals);
                List<String> idlist = service.getDomains().listIds(filter, null);

                if (!idlist.isEmpty()) {
                    Domain domain = service.getDomains().read(idlist.get(0));

                    if (domain.getWebsite() != null) {
                        return domain.getWebsite();
                    }
                }
            }

        }
        return null;
    }

    public static WebPage getWebPage(SystemService service, Website site, String pageName) {
        if (service == null) {
            throw new NullPointerException("Service was null.");
        }

        if (site == null) {
            throw new NullPointerException("Website was null");
        }

        if (pageName == null) {
            throw new NullPointerException("pageName was null");
        }

        OrganisationService orgService = service.getOrganisationService(site.getOrganisation());

        Crud.Complete<String, WebPage> pages = orgService.getPages(site);
        List<String> pageIds = pages.listIds(new CompareFilter("name", pageName, CompareFilter.CompareType.Equals), null);

        if (pageIds.isEmpty()) {
            return null;
        }

        return pages.read(pageIds.get(0));

    }

    public static Document getDocument(SystemService service, Request request) {
        String host = request.getClientUrl().getHost();
        if (host != null && isSystemRequest(request)) {
            Url url = request.getUrl();
            // /_render/<orgid>/documents/<docid>
            if (url.getSegments().size() < 4) {
                return null;
            }

            String section1 = url.getSegments().get(0);
            String section2 = url.getSegments().get(2);
            if (!CmsApplication.SYSTEMSITE_PREFIX.equals(section1) || !"documents".equals(section2)) {
                return null;
            }

            String orgId = url.getSegments().get(1);
            String siteId = url.getSegments().get(3);

            Organisation organisation = service.getOrganisationCrud().read(orgId);
            Document document = service.getOrganisationService(organisation).getDocuments().read(siteId);
            return document;


        }
        return null;
    }

    public static boolean isSystemRequest(Request request) {
        return systemHosts.contains(request.getClientUrl().getHost());
    }
}
