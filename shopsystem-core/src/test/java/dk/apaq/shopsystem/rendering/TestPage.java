package dk.apaq.shopsystem.rendering;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.context.DataContext;
import dk.apaq.shopsystem.entity.ComponentInformation;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Page;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Simple test using the WicketTester
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/defaultspringcontext.xml"})
public class TestPage extends AbstractJUnit4SpringContextTests {

    private WicketTester tester;
    
    @Autowired
    private SystemService service;

    @Before
    public void setUp() {
        String id = service.getOrganisationCrud().create();
        Organisation org = service.getOrganisationCrud().read(id);
        DataContext.setService(service.getOrganisationService(org));
        
        OrganisationService orgService = service.getOrganisationService(org);
        Website site = orgService.getWebsites().read(orgService.getWebsites().create());
        DataContext.setWebsite(site);
        
        Crud.Complete<String, Page> pageCrud = orgService.getPages(site);
        Page page = pageCrud.read(pageCrud.create());
        page.setName("test");
        page.setThemeName("Basic");
        page.setTemplateName("Simple");
        ComponentInformation info = new ComponentInformation();
        info.setComponentName("SingleImage");
        info.setModuleName("Image");
        info.setPlaceholderName("placeholder_1");
        page.addComponentInformation(info);
        pageCrud.update(page);
        
        WebApplication app = applicationContext.getBean("wicketApplication", WebApplication.class);
        tester = new WicketTester(app);
    }

    @Test
    public void homepageRendersSuccessfully() {
        
        //start and render the test page
        tester.executeUrl("http://localhost:8080/context/servlet/test");
        //tester.startPage(WicketPage.class);

        //assert rendered page class
        //tester.assertRenderedPage(WicketPage.class);
    }
}
