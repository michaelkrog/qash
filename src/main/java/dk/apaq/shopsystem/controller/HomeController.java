package dk.apaq.shopsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Martin Christensen
 */

@Controller
public class HomeController  {

    @RequestMapping("/")
    public ModelAndView helloWorld() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("layout");
        mav.addObject("message", "ShoppinNet V.2");
        return mav;
    }
    
    @RequestMapping("/testing")
    public ModelAndView helloWorld2() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("layout");
        mav.addObject("message", "Testing...");
        return mav;
    }
        
}