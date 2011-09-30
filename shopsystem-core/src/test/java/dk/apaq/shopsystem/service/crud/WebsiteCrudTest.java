package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.Domain;
import dk.apaq.shopsystem.entity.DomainRegistration;
import dk.apaq.shopsystem.entity.Organisation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import dk.apaq.shopsystem.entity.Store;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.ContainsDomainFilter;
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
        Crud.Editable<String, Website> crud = service.getOrganisationService(org).getWebsites();
        Website result = crud.read(crud.create());

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
        Crud.Editable<String, Website> crud = service.getOrganisationService(org).getWebsites();

        for(int i=0;i<10;i++)
            crud.create();

        List<String> idlist = crud.listIds();
        assertTrue(idlist.size() >= 10);
    }

      @Test
    public void testListIdsByDomain() {
        System.out.println("listIds");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        OrganisationService orgService = service.getOrganisationService(org);

        Domain domain = orgService.getDomains().read(orgService.getDomains().create());
        domain.setName("blabla.dk");
        orgService.getDomains().update(domain);

        Crud.Complete<String, Website> crud = orgService.getWebsites();

        Website website = crud.read(crud.create());
        DomainRegistration dr = new DomainRegistration();
        dr.setDomain(domain);
        dr.setSubDomain("bla");
        website.getDomainRegistrations().add(dr);
        crud.update(website);

        //create a thousands organsiations with each there domain, site and subdomain.
        for(int i=0;i<500;i++) {
            Organisation tmpOrg = orgcrud.read(orgcrud.create());
            OrganisationService tmpOrgService = service.getOrganisationService(org);

            Domain tmpDomain = tmpOrgService.getDomains().read(orgService.getDomains().create());
            tmpDomain.setName("tmpdomain_"+i);
            tmpOrgService.getDomains().update(tmpDomain);

            Website tmpSite = tmpOrgService.getWebsites().read(tmpOrgService.getWebsites().create());
            DomainRegistration tmpdr = new DomainRegistration();
            tmpdr.setDomain(tmpDomain);
            tmpdr.setSubDomain("test_"+i);
            tmpSite.getDomainRegistrations().add(dr);
            tmpOrgService.getWebsites().update(website);

            if(i % 1000 == 0) {
                System.gc();
            }
        }

        long startTime = System.currentTimeMillis();
        List<String> ids = crud.listIds(new ContainsDomainFilter("domains", "blabla.dk", "bla"), null);
        long endTime = System.currentTimeMillis();
        assertFalse(ids.isEmpty());
        assertTrue("Searching by domain took more than 25 msec(time:"+(endTime-startTime)+"ms.)", endTime-startTime<25);
        System.out.println("Searching by domain took more "+(endTime-startTime)+"ms.");

        ids = crud.listIds(new ContainsDomainFilter("domains", "bla.dk", "bla"), null);
        assertTrue(ids.isEmpty());
    }

    @Test
    public void testCreate() {
        System.out.println("create");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Editable<String, Website> crud = service.getOrganisationService(org).getWebsites();
        Website Website = crud.read(crud.create());

        assertNotNull(Website);
    }

    @Test
    public void testUpdate() {
        System.out.println("update");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Editable<String, Website> crud = service.getOrganisationService(org).getWebsites();
        Website result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result.setName("My Site");
        crud.update(result);

        result = crud.read(id);
        assertEquals("My Site", result.getName());
        
    }

    @Test
    public void testDelete() {
        System.out.println("delete");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Editable<String, Website> crud = service.getOrganisationService(org).getWebsites();
        Website result = crud.read(crud.create());

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

        Crud.Editable<String, Website> WebsiteCrud1 = service.getOrganisationService(org1).getWebsites();
        Crud.Editable<String, Website> WebsiteCrud2 = service.getOrganisationService(org2).getWebsites();

        Website Website1 = WebsiteCrud1.read(WebsiteCrud1.create());
        Website Website2 = WebsiteCrud2.read(WebsiteCrud2.create());

        //Allowed
        WebsiteCrud1.read(Website1.getId());
        WebsiteCrud2.update(Website2);

        try {
            WebsiteCrud1.read(Website2.getId());
            fail("Should not be allowed");
        } catch(SecurityException ex) { }


        try {
            WebsiteCrud2.update(Website1);
            fail("Should not be allowed");
        } catch(SecurityException ex) { }

    }
     
     
}