package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.rendering.resources.ContentResourceReference;
import dk.apaq.shopsystem.rendering.resources.ThemeResourceReference;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import javax.servlet.http.HttpServletResponse;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebResponse;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 */
public class CmsApplication extends WebApplication {

    public static final String SYSTEMSITE_PREFIX = "_render";
    private static final String[] botAgents = {	
			"googlebot", "msnbot", "slurp", "jeeves"
	/*
	 * "appie", "architext", "jeeves", "bjaaland", "ferret", "gulliver",
	 * "harvest", "htdig", "linkwalker", "lycos_", "moget", "muscatferret",
	 * "myweb", "nomad", "scooter", "yahoo!\\sslurp\\schina", "slurp",
	 * "weblayers", "antibot", "bruinbot", "digout4u", "echo!", "ia_archiver",
	 * "jennybot", "mercator", "netcraft", "msnbot", "petersnews",
	 * "unlost_web_crawler", "voila", "webbase", "webcollage", "cfetch",
	 * "zyborg", "wisenutbot", "robot", "crawl", "spider"
	 */};	

    
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
        getMarkupSettings().setCompressWhitespace(true);
        
        //Sets a specific MarkupParserFactory that adds needed filter to the markupparser.
        getMarkupSettings().setMarkupFactory(new CmsMarkupParserFactory());
       
    }

    public ContentResourceReference getContentResourceReference() {
        return contentResourceReference;
    }

    public ThemeResourceReference getThemeResourceReference() {
        return themeResourceReference;
    }
    
    @Override
    protected WebResponse newWebResponse(final WebRequest webRequest, HttpServletResponse httpServletResponse) {
        return new ServletWebResponse((ServletWebRequest)webRequest, httpServletResponse) {

          @Override
          public String encodeURL(CharSequence url) {
              final String agent = webRequest.getHeader("User-Agent");
              return isAgent(agent) || isGuiEditor(agent)? url.toString() : super.encodeURL(url);
          }
      };
    }
    
    public static boolean isAgent(final String agent) {
	if (agent != null) {
		final String lowerAgent = agent.toLowerCase();
		for (final String bot : botAgents) {
			if (lowerAgent.indexOf(bot) != -1) {
				return true;
			}
		}
	}
	return false;
}
    
    public static boolean isGuiEditor(final String agent) {
        return agent.contains("CmsGuiEditor");
    }
    
    public OrganisationService getOrgansiationService() {
        return CmsUtil.getOrganisationService(service, RequestCycle.get().getRequest());
    }
    
}
