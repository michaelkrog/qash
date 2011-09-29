package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.Organisation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import dk.apaq.shopsystem.entity.Store;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.entity.Tax;
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
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
public class WebsiteCrudTest {

    public WebsiteCrudTest() {
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
    public void testRead() {
        System.out.println("read");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Editable<String, Tax> crud = service.getOrganisationService(org).getTaxes();
        Tax result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result = crud.read(id);
        assertNotNull(result);

        crud.delete(id);

        result = crud.read(id);
        assertNull(result);
    }

     @Test
    public void testListIds() {
        System.out.println("listIds");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Editable<String, Tax> crud = service.getOrganisationService(org).getTaxes();

        for(int i=0;i<10;i++)
            crud.create();

        List<String> idlist = crud.listIds();
        assertTrue(idlist.size() >= 10);
    }

    @Test
    public void testCreate() {
        System.out.println("create");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Editable<String, Tax> crud = service.getOrganisationService(org).getTaxes();
        Tax tax = crud.read(crud.create());

        assertNotNull(tax);
    }

    @Test
    public void testUpdate() {
        System.out.println("update");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Editable<String, Tax> crud = service.getOrganisationService(org).getTaxes();
        Tax result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result.setName("Moms");
        result.setRate(25.0);
        crud.update(result);

        result = crud.read(id);
        assertEquals("Moms", result.getName());
        assertEquals(25.0, result.getRate(), 0.3);
    }

    @Test
    public void testDelete() {
        System.out.println("delete");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Editable<String, Tax> crud = service.getOrganisationService(org).getTaxes();
        Tax result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result = crud.read(id);
        assertNotNull(result);

        crud.delete(id);

        result = crud.read(id);
        assertNull(result);
    }

    @Test
    public void testSecurity() {
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org1 = orgcrud.read(orgcrud.create());
        Organisation org2 = orgcrud.read(orgcrud.create());

        Crud.Editable<String, Tax> taxCrud1 = service.getOrganisationService(org1).getTaxes();
        Crud.Editable<String, Tax> taxCrud2 = service.getOrganisationService(org2).getTaxes();

        Tax tax1 = taxCrud1.read(taxCrud1.create());
        Tax tax2 = taxCrud2.read(taxCrud2.create());

        //Allowed
        taxCrud1.read(tax1.getId());
        taxCrud2.update(tax2);

        try {
            taxCrud1.read(tax2.getId());
            fail("Should not be allowed");
        } catch(SecurityException ex) { }


        try {
            taxCrud2.update(tax1);
            fail("Should not be allowed");
        } catch(SecurityException ex) { }

    }
     
     
}