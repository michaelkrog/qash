package dk.apaq.shopsystem.controller;

import dk.apaq.shopsystem.rendering.Module;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Page;
import dk.apaq.shopsystem.rendering.Theme;
import dk.apaq.shopsystem.entity.Website;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Martin Christensen
 */

@Controller
public class HomeController  {

    /**
     * This method is just an idea of how generating a page could be when API's are done.
     * @return
     */
    public ModelAndView service() {
        //Based on the host we need to get a Website
        Website site = null;
        Organisation org = site.getOrganisation();

        //Based on the url we need to get a Page
        Page page = null;
        Theme template = /*page.getTemplate()*/ null;

        List<Module> modules = /*page.getModules() */ null;

        ModelAndView mav = new ModelAndView();

        //The viewresolver must deliver a view based on the given page.
        //This includes injecting needed javascript, fonts etc.
        //Use velocity for views as it supports dynamic concatenation of
        //different template data
        mav.setViewName(page.getId());

        mav.addObject("site", site);
        mav.addObject("page", page);
        //Get data required for modules and add them to mav for use in view

        return mav;

    }

    @RequestMapping("/")
    public ModelAndView helloWorld() {




        ModelAndView mav = new ModelAndView();
        mav.setViewName("layout");
        return mav;
    }
           
}