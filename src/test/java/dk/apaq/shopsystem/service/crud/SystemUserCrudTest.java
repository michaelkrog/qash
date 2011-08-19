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
import dk.apaq.shopsystem.model.SystemUser;
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
public class SystemUserCrudTest {

    public SystemUserCrudTest() {
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
        Crud.Editable<String, SystemUser> crud = service.getSystemUserCrud();
        SystemUser result = crud.read(crud.create());
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    /**
     * Test of update method, of class service.getAccountCrud().
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        Crud.Editable<String, SystemUser> crud = service.getSystemUserCrud();
        SystemUser result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result.setName("michael.krog");
        result.setDisplayname("Michael Krog");

        service.getSystemUserCrud().update(result);

        result = service.getSystemUserCrud().read(id);

        assertEquals("michael.krog", result.getName());
        assertEquals("Michael Krog", result.getDisplayname());
    }

    /**
     * Test of delete method, of class service.getAccountCrud().
     */
    @Test
    public void testDelete() {
        System.out.println("delete");
        Crud.Editable<String, SystemUser> crud = service.getSystemUserCrud();
        SystemUser result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result = service.getSystemUserCrud().read(id);
        assertNotNull(result);

        service.getSystemUserCrud().delete(id);

        result = service.getSystemUserCrud().read(id);
        assertNull(result);
    }

    /**
     * Test of read method, of class service.getAccountCrud().
     */
    @Test
    public void testRead() {
        System.out.println("read");
        Crud.Editable<String, SystemUser> crud = service.getSystemUserCrud();
        SystemUser result = crud.read(crud.create());

        assertNotNull(result);
        assertNotNull(result.getId());

        String id = result.getId();

        result = service.getSystemUserCrud().read(id);
        assertNotNull(result);

        service.getSystemUserCrud().delete(id);

        result = service.getSystemUserCrud().read(id);
        assertNull(result);
    }

    /**
     * Test of listIds method, of class service.getAccountCrud().
     */
    @Test
    public void testListIds() {
        System.out.println("listIds");

        for (int i = 0; i < 10; i++) {
            service.getSystemUserCrud().create();
        }

        List<String> idlist = service.getSystemUserCrud().listIds();
        assertTrue(idlist.size() >= 10);
    }

    @Test
    public void testListIdsWithParams() {
        System.out.println("listIds");
        Crud.Editable<String, SystemUser> crud = service.getSystemUserCrud();
            
        for (int i = 0; i < 10; i++) {
            SystemUser account = crud.read(crud.create());
            account.setName(Integer.toString(i));
            account.setEmail(i + "@gmail.com");
            account.getIdentifiers().add(Integer.toString(i));
            service.getSystemUserCrud().update(account);
        }

        Filter filter = new CompareFilter("name", "5", CompareFilter.CompareType.Equals);
        List<String> idlist = service.getSystemUserCrud().listIds(filter, null, null);
        assertEquals(1, idlist.size());

        filter = new LikeFilter("email", "*@gmail.com");
        idlist = service.getSystemUserCrud().listIds(filter, null, null);
        assertEquals(10, idlist.size());

        filter = new ContainsFilter("identifiers", "5");
        idlist = service.getSystemUserCrud().listIds(filter, null, null);
        assertEquals(1, idlist.size());
    }

    @Test
    public void testSecurity() {
        Crud.Editable<String, SystemUser> crud = service.getSystemUserCrud();
        SystemUser user = crud.read(crud.create());
        String id = user.getId();
        user.setName(SecurityContextHolder.getContext().getAuthentication().getName());
        service.getSystemUserCrud().update(user);

        //Allowed
        service.getOrganisationCrud().read(id);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("Jane", "Doe"));

        /*try {
        //Not allowed
        service.getAccountCrud().read(id);
        fail("Should not be allowed");
        } catch(SecurityException ex) {}
         */
        try {
            //Not allowed
            service.getSystemUserCrud().update(user);
            fail("Should not be allowed");
        }
        catch (SecurityException ex) {
        }
    }
}
