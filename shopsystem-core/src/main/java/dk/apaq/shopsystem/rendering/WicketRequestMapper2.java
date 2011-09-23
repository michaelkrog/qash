package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.entity.Page;
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

/**
 *
 */
public class WicketRequestMapper2 extends AbstractBookmarkableMapper {

    public static MetaDataKey<Page> PAGE = new MetaDataKey<Page>() { };
    private IPageParametersEncoder pageParametersEncoder = new PageParametersEncoder();

    @Override
    protected UrlInfo parseRequest(Request request) {
        Url url = request.getUrl();
        // try to extract page and component information from URL
        PageComponentInfo info = getPageComponentInfo(url);


        // get page name
        String pageName = url.getSegments().get(0);
        Page page = new Page();
        page.setName(pageName);
        RequestCycle.get().setMetaData(PAGE, page);
        
        // extract the PageParameters from URL if there are any
        PageParameters pageParameters = extractPageParameters(request, 1, pageParametersEncoder);

        return new UrlInfo(info, WicketPage.class, pageParameters);

    }

    @Override
    protected Url buildUrl(UrlInfo info) {
        Url url = new Url();
        Page page = RequestCycle.get().getMetaData(PAGE);
        
        url.getSegments().add(page.getName());

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
}
