package dk.apaq.shopsystem.service.crud;

import org.dom4j.DocumentType;
import dk.apaq.crud.Crud;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Document;
import java.util.Date;
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
public class DocumentCrudTest {

    public DocumentCrudTest() {
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
        Crud.Complete<String, Document> crud = service.getOrganisationService(org).getDocuments();
        Document result = crud.read(crud.create());

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
        Crud.Complete<String, Document> crud = service.getOrganisationService(org).getDocuments();

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
        Crud.Complete<String, Document> crud = service.getOrganisationService(org).getDocuments();
        Document result = crud.read(crud.create());
        assertNotNull(result);
    }

    @Test
    public void testUpdate() {
        System.out.println("update");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());

       
        Crud.Complete<String, Document> crud = service.getOrganisationService(org).getDocuments();
        Document result = crud.read(crud.create());
        result.setName("name");
        crud.update(result);
        
        result = crud.read(result.getId());
        
        assertNotNull(result);
        assertNotNull(result.getId());

        
        assertEquals("name", result.getName());

    }

    @Test
    public void testDelete() {
        System.out.println("delete");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Complete<String, Document> crud = service.getOrganisationService(org).getDocuments();
        Document result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result = crud.read(id);
        assertNotNull(result);

        crud.delete(id);

        result = crud.read(id);
        assertNull(result);
    }

   


}