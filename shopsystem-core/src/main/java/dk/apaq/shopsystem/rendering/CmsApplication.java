package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.service.SystemService;
import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 */
public class CmsApplication extends WebApplication {

    @Autowired
    private SystemService service;

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<CmsPage> getHomePage() {
        return CmsPage.class;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();
        
        //Lets all wicket requests go through the CmsMapper
        //setRootRequestMapper(new CmsMapper(service));
        mount(new CmsPageMapper(service));
        mountResource("/_themes/${themename}", new ThemeResourceReference(service));
        
        //Removes unneeded wickets tags in renderings output
        getMarkupSettings().setStripWicketTags(true);
        
        //Sets a specific MarkupParserFactory that adds needed filter to the markupparser.
        getMarkupSettings().setMarkupFactory(new CmsMarkupParserFactory());
        

       
    }
}
