package dk.apaq.shopsystem.rendering;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.handler.IPageProvider;
import org.apache.wicket.request.handler.RenderPageRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 */
public class WicketRequestMapper implements IRequestMapper {

    private class PageProvider implements IPageProvider {

        private String pageName;
        private int renderCount;
        private WicketPage page;
        private PageParameters parameters = new PageParameters();

        public PageProvider(String pageName) {
            this.pageName = pageName;
        }

        public String getPageName() {
            return pageName;
        }
        
        @Override
        public IRequestablePage getPageInstance() {
            if(page==null) {
                page = new WicketPage(parameters);
            }
            return page;
        }

        @Override
        public PageParameters getPageParameters() {
            return parameters;
        }

        @Override
        public boolean isNewPageInstance() {
            return page==null;
        }

        @Override
        public Class<? extends IRequestablePage> getPageClass() {
            return WicketPage.class;
        }

        @Override
        public Integer getPageId() {
            return pageName.hashCode();
        }

        @Override
        public Integer getRenderCount() {
            return renderCount;
        }

        @Override
        public void detach() {
                page = null;
        }

    }

    @Override
    public IRequestHandler mapRequest(Request request) {
        return new RenderPageRequestHandler(new PageProvider(request.getContextPath()));
    }

    @Override
    public int getCompatibilityScore(Request request) {
        return 1;
    }

    @Override
    public Url mapHandler(IRequestHandler requestHandler) {
        List<String> urlSegments = new ArrayList<String>();

        PageProvider pageProvider =(PageProvider) ((RenderPageRequestHandler)requestHandler).getPageProvider();
        urlSegments.add(pageProvider.getPageName());
        return new Url(urlSegments, Charset.defaultCharset());
    }

}
