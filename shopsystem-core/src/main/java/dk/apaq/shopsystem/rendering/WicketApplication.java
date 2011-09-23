package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see dk.nykredit.cmstest.Start#main(String[])
 */
public class WicketApplication extends WebApplication {

    @Autowired
    private SystemService service;

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<WicketPage> getHomePage() {
        return WicketPage.class;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();
        // add your configuration here
        setRootRequestMapper(new WicketRequestMapper2());
    }
}
