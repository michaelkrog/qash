package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.SystemService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
import dk.apaq.shopsystem.entity.Store;
import dk.apaq.shopsystem.entity.SystemUser;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;

/**
 *
 * @author michaelzachariassenkrog
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
@Transactional
public class OrganisationCrudTest extends AbstractJUnit4SpringContextTests  {

    public OrganisationCrudTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("John", "Doe"));
    }

    @After
    public void tearDown() {
    }

    @Autowired
    private SystemService service;

   
    @Test
    public void testCreate() {
        System.out.println("create");
        OrganisationCrud crud = service.getOrganisationCrud();
        Organisation result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());


        
    }

    @Test
    public void testUpdate() {
        System.out.println("update");
        OrganisationCrud crud = service.getOrganisationCrud();
        SystemUser user = new SystemUser();
        user.setName("john");
        user.setPassword("doe");

        Organisation org = new Organisation();
        Organisation result = service.createOrganisation(user, org);// crud.read(crud.create());

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("john", "doe"));


        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result.setCompanyName("Apaq");
        //result.getUsers().add("Michael");
        
        service.getOrganisationCrud().update(result);

        result = service.getOrganisationCrud().read(id);

        assertEquals("Apaq", result.getCompanyName());
//        assertEquals("Michael", result.getUsers().get(0));
        
        


    }

    /*@Test
    public void testDelete() {
        System.out.println("delete");
        OrganisationCrud crud = service.getOrganisationCrud();
        SystemUser user = new SystemUser();
        user.setName("john");
        user.setPassword("doe");

        Organisation org = new Organisation();
        Organisation result = service.createOrganisation(user, org);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("john", "doe"));

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result = service.getOrganisationCrud().read(id);
        assertNotNull(result);

        service.getOrganisationCrud().delete(id);

        result = service.getOrganisationCrud().read(id);
        assertNull(result);


    }*/

    @Test
    public void testRead() {
        System.out.println("read");
        OrganisationCrud crud = service.getOrganisationCrud();
        Organisation result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result = service.getOrganisationCrud().read(id);
        assertNotNull(result);

        
    }

    @Test
    public void testListIds() {
        System.out.println("listIds");
        
        for(int i=0;i<10;i++) {
            SystemUser user = new SystemUser();
            user.setName("john" + i);
            user.setPassword("doe");

            Organisation org = new Organisation();
            Organisation result = service.createOrganisation(user, org);
        }

        List<String> idlist = service.getOrganisationCrud().listIds();
        assertTrue(idlist.size() >= 10 );
    }

    @Test
    public void testSecurity() {
        OrganisationCrud crud = service.getOrganisationCrud();
        Organisation org = crud.read(crud.create());
        String id = org.getId();

        //Allowed
        service.getOrganisationCrud().read(id);

        List<Organisation> orgList = service.getOrganisationCrud().list();
        assertFalse(orgList.isEmpty());

        List<String> orgIdList = service.getOrganisationCrud().listIds();
        assertFalse(orgIdList.isEmpty());

        SystemUser user = new SystemUser();
        user.setName("jane");
        user.setPassword("doe");
        user = service.getSystemUserCrud().read(service.getSystemUserCrud().create(user));

        
        //Allowed
        service.getOrganisationCrud().read(id);
        
        try {
            //Not allowed
            service.getOrganisationCrud().update(org);
            fail("Should not be allowed");
        } catch(SecurityException ex) {}

        orgList = service.getOrganisationCrud().list(null, user);
        assertEquals(0, orgList.size());

        orgIdList = service.getOrganisationCrud().listIds(null, user);
        assertEquals(0, orgIdList.size());
    }
    
    

}