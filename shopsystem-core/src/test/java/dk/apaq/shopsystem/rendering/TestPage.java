package dk.apaq.shopsystem.rendering;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.ComponentInformation;
import dk.apaq.shopsystem.entity.ComponentParameter;
import dk.apaq.shopsystem.entity.Domain;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Page;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.util.StreamUtils;
import dk.apaq.vfs.File;
import dk.apaq.vfs.FileSystem;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
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
    private Page page;
    private Organisation org;
    private Website site;

    @Before
    public void setUp() throws IOException {
        String id = service.getOrganisationCrud().create();
        org = service.getOrganisationCrud().read(id);

        OrganisationService orgService = service.getOrganisationService(org);
        site = orgService.getWebsites().read(orgService.getWebsites().create());
        

        Crud.Complete<String, Page> pageCrud = orgService.getPages(site);
        page = pageCrud.read(pageCrud.create());
        page.setName("test");
        page.setThemeName("Basic");
        page.setTemplateName("Simple");
        
        ComponentInformation info = new ComponentInformation();
        info.setModuleName("Standard");
        info.setComponentName("Label");
        info.setPlaceholderName("placeholder_1");

        ComponentParameter cp = new ComponentParameter();
        cp.setString("Hallooo!");
        info.getParameterMap().put("text", cp);
        page.addComponentInformation(info);
        
        info = new ComponentInformation();
        info.setModuleName("Standard");
        info.setComponentName("Image");
        info.setPlaceholderName("placeholder_1");

        cp = new ComponentParameter();
        cp.setString("/Content/monologo.png");
        info.getParameterMap().put("path", cp);
        page.addComponentInformation(info);
        
        pageCrud.update(page);

        Domain domain = orgService.getDomains().read(orgService.getDomains().create());
        domain.setName("coolbiks.dk");
        domain.setWebsite(site);
        orgService.getDomains().update(domain);
        
        File sourceImageFile = (File) service.getFileSystem().getNode("/System/Content/monologo.png");
        FileSystem fs = orgService.getFileSystem();
        File targetImageFile = fs.getRoot().getDirectory("Content", true).getFile("monologo.png", true);
        StreamUtils.copy(sourceImageFile.getInputStream(), targetImageFile.getOutputStream());
                
        WebApplication app = applicationContext.getBean("wicketApplication", WebApplication.class);
        tester = new WicketTester(app);

    }

    @Test
    public void homepageRendersSuccessfully() {

        //start and render the test page
        String url = "http://coolbiks.dk/context/servlet/" + page.getName();
        tester.executeUrl(url);
        tester.assertContains("Basic/style.css");
        
        tester.assertContains("style_small.css");
        
    }
    
    @Test
    public void homepageRendersSuccessfullyLarge() {

        //start and render the test page
        String url = "http://coolbiks.dk/context/servlet/" + page.getName() + "?device.device-width=1000";
        tester.executeUrl(url);
        String response = tester.getLastResponseAsString();
        tester.assertContains("style.css");
        
        tester.assertContainsNot("style_small.css");
    }
    
    @Test
    public void homepageRendersSuccessfullySmall() {

        //start and render the test page
        String url = "http://coolbiks.dk/context/servlet/" + page.getName() + "?device.device-width=400";
        tester.executeUrl(url);
        tester.assertContains("style.css");
        
        tester.assertContains("style_small.css");
    }

    @Test
    public void retrieveStylesheetSuccessfully() {

        tester.executeUrl("http://coolbiks.dk/context/servlet/_/themes/Basic/style.css");
        tester.assertContains("yellow");
    }

    @Test
    public void retrieveFileSuccessfully() throws IOException {

        tester.executeUrl("http://coolbiks.dk/context/servlet/_/content/monologo.png");
        byte[] bytes = tester.getLastResponse().getDocument().getBytes();
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
    }
    
    @Test
    public void renderSystemPageSuccessfully() {

        tester.executeUrl("http://localhost/context/servlet/_api/" + org.getId() + "/sites/" + site.getId() + "/" + page.getName());
        String text = tester.getLastResponseAsString();
        System.out.println(text);

    }
    
        @Test
    public void retrieveSystemStylesheetSuccessfully() {

        tester.executeUrl("http://localhost/context/servlet/_api/" + org.getId() + "/sites/" + site.getId()+"/_/themes/Basic/style.css");
        String text = tester.getLastResponseAsString();
        System.out.println(text);
    }
        
            @Test
    public void retrieveSystemFileSuccessfully() throws IOException {

        tester.executeUrl("http://localhost/context/servlet/_api/" + org.getId() + "/sites/" + site.getId()+"/_/content/monologo.png");
        byte[] bytes = tester.getLastResponse().getDocument().getBytes();
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
    }
    
}
