package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.rendering.resources.ContentResourceReference;
import dk.apaq.shopsystem.rendering.resources.ThemeResourceReference;
import dk.apaq.shopsystem.service.SystemService;
import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 */
public class CmsApplication extends WebApplication {

    public static final String SYSTEMSITE_PREFIX = "_render";
    
    @Autowired
    private SystemService service;

    private ThemeResourceReference themeResourceReference;
    private ContentResourceReference contentResourceReference;
    
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
        
        themeResourceReference = new ThemeResourceReference(service);
        contentResourceReference = new ContentResourceReference(service);
        
        mount(new CmsPageMapper(service));
        
        mountResource("/_/themes/${themename}", themeResourceReference);
        mountResource("/" + SYSTEMSITE_PREFIX + "/${orgid}/sites/${siteid}/_/themes/${themename}", themeResourceReference);
        
        mountResource("/_/content", contentResourceReference);
        mountResource("/" + SYSTEMSITE_PREFIX + "/${orgid}/sites/${siteid}/_/content", contentResourceReference);
        
        //Removes unneeded wickets tags in renderings output
        getMarkupSettings().setStripWicketTags(true);
        
        //Sets a specific MarkupParserFactory that adds needed filter to the markupparser.
        getMarkupSettings().setMarkupFactory(new CmsMarkupParserFactory());
        

       
    }
}
