package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.model.Organisation;
import dk.apaq.shopsystem.service.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
import dk.apaq.shopsystem.model.Store;
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
import static org.junit.Assert.*;

/**
 *
 * @author michaelzachariassenkrog
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
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
    private Service service;

   
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
        Organisation result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result.setName("Apaq");
        //result.getUsers().add("Michael");
        
        service.getOrganisationCrud().update(result);

        result = service.getOrganisationCrud().read(id);

        assertEquals("Apaq", result.getName());
        assertEquals("Michael", result.getUsers().get(0));
        
        


    }

    @Test
    public void testDelete() {
        System.out.println("delete");
        OrganisationCrud crud = service.getOrganisationCrud();
        Organisation result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result = service.getOrganisationCrud().read(id);
        assertNotNull(result);

        service.getOrganisationCrud().delete(id);

        result = service.getOrganisationCrud().read(id);
        assertNull(result);


    }

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

        service.getOrganisationCrud().delete(id);

        result = service.getOrganisationCrud().read(id);
        assertNull(result);
    }

    @Test
    public void testListIds() {
        System.out.println("listIds");
        
        for(int i=0;i<10;i++)
            service.getOrganisationCrud().create();

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

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("Jane", "Doe"));

        try {
            //Not allowed
            service.getOrganisationCrud().read(id);
            fail("Should not be allowed");
        } catch(SecurityException ex) {}

        try {
            //Not allowed
            service.getOrganisationCrud().update(org);
            fail("Should not be allowed");
        } catch(SecurityException ex) {}
    }
    
    

}