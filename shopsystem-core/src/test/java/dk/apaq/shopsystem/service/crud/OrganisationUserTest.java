package dk.apaq.shopsystem.service.crud;

import dk.apaq.filter.Filter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.core.LikeFilter;
import dk.apaq.shopsystem.entity.BaseUser;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.SystemService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.entity.SystemUserReference;
import dk.apaq.shopsystem.service.OrganisationService;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;

/**
 *
 * @author michaelzachariassenkrog
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/defaultspringcontext.xml"})
public class OrganisationUserTest {

    public OrganisationUserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("michael.krog", "Doe"));

        if(orgService==null)
        {
            String orgid = service.getOrganisationCrud().create();
            org = service.getOrganisationCrud().read(orgid);
            orgService = service.getOrganisationService(org);
        }
    }

    @After
    public void tearDown() {
    }
    @Autowired
    private SystemService service;
    
    private OrganisationService orgService;

    private Organisation org;

    /**
     * Test of create method, of class service.getAccountCrud().
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        UserCrud crud = orgService.getUsers();

        BaseUser result = crud.read(crud.createSystemUser());
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    /**
     * Test of update method, of class service.getAccountCrud().
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        UserCrud crud = orgService.getUsers();
        SystemUser result = (SystemUser) crud.read(crud.createSystemUser());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result.setName("michael.krog");
        result.setDisplayName("Michael Krog");

        crud.update(result);

        result = (SystemUser) crud.read(id);

        assertEquals("michael.krog", result.getName());
        assertEquals("Michael Krog", result.getDisplayName());
    }

    /**
     * Test of delete method, of class service.getAccountCrud().
     */
    @Test
    public void testDelete() {
        System.out.println("delete");
        UserCrud crud = orgService.getUsers();
        SystemUser result = (SystemUser) crud.read(crud.createSystemUser());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result = (SystemUser) crud.read(id);
        assertNotNull(result);

        crud.delete(id);

        result = (SystemUser) crud.read(id);
        assertNull(result);
    }

    /**
     * Test of read method, of class service.getAccountCrud().
     */
    @Test
    public void testRead() {
        System.out.println("read");
        UserCrud crud = orgService.getUsers();
        SystemUser result = (SystemUser) crud.read(crud.createSystemUser());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result = (SystemUser) crud.read(id);
        assertNotNull(result);

        crud.delete(id);

        result = (SystemUser) crud.read(id);
        assertNull(result);
    }

    @Test
    public void testReference() {
        System.out.println("reference");

        UserCrud crud = orgService.getUsers();

        //Create a systemuser through service
        SystemUser user = service.getSystemUserCrud().read(service.getSystemUserCrud().create());
        user.setName("michael");
        user.getRoles().add("ROLE_ORGADMIN");
        service.getSystemUserCrud().update(user);

        //create af reference through orgservice.
        SystemUserReference userRef = (SystemUserReference) crud.read(crud.createSystemUserReference(user));
        userRef.getRoles().add("ROLE_ORGUSER");
        crud.update(userRef);

        //Check that both are ok and that the reference works.
        assertNotNull(user);
        assertNotNull(userRef);
        assertEquals("michael", user.getName());
        assertEquals("michael", userRef.getName());
        assertEquals("ROLE_ORGADMIN", user.getRoles().get(0));
        assertEquals("ROLE_ORGUSER", userRef.getRoles().get(0));
        
        //delete the reference
        crud.delete(userRef.getId());
        userRef = (SystemUserReference) crud.read(userRef.getId());
        assertNull(userRef);

        //Ensure the original user is not deleted.
        user = service.getSystemUserCrud().read(user.getId());
        assertNotNull(user);

    }

    /**
     * Test of listIds method, of class service.getAccountCrud().
     */
    @Test
    public void testListIds() {
        System.out.println("listIds");
        UserCrud crud = orgService.getUsers();

        for (int i = 0; i < 10; i++) {
            crud.createSystemUser();
        }

        List<String> idlist = crud.listIds();
        assertTrue(idlist.size() >= 10);
    }

    @Test
    public void testListIdsWithParams() {
        System.out.println("listIds");
        UserCrud crud = orgService.getUsers();
            
        for (int i = 0; i < 10; i++) {
            SystemUser account = (SystemUser) crud.read(crud.createSystemUser());
            account.setName(Integer.toString(i));
            account.setEmail(i + "@gmail.com");
            crud.update(account);
        }

        Filter filter = new CompareFilter("name", "5", CompareFilter.CompareType.Equals);
        List<String> idlist = crud.listIds(filter, null, null);
        assertEquals(1, idlist.size());

        filter = new LikeFilter("email", "*@gmail.com");
        idlist = crud.listIds(filter, null, null);
        assertEquals(10, idlist.size());

    }

    /*@Test
    public void testSecurity() {
        UserCrud crud = orgService.getUsers();
        SystemUser user = crud.read(crud.create());
        String id = user.getId();
        user.setName(SecurityContextHolder.getContext().getAuthentication().getName());
        service.getSystemUserCrud().update(user);

        //Allowed
        service.getOrganisationCrud().read(id);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("Jane", "Doe"));


        try {
            //Not allowed
            service.getSystemUserCrud().update(user);
            fail("Should not be allowed");
        }
        catch (SecurityException ex) {
        }
    }*/
}
