/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.apaq.shopsystem.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class tax { 

    @RequestMapping(value = "/_api", method = RequestMethod.GET)
    //public String getTaxList(@PathVariable String Id) {
    public String getTaxList() {
        return("tax found...");
    }
    
}
