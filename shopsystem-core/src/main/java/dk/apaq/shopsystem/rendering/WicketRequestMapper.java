package dk.apaq.shopsystem.rendering;

import dk.apaq.crud.Crud;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.context.DataContext;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Page;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.util.List;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.AbstractBookmarkableMapper;
import org.apache.wicket.request.mapper.info.ComponentInfo;
import org.apache.wicket.request.mapper.info.PageComponentInfo;
import org.apache.wicket.request.mapper.info.PageInfo;
import org.apache.wicket.request.mapper.parameter.IPageParametersEncoder;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.mapper.parameter.PageParametersEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class WicketRequestMapper extends AbstractBookmarkableMapper {

    private static final Logger LOG = LoggerFactory.getLogger(WicketRequestMapper.class);

    private final SystemService service;
    public static MetaDataKey<Page> PAGE = new MetaDataKey<Page>() { };
    private IPageParametersEncoder pageParametersEncoder = new PageParametersEncoder();

    public WicketRequestMapper(SystemService service) {
        this.service = service;
    }

    @Override
    protected UrlInfo parseRequest(Request request) {
        Url url = request.getUrl();

        boolean systemDomain = isSystemDomain(url);
        if(systemDomain && url.getPath() != null && url.getPath().startsWith("/_sites")) {
            if(url.getSegments().size()<2) {
                return null;
            }

            String siteName = url.getSegments().get(1);
            Website site = null; //Get site from site name
            OrganisationService organisationService = null; //Get orgservice from website and service
            String pageName = null; //
        } else {

        }

        // try to extract page and component information from URL
        PageComponentInfo info = getPageComponentInfo(url);


        // get page name
        String pageName = url.getSegments().isEmpty() ? null : url.getSegments().get(0);

        //Should we read the domains here and add to the RequestCycle instead for det DataContext thingy?
        OrganisationService service = DataContext.getService();
        if(service==null) {
            LOG.debug("No organisation found.");
            return null;
        }
        
        Website site = DataContext.getWebsite();
        if(site==null) {
            LOG.debug("No site found.");
            return null;
        }
        
        Crud.Complete<String, Page> pages = service.getPages(site);
        List<String> pageIds = pages.listIds(new CompareFilter("name", pageName, CompareFilter.CompareType.Equals), null);
        
        if(pageIds.isEmpty()) {
            LOG.debug("No page found.");
            return null;
        }
        
        Page page = pages.read(pageIds.get(0));
        RequestCycle.get().setMetaData(PAGE, page);
        
        // extract the PageParameters from URL if there are any
        PageParameters pageParameters = extractPageParameters(request, 1, pageParametersEncoder);


        return new UrlInfo(info, WicketPage.class, pageParameters);

    }

    @Override
    protected Url buildUrl(UrlInfo info) {
        Url url = new Url();
        Page page = RequestCycle.get().getMetaData(PAGE);
        if(page!=null) {
            url.getSegments().add(page.getName());
        }

        encodePageComponentInfo(url, info.getPageComponentInfo());

        return encodePageParameters(url, info.getPageParameters(), pageParametersEncoder);
    }

    @Override
    protected boolean pageMustHaveBeenCreatedBookmarkable() {
        return false;
    }

    @Override
    public int getCompatibilityScore(Request request) {
        return 0;
    }

    private boolean isSystemDomain(Url url) {
        //TODO Match against a configureable list
        return "qash.dk".equals(url.getHost()) ||
                "www.qash.dk".equals(url.getHost()) ||
                "localhost".equals(url.getHost());
    }
}
