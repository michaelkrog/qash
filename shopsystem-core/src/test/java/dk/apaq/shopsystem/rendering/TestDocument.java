package dk.apaq.shopsystem.rendering;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.ComponentInformation;
import dk.apaq.shopsystem.entity.Document;
import dk.apaq.shopsystem.entity.Domain;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.WebPage;
import dk.apaq.shopsystem.entity.Template;
import dk.apaq.shopsystem.entity.Theme;
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
public class TestDocument extends AbstractJUnit4SpringContextTests {

    private WicketTester tester;
    @Autowired
    private SystemService service;
    private Document document;
    private Organisation org;
    
    @Before
    public void setUp() throws IOException, InterruptedException {
        WebApplication app = applicationContext.getBean("wicketApplication", WebApplication.class);
        tester = new WicketTester(app);
        
        String id = service.getOrganisationCrud().create();
        org = service.getOrganisationCrud().read(id);

        OrganisationService orgService = service.getOrganisationService(org);
        
        Theme theme = orgService.getThemes().read("Basic");
        Template template = theme.getTemplate("Simple");

        Crud.Complete<String, Document> docCrud = orgService.getDocuments();
        document = docCrud.read(docCrud.create());
        document.setName("test");
        document.setThemeName(theme.getName());
        document.setTemplateName(template.getName());
        
        for(ComponentInformation ci : template.getDefaultComponentInformations()) {
            document.addComponentInformation(ci);
        }
        
        docCrud.update(document);

        File sourceImageFile = (File) service.getFileSystem().getNode("/System/Content/monologo.png");
        FileSystem fs = orgService.getFileSystem();
        File targetImageFile = fs.getRoot().getDirectory("Content", true).getFile("monologo.png", true);
        StreamUtils.copy(sourceImageFile.getInputStream(), targetImageFile.getOutputStream());
                


    }

    
    
    
    @Test
    public void renderSystemPageSuccessfully() {

        tester.executeUrl("http://localhost/context/servlet/_render/" + org.getId() + "/documents/" + document.getId() + "/index");
        String text = tester.getLastResponseAsString();
        tester.assertContains("style.css");
        tester.assertContains("style_small.css");
        tester.assertContains("<img class=\"cms-image\"");
        tester.assertContainsNot("<cms:component");
        tester.assertContainsNot("</cms:component");
    }
    
        @Test
    public void retrieveSystemStylesheetSuccessfully() {

        tester.executeUrl("http://localhost/context/servlet/_render/" + org.getId() + "/documents/" + document.getId() + "/_/themes/Basic/style.css");
        String text = tester.getLastResponseAsString();
        tester.assertContains("body");
    }

        
    @Test
    public void retrieveSystemFileSuccessfully() throws IOException {

        tester.executeUrl("http://localhost/context/servlet/_render/" + org.getId() + "/documents/" + document.getId() + "/_/content/monologo.png");
        byte[] bytes = tester.getLastResponse().getDocument().getBytes();
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
    }
    
}
