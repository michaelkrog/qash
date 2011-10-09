package dk.apaq.shopsystem.rendering;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.context.DataContext;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Page;
import dk.apaq.shopsystem.entity.Template;
import dk.apaq.shopsystem.entity.Theme;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.file.IResourceFinder;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
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
        
        setRootRequestMapper(new CmsMapper(service));
        getMarkupSettings().setStripWicketTags(true);
        getMarkupSettings().setMarkupFactory(new CmsMarkupParserFactory());
        

       
    }
}
