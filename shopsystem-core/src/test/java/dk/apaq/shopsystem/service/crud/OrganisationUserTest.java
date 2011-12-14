package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.OrganisationUser;
import dk.apaq.shopsystem.service.SystemService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import dk.apaq.shopsystem.entity.SystemUser;
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
            
            systemUser = new SystemUser();
            String userId = service.getSystemUserCrud().create(systemUser);
            systemUser = service.getSystemUserCrud().read(userId);
            
            orgUser = new OrganisationUser();
            orgUser.setUser(systemUser);
            
            String orgOwnerId = orgService.getUsers().create(orgUser);
            orgUser = (OrganisationUser)orgService.getUsers().read(orgOwnerId);
        }
    }

    @After
    public void tearDown() {
    }
    @Autowired
    private SystemService service;
    
    private OrganisationService orgService;

    private Organisation org;
    
    private SystemUser systemUser;
    
    private OrganisationUser orgUser;

    /**
     * Test of create method, of class service.getAccountCrud().
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        Crud.Complete<String, OrganisationUser> crud = orgService.getUsers();

        OrganisationUser result = crud.read(crud.create(new OrganisationUser(systemUser)));
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    /**
     * Test of update method, of class service.getAccountCrud().
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        Crud.Complete<String, OrganisationUser> crud = orgService.getUsers();
        OrganisationUser result = (OrganisationUser) crud.read(crud.create(new OrganisationUser(systemUser)));

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

       
        crud.update(result);

       }

    /**
     * Test of delete method, of class service.getAccountCrud().
     */
    @Test
    public void testDelete() {
        System.out.println("delete");
        Crud.Complete<String, OrganisationUser> crud = orgService.getUsers();
        OrganisationUser organisationUser = new OrganisationUser();
        organisationUser.setUser(systemUser);
        organisationUser = crud.read(crud.create(organisationUser));

        assertNotNull(organisationUser);
        assertNotNull(organisationUser.getId());

        String id = organisationUser.getId();

        organisationUser = crud.read(id);
        assertNotNull(organisationUser);

        crud.delete(id);

        organisationUser = crud.read(id);
        assertNull(organisationUser);
    }

    /**
     * Test of read method, of class service.getAccountCrud().
     */
    @Test
    public void testRead() {
        System.out.println("read");
        Crud.Complete<String, OrganisationUser> crud = orgService.getUsers();
        OrganisationUser organisationUser = new OrganisationUser();
        organisationUser.setUser(systemUser);
        organisationUser = crud.read(crud.create(organisationUser));

        assertNotNull(organisationUser);
        assertNotNull(organisationUser.getId());

        String id = organisationUser.getId();

        assertNotNull(organisationUser);

        crud.delete(id);

        organisationUser = crud.read(id);
        assertNull(organisationUser);
    }

    @Test
    public void testReference() {
        System.out.println("reference");

        Crud.Complete<String, OrganisationUser> crud = orgService.getUsers();

        //Create a systemuser through service
        SystemUser user = service.getSystemUserCrud().read(service.getSystemUserCrud().create());
        user.setName("michael");
        user.getRoles().add("ROLE_ORGADMIN");
        service.getSystemUserCrud().update(user);

        //create af reference through orgservice.
        OrganisationUser userRef = crud.read(crud.create(new OrganisationUser(user)));
        userRef.getRoles().add("ROLE_ORGUSER");
        userRef.setUser(user);
        crud.update(userRef);

        //Check that both are ok and that the reference works.
        assertNotNull(user);
        assertNotNull(userRef);
        assertEquals("michael", user.getName());
        assertEquals("ROLE_ORGADMIN", user.getRoles().get(0));
        assertEquals("ROLE_ORGUSER", userRef.getRoles().get(0));
        
        //delete the reference
        crud.delete(userRef.getId());
        userRef = crud.read(userRef.getId());
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
        Crud.Complete<String, OrganisationUser> crud = orgService.getUsers();

        for (int i = 0; i < 10; i++) {
            crud.create(new OrganisationUser(systemUser));
        }

        List<String> idlist = crud.listIds();
        assertTrue(idlist.size() >= 10);
    }

    
}
