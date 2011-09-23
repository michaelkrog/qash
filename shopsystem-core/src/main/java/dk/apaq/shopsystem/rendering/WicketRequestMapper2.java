package dk.apaq.shopsystem.rendering;

import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
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

    private IPageParametersEncoder pageParametersEncoder = new PageParametersEncoder();

    @Override
    protected UrlInfo parseRequest(Request request) {
        Url url = request.getUrl();
		/*if (url.getSegments().size() >= 3 &&
			urlStartsWith(url, getContext().getNamespace(),
				getContext().getBookmarkableIdentifier()))
		{*/
			// try to extract page and component information from URL
			PageComponentInfo info = getPageComponentInfo(url);


			// load the page class
			String pageName = url.getSegments().get(0);

                        // extract the PageParameters from URL if there are any
                        PageParameters pageParameters = extractPageParameters(request, 1,  pageParametersEncoder);
                        
                        return new UrlInfo(info, WicketPage.class, pageParameters);

		//}
		//return null;
    }

    @Override
    protected Url buildUrl(UrlInfo info) {
        Url url = new Url();
		//url.getSegments().add(getContext().getNamespace());
		//url.getSegments().add(getContext().getBookmarkableIdentifier());
		//url.getSegments().add(info.getPageClass().getName());
        url.getSegments().add("john");
       
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
