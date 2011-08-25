package dk.apaq.shopsystem.controller;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Product;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;

/**
 *
 * @author Martin Christensen
 */

@Controller
public class ProductController implements ApplicationContextAware {

    private ApplicationContext context;


    @RequestMapping(value="/products/{Id}", method=RequestMethod.GET)
    public ModelAndView helloWorld(HttpServletRequest request, HttpServletResponse response, @PathVariable("Id") String orgId, @PathVariable("Id") String Id) {
        Service service = context.getBean(Service.class);

        //TODO: Somehow get a website that matches the request url.
        //      This may require a new crud managing websites across all
        //      organisations.
        Website website = /* insert found website*/ null;
        
        //Get the organisation
        Organisation org = website.getOrganisation();

        //Get the product owned by the organisation
        Crud<String, Product> productCrud = service.getProductCrud(org);
        Product product = productCrud.read(Id);

        //Build output
        ModelAndView mav = new ModelAndView();
        mav.setViewName("layout");
        mav.addObject("product", product);

        return mav;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
    
}