package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.entity.Theme;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.FileSystem;
import java.io.IOException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;

/**
 *
 * @author krog
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
public class ThemeTest {

    public ThemeTest() {
    }

    @Before
    public void setUp() throws IOException {
        FileSystem fs =  service.getFileSystem();
        themeDir = fs.getRoot().getDirectory("System").getDirectory("Themes").getDirectory("Standard").getDirectory("Basic.theme");

        //theme.info
        /*File infoFile = themeDir.getFile("theme.info", true);
        OutputStreamWriter infoOsw = new OutputStreamWriter(infoFile.getOutputStream());
        infoOsw.write("{\"version\":\"1.0.0\",  \"releaseDate\":\"2011-01-01\", \"seller\": { \"id\":\"qwerty\", \"name\":\"Apaq\", \"email\": \"mic@apaq.dk\"}, templates:{\"FrontPage\":{ \"description\":\"A nice landing page template\" } }    }");
        infoOsw.close();

        //FrontPage.html
        File frontPageFile = themeDir.getFile("FrontPage.html", true);
        OutputStreamWriter frontOsw = new OutputStreamWriter(frontPageFile.getOutputStream());
        frontOsw.write("<html><body></body></html>");
        frontOsw.close();*/


    }

    @After
    public void tearDown() {
    }

    @Autowired
    private SystemService service;
    private Directory themeDir;
    
    /**
     * Test of getVersion method, of class Theme.
     */
    @Test
    public void testGetters() throws IOException {
        System.out.println("getters");
        Theme instance = new Theme(themeDir);
        assertEquals("1.0.0", instance.getVersion());
        assertNotNull(instance.getReleaseDate());
        assertEquals("qwerty", instance.getSellerInfo().getId());
        assertEquals("Apaq", instance.getSellerInfo().getName());
        assertEquals("mic@apaq.dk", instance.getSellerInfo().getEmail());
        assertEquals(1, instance.listTemplates().size());
        
        assertEquals("Simple", instance.listTemplates().get(0).getName());
        assertEquals(2, instance.listTemplates().get(0).getDefaultComponentInformationCount());
        

    }

    

}