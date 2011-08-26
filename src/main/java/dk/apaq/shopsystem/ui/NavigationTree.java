/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.apaq.shopsystem.ui;

import com.vaadin.ui.Tree;

/**
 *
 * @author Martin Christensen
 */
public class NavigationTree extends Tree {
    
    public static final Object SHOW_ALL = "Users";
    public static final Object SEARCH = "Websites";

    
    public NavigationTree() {
             addItem(SHOW_ALL);
             addItem(SEARCH);
     }
    
 }


