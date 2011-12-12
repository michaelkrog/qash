package dk.apaq.shopsystem.service;

import dk.apaq.crud.Crud.Complete;
import dk.apaq.crud.Crud.Filterable;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;
import dk.apaq.vfs.FileSystem;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author michael
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
public class SystemServiceImplTest {
    
    @Autowired
    SystemService service;
    
    @Autowired
    MockMailSender mailSender;
    
/*
    @Test
    public void testSetFileSystemPopulator() {
        System.out.println("setFileSystemPopulator");
        FileSystemPopulator populator = null;
        SystemServiceImpl instance = new SystemServiceImpl();
        instance.setFileSystemPopulator(populator);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetOrganisationService() {
        System.out.println("getOrganisationService");
        Organisation org = null;
        SystemServiceImpl instance = new SystemServiceImpl();
        OrganisationService expResult = null;
        OrganisationService result = instance.getOrganisationService(org);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetOrganisationCrud() {
        System.out.println("getOrganisationCrud");
        SystemServiceImpl instance = new SystemServiceImpl();
        OrganisationCrud expResult = null;
        OrganisationCrud result = instance.getOrganisationCrud();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSystemUserCrud() {
        System.out.println("getSystemUserCrud");
        SystemServiceImpl instance = new SystemServiceImpl();
        Complete expResult = null;
        Complete result = instance.getSystemUserCrud();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetFileSystem() {
        System.out.println("getFileSystem");
        SystemServiceImpl instance = new SystemServiceImpl();
        FileSystem expResult = null;
        FileSystem result = instance.getFileSystem();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetDomains() {
        System.out.println("getDomains");
        SystemServiceImpl instance = new SystemServiceImpl();
        Filterable expResult = null;
        Filterable result = instance.getDomains();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetApplicationContext() {
        System.out.println("setApplicationContext");
        ApplicationContext applicationContext = null;
        SystemServiceImpl instance = new SystemServiceImpl();
        instance.setApplicationContext(applicationContext);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    @Test
    public void testCreateOrganisation() {
        System.out.println("createOrganisation");
        
        mailSender.reset();
        
        SystemUser user = new SystemUser();
        user.setName("john");
        user.setPassword("doe");
        user.setEmail("john@doe.com");
        
        Organisation organisation = new Organisation();
        organisation.setCompanyName("Apaq");
        
        
        Organisation result = service.createOrganisation(user, organisation);
        
        assertTrue(mailSender.isMailSend());
        
    }
}
