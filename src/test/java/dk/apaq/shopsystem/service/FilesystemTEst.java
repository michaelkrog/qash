package dk.apaq.shopsystem.service;

import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemException;
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

    @Test
    public void testFoldersExists() throws FileSystemException {
        FileSystem fs = service.getFileSystem();
        assertTrue(fs.getRoot().resolveFile("System/Modules/Standard").exists());
        assertTrue(fs.getRoot().resolveFile("System/Modules/Optional").exists());
        assertTrue(fs.getRoot().resolveFile("System/Templates/Standard").exists());
        assertTrue(fs.getRoot().resolveFile("System/Templates/Optional").exists());
        assertTrue(fs.getRoot().resolveFile("Organisations").exists());
    }
    
    @Test
    public void testOrgansiationFoldersExists() throws FileSystemException {
        OrganisationCrud crud = service.getOrganisationCrud();
        Organisation o = crud.read(crud.create());
        
        OrganisationService os = service.getOrganisationService(o);
        
        FileSystem fs = os.getFileSystem();
        assertTrue(fs.getRoot().resolveFile("Modules").exists());
        assertTrue(fs.getRoot().resolveFile("Templates").exists());
    }
}
