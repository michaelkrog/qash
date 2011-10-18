/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.entity.Module;
import dk.apaq.shopsystem.rendering.simplescript.SimpleScriptComponent;
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
        themeDir = fs.getRoot().getDirectory("System", true).getDirectory("Modules", true).getDirectory("Standard", true).getDirectory("Standard.module", true);


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
        assertEquals(2, instance.listComponents().size());
        
        //SimpleScript script = new SimpleScriptComponent(null, null)

        //assertEquals(String.class, instance.listComponents().get(0).getParamMap().get("text").getType());

    }

    

}