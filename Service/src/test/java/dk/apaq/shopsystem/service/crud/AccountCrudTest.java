/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.core.ContainsFilter;
import dk.apaq.filter.core.LikeFilter;
import dk.apaq.shopsystem.service.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import dk.apaq.shopsystem.model.Account;
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
public class AccountCrudTest {

    public AccountCrudTest() {
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

    }

    @After
    public void tearDown() {
    }
    @Autowired
    private Service service;

    /**
     * Test of create method, of class service.getAccountCrud().
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        Crud.Editable<String, Account> crud = service.getAccountCrud();
        Account result = crud.read(crud.create());
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    /**
     * Test of update method, of class service.getAccountCrud().
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        Crud.Editable<String, Account> crud = service.getAccountCrud();
        Account result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result.setName("michael.krog");
        result.setDisplayname("Michael Krog");

        service.getAccountCrud().update(result);

        result = service.getAccountCrud().read(id);

        assertEquals("michael.krog", result.getName());
        assertEquals("Michael Krog", result.getDisplayname());
    }

    /**
     * Test of delete method, of class service.getAccountCrud().
     */
    @Test
    public void testDelete() {
        System.out.println("delete");
        Crud.Editable<String, Account> crud = service.getAccountCrud();
        Account result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result = service.getAccountCrud().read(id);
        assertNotNull(result);

        service.getAccountCrud().delete(id);

        result = service.getAccountCrud().read(id);
        assertNull(result);
    }

    /**
     * Test of read method, of class service.getAccountCrud().
     */
    @Test
    public void testRead() {
        System.out.println("read");
        Crud.Editable<String, Account> crud = service.getAccountCrud();
        Account result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result = service.getAccountCrud().read(id);
        assertNotNull(result);

        service.getAccountCrud().delete(id);

        result = service.getAccountCrud().read(id);
        assertNull(result);
    }

    /**
     * Test of listIds method, of class service.getAccountCrud().
     */
    @Test
    public void testListIds() {
        System.out.println("listIds");

        for (int i = 0; i < 10; i++) {
            service.getAccountCrud().create();
        }

        List<String> idlist = service.getAccountCrud().listIds();
        assertTrue(idlist.size() >= 10);
    }

    @Test
    public void testListIdsWithParams() {
        System.out.println("listIds");
        Crud.Editable<String, Account> crud = service.getAccountCrud();
            
        for (int i = 0; i < 10; i++) {
            Account account = crud.read(crud.create());
            account.setName(Integer.toString(i));
            account.setEmail(i + "@gmail.com");
            account.getIdentifiers().add(Integer.toString(i));
            service.getAccountCrud().update(account);
        }

        Filter filter = new CompareFilter("name", "5", CompareFilter.CompareType.Equals);
        List<String> idlist = service.getAccountCrud().listIds(filter, null, null);
        assertEquals(1, idlist.size());

        filter = new LikeFilter("email", "*@gmail.com");
        idlist = service.getAccountCrud().listIds(filter, null, null);
        assertEquals(10, idlist.size());

        filter = new ContainsFilter("identifiers", "5");
        idlist = service.getAccountCrud().listIds(filter, null, null);
        assertEquals(1, idlist.size());
    }

    @Test
    public void testSecurity() {
        Crud.Editable<String, Account> crud = service.getAccountCrud();
        Account account = crud.read(crud.create());
        String id = account.getId();
        account.setName(SecurityContextHolder.getContext().getAuthentication().getName());
        service.getAccountCrud().update(account);

        //Allowed
        service.getShopCrud().read(id);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("Jane", "Doe"));

        /*try {
        //Not allowed
        service.getAccountCrud().read(id);
        fail("Should not be allowed");
        } catch(SecurityException ex) {}
         */
        try {
            //Not allowed
            service.getAccountCrud().update(account);
            fail("Should not be allowed");
        }
        catch (SecurityException ex) {
        }
    }
}
