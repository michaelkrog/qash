package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.entity.Organisation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import dk.apaq.shopsystem.entity.Store;
import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.entity.Domain;
import dk.apaq.shopsystem.entity.Website;
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
public class DomainCrudTest {

    public DomainCrudTest() {
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
        Crud.Editable<String, Domain> crud = service.getOrganisationService(org).getDomains();
        Domain result = crud.read(crud.create());

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
        Crud.Editable<String, Domain> crud = service.getOrganisationService(org).getDomains();

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

        Crud.Complete<String, Website> websiteCrud = orgService.getWebsites();
        Website website = websiteCrud.read(websiteCrud.create());

        Crud.Complete<String, Domain> domainCrud = orgService.getDomains();
        Domain domain = domainCrud.read(domainCrud.create());
        domain.setName("www.blabla.dk");
        domain.setWebsite(website);
        orgService.getDomains().update(domain);

        
        //create a thousands organsiations with each there domain, site and subdomain.
        for(int i=0;i<200;i++) {
            Organisation tmpOrg = orgcrud.read(orgcrud.create());
            OrganisationService tmpOrgService = service.getOrganisationService(org);

            Website tmpSite = tmpOrgService.getWebsites().read(tmpOrgService.getWebsites().create());

            Domain tmpDomain = tmpOrgService.getDomains().read(tmpOrgService.getDomains().create());
            tmpDomain.setName("tmpdomain_"+i);
            tmpDomain.setWebsite(tmpSite);
            tmpOrgService.getDomains().update(tmpDomain);

            
            if(i % 1000 == 0) {
                System.gc();
            }
        }

        long startTime = System.currentTimeMillis();
        List<String> ids = domainCrud.listIds(new CompareFilter("name", "www.blabla.dk", CompareFilter.CompareType.Equals), null);
        long endTime = System.currentTimeMillis();
        assertFalse(ids.isEmpty());
        assertTrue("Searching by domain took more than 50 msec(time:"+(endTime-startTime)+"ms.)", endTime-startTime<50);
        System.out.println("Searching by domain took more "+(endTime-startTime)+"ms.");

        ids = domainCrud.listIds(new CompareFilter("name", "blabla.dk", CompareFilter.CompareType.Equals), null);
        assertTrue(ids.isEmpty());
    }

    @Test
    public void testCreate() {
        System.out.println("create");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Editable<String, Domain> crud = service.getOrganisationService(org).getDomains();
        Domain Domain = crud.read(crud.create());

        assertNotNull(Domain);
    }

    @Test
    public void testUpdate() {
        System.out.println("update");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Editable<String, Domain> crud = service.getOrganisationService(org).getDomains();
        Domain result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result.setName("Moms");
        crud.update(result);

        result = crud.read(id);
        assertEquals("Moms", result.getName());
        
    }

    @Test
    public void testDelete() {
        System.out.println("delete");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Editable<String, Domain> crud = service.getOrganisationService(org).getDomains();
        Domain result = crud.read(crud.create());

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

        Crud.Editable<String, Domain> DomainCrud1 = service.getOrganisationService(org1).getDomains();
        Crud.Editable<String, Domain> DomainCrud2 = service.getOrganisationService(org2).getDomains();

        Domain Domain1 = DomainCrud1.read(DomainCrud1.create());
        Domain Domain2 = DomainCrud2.read(DomainCrud2.create());

        //Allowed
        DomainCrud1.read(Domain1.getId());
        DomainCrud2.update(Domain2);

        try {
            DomainCrud1.read(Domain2.getId());
            fail("Should not be allowed");
        } catch(SecurityException ex) { }


        try {
            DomainCrud2.update(Domain1);
            fail("Should not be allowed");
        } catch(SecurityException ex) { }

    }
     
     
}