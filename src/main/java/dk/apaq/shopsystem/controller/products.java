package dk.apaq.shopsystem.controller;

import dk.apaq.crud.Crud;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import dk.apaq.shopsystem.service.crud.EntityManagerCrudForSpring;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;

/**
 *
 * @author Martin Christensen
 */

@Controller
public class products  {

    @RequestMapping(value="/products/{Id}", method=RequestMethod.GET)
    public ModelAndView helloWorld(@PathVariable("Id") String Id) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("layout");
        mav.addObject("message", Id);

        return mav;
    }
    
}