package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.LikeFilter;
import dk.apaq.filter.sort.SortDirection;
import dk.apaq.filter.sort.Sorter;
import dk.apaq.filter.sort.SorterEntry;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Product;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import dk.apaq.shopsystem.service.SystemService;
import java.util.List;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
public class ProductCrudTest extends AbstractJUnit4SpringContextTests   {

    public ProductCrudTest() {
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
        Crud.Complete<String, Product> crud = service.getOrganisationService(org).getProducts();
        Product result = crud.read(crud.create());

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
    public void testCreate() {
        System.out.println("create");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Complete<String, Product> crud = service.getOrganisationService(org).getProducts();
        Product item = crud.read(crud.create());

        assertNotNull(item);
    }

    @Test
    public void testUpdate() {
        System.out.println("update");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Complete<String, Product> crud = service.getOrganisationService(org).getProducts();
        Product result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result.setBarcode("barcode");
        result.setStockProduct(true);
        crud.update(result);

        result = crud.read(id);
        assertEquals("barcode", result.getBarcode());
        assertEquals(true, result.isStockProduct());

    }

    @Test
    public void testDelete() {
        System.out.println("delete");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Complete<String, Product> crud = service.getOrganisationService(org).getProducts();
        Product result = crud.read(crud.create());

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
        Crud.Editable<String, Product> crud = service.getOrganisationService(org).getProducts();

        for(int i=0;i<10;i++)
            crud.create();

        List<String> idlist = crud.listIds();
        assertTrue(idlist.size() >= 10);
    }

    /*
        @Test
    public void testListIdsFilter() {
        System.out.println("listIds");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Complete<String, Product> crud = service.getOrganisationService(org).getProducts();
        Product item = crud.read(crud.create());
        item.setName("Bob & Anne");
        item.setBarcode("1001");
        item.setItemNo("10012");
        crud.update(item);

        Filter filter1 = new LikeFilter("name", "Bob");
        Filter filter2 = new LikeFilter("name", "& anne", false);
        Filter filter3 = new LikeFilter("itemNo", "10021", 0.4F, false);
        Filter filter4 = new LikeFilter("barcode", "1001");

        List<String> idlist1 = crud.listIds(filter1, null);
        List<String> idlist2 = crud.listIds(filter2, null);
        List<String> idlist3 = crud.listIds(filter3, null);
        List<String> idlist4 = crud.listIds(filter4, null);

        assertFalse(idlist1.isEmpty());
        assertFalse(idlist2.isEmpty());
        assertFalse(idlist3.isEmpty());
        assertFalse(idlist4.isEmpty());

    }*/

    @Test
    public void testListIdsSorted() {
        System.out.println("listIds");
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org = orgcrud.read(orgcrud.create());
        Crud.Complete<String, Product> crud = service.getOrganisationService(org).getProducts();

        Product item = crud.read(crud.create());
        item.setName("Bob");
        item.setBarcode("1001");
        item.setItemNo("10012");
        crud.update(item);

        Product item2 = crud.read(crud.create());
        item2.setName("Ann");
        item2.setBarcode("1001");
        item2.setItemNo("10012");
        crud.update(item2);

        Product item3 = crud.read(crud.create());
        item3.setName("Karl");
        item3.setBarcode("1001");
        item3.setItemNo("10012");
        crud.update(item3);

        Sorter sorter = new Sorter("name", SortDirection.Ascending);

        List<String> idlist1 = crud.listIds(null, sorter);


        assertFalse(idlist1.isEmpty());
        assertEquals(idlist1.get(0), item2.getId());
        assertEquals(idlist1.get(1), item.getId());
        assertEquals(idlist1.get(2), item3.getId());

    }

    @Test
    public void testSecurity() {
        OrganisationCrud orgcrud = service.getOrganisationCrud();
        Organisation org1 = orgcrud.read(orgcrud.create());
        Organisation org2 = orgcrud.read(orgcrud.create());

        Crud.Editable<String, Product> itemCrud1 = service.getOrganisationService(org1).getProducts();
        Crud.Editable<String, Product> itemCrud2 = service.getOrganisationService(org2).getProducts();

        Product item1 = itemCrud1.read(itemCrud1.create());
        Product item2 = itemCrud2.read(itemCrud2.create());

        //Allowed
        itemCrud1.read(item1.getId());
        itemCrud2.update(item2);
        
        try {
            itemCrud1.read(item2.getId());
            fail("Should not be allowed");
        } catch(SecurityException ex) { }


        try {
            itemCrud2.update(item1);
            fail("Should not be allowed");
        } catch(SecurityException ex) { }

    }


}