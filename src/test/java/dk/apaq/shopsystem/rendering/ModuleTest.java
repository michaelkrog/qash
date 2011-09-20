/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import dk.apaq.vfs.FileSystem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.Date;
import java.util.List;
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
public class ModuleTest {

    public ModuleTest() {
    }

    @Before
    public void setUp() throws IOException {
        FileSystem fs =  service.getFileSystem();
        themeDir = fs.getRoot().getDirectory("Modules", true).getDirectory("Image.module", true);

        //theme.info
        File infoFile = themeDir.getFile("module.info", true);
        OutputStreamWriter infoOsw = new OutputStreamWriter(infoFile.getOutputStream());
        infoOsw.write("{\"version\":\"1.0.0\",  \"releaseDate\":\"2011-01-01\", \"seller\": { \"id\":\"qwerty\", \"name\":\"Apaq\", \"email\": \"mic@apaq.dk\"}, components:{\"SingleImage\":{\"description\":\"A simple component for displaying an image\",  \"parameters\":{ \"path\":{ \"type\":\"String\",  \"default\":\"\",  \"optionalText\":\"The filesystem path for the image\" },  \"title\":{  \"type\":\"String\", \"default\":\"\",  \"optionalText\":\"The title of the image\" },\"styleclass\":{  \"type\":\"String\",  \"default\":\"\", \"optionalText\":\"Special styleclass to append to image\"} }    } } }");
        infoOsw.close();

        //FrontPage.html
        File frontPageFile = themeDir.getFile("SingleImage.code", true);
        OutputStreamWriter frontOsw = new OutputStreamWriter(frontPageFile.getOutputStream());
        frontOsw.write("var test = 1;");
        frontOsw.close();


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
        Module instance = new Module(themeDir);
        assertEquals("1.0.0", instance.getVersion());
        assertNotNull(instance.getReleaseDate());
        assertEquals("qwerty", instance.getSellerInfo().getId());
        assertEquals("Apaq", instance.getSellerInfo().getName());
        assertEquals("mic@apaq.dk", instance.getSellerInfo().getEmail());
        assertEquals(1, instance.listComponents().size());

        assertEquals(String.class, instance.listComponents().get(0).getParamMap().get("path").getType());

    }

    

}