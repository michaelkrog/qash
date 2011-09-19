package dk.apaq.shopsystem.service;

import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.FileSystem;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 *
 * @author michael
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
public class FilesystemTest {
    
    public FilesystemTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Autowired
    private SystemService service;


    /*************** CANNOT DO THE TEST UNTIL JAVAVFS HAS SUPPORT FOR FILESYSTEM IN RAM ************/
    @Test
    public void testFoldersExists() throws IOException {
        /*FileSystem fs = service.getFileSystem();
        Directory root = fs.getRoot();

        root.getDirectory("System").getDirectory("Modules").getDirectory("Standard");
        root.getDirectory("System").getDirectory("Modules").getDirectory("Optional");
        root.getDirectory("System").getDirectory("Templates").getDirectory("Standard");
        root.getDirectory("System").getDirectory("Templates").getDirectory("Optional");
        root.getDirectory("Organisations");*/
    }
    
    @Test
    public void testOrgansiationFoldersExists() throws IOException {
        /*OrganisationCrud crud = service.getOrganisationCrud();
        Organisation o = crud.read(crud.create());
        
        OrganisationService os = service.getOrganisationService(o);
        
        FileSystem fs = os.getFileSystem();
        fs.getRoot().getDirectory("Modules");
        fs.getRoot().getDirectory("Templates");*/
    }
}
